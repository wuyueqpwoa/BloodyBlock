package game.server;

import game.business.BusinessManager;
import game.net.*;
import game.net.agent.*;
import game.util.ConfigUtil;
import game.net.message.MessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器，功能全面
 * Created by wuy on 2017/5/16.
 */
public class Server {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	// 服务器ID，格式为"服务器类型_编号"
	private String id;
	// 服务器代理管理器
	private ServerAgentManager serverAgentManager = new ServerAgentManager();
	// 服务器网络服务管理器
	private ServerNetServiceManager serverNetServiceManager = new ServerNetServiceManager();
	// 消息管理器
	private MessageManager messageManager = new MessageManager();
	// 业务管理器
	private BusinessManager businessManager = new BusinessManager();

	public Logger getLogger() {
		return logger;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ServerAgentManager getServerAgentManager() {
		return serverAgentManager;
	}

	public ServerNetServiceManager getServerNetServiceManager() {
		return serverNetServiceManager;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public BusinessManager getBusinessManager() {
		return businessManager;
	}

	/**
	 * 初始化
	 */
	public void init() throws Exception {
		// 通过配置文件初始化服务器
		ConfigUtil.initServerByConfig(this);
		businessManager.setServer(this);
		businessManager.setMessageManager(messageManager);
	}

	/**
	 * 启动
	 */
	public void start() throws Exception {
		serverNetServiceManager.start();
		businessManager.start();
	}

	@Override
	public String toString() {
		return "Server{" +
				"id='" + id + '\'' +
				", serverAgentManager=" + serverAgentManager +
				", serverNetServiceManager=" + serverNetServiceManager +
				", messageManager=" + messageManager +
				", businessManager=" + businessManager +
				'}';
	}
}
