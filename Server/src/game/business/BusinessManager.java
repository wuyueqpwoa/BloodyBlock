package game.business;

import game.net.message.Message;
import game.net.message.MessageManager;
import game.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 业务管理器(单线程)
 * Created by wuy on 2017/6/22.
 */
public class BusinessManager {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	// 服务器
	private Server server;
	// 业务Map(key为方法名)
	private Map<String, Business> businessMap = new LinkedHashMap<>();
	// 方法Map(key为方法名)
	private Map<String, Method> methodMap = new LinkedHashMap<>();
	// 消息管理器
	private MessageManager messageManager;
	// 是否存活
	private boolean isAlive;

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Map<String, Business> getBusinessMap() {
		return businessMap;
	}

	public Map<String, Method> getMethodMap() {
		return methodMap;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public void add(Business business) throws Exception {
		// 找到所有符合条件的方法，进行注册
		for (Method m : business.getClass().getMethods()) {
			Class[] parameterTypes = m.getParameterTypes();
			// 入参仅有Message对象
			if (parameterTypes != null
					&& parameterTypes.length == 1
					&& parameterTypes[0].equals(Message.class)) {
				if (businessMap.containsKey(m.getName())) {
					throw new Exception("find same name method:" + methodMap.get(m.getName()) + ", and:" + m);
				}
				businessMap.put(m.getName(), business);
				methodMap.put(m.getName(), m);
			}
		}
	}

	public int size() {
		return businessMap.size();
	}

	/**
	 * 开始业务处理
	 */
	public void start() {
		if (isAlive) {
			return;
		} else {
			isAlive = true;
		}
		logger.info("starting...methodMap:" + methodMap);
		new Thread() {
			@Override
			public void run() {
				logger.info("started.");
				Message message;
				while (isAlive) {
					try {
						message = messageManager.takeFirst();
					} catch (InterruptedException e) {
						logger.error("take message error:", e);
						continue;
					}
					// 业务处理
					long startTime = System.currentTimeMillis();
					logger.info("business start at:" + startTime + ", message:" + message);
					try {
						handle(message);
					} catch (Exception e) {
						logger.error("business error:", e);
					}
					long endTime = System.currentTimeMillis();
					logger.info("business end at:" + endTime + ", it takes:" + (endTime - startTime));
				}
				logger.info("stopped.");
			}
		}.start();
	}

	// 处理消息
	private void handle(Message message) throws Exception {
		// 消息的目标服务器ID与本服务器相等
		if (server.getId().equals(message.getDestinationServerId())) {
			// 找到调用方法
			Business business = businessMap.get(message.getInvokeMethodName());
			Method method = methodMap.get(message.getInvokeMethodName());
			Message newMessage = (Message) method.invoke(business, message);
			// 回调
			if (newMessage != null) {
				newMessage.getAgent().writeAndFlush(newMessage);
			}
		}
	}

	/**
	 * 停止业务处理
	 */
	public void stop() {
		logger.info("stopping...");
		isAlive = false;
	}

	/**
	 * 是否存活
	 *
	 * @return 存活，返回true；否则，返回false
	 */
	public boolean isAlive() {
		return isAlive;
	}
}
