package game.common.net;

import game.common.message.CloneableMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 套接字服务
 * Created by wuy on 2017/6/8.
 */
public abstract class SocketService {

	private Logger logger;
	// 服务名称
	private String name;
	// 地址
	private String host;
	// 端口
	private int port;
	// 最大帧长度10Mb=1310720B
	private int frameLength = 1024 * 1024 * 10;
	// 可克隆的消息处理者
	private CloneableMessageHandler cloneableMessageHandler;

	public SocketService() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public Logger getLogger() {
		return logger;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getFrameLength() {
		return frameLength;
	}

	public void setFrameLength(int frameLength) {
		this.frameLength = frameLength;
	}

	public CloneableMessageHandler getCloneableMessageHandler() {
		return cloneableMessageHandler;
	}

	public void setCloneableMessageHandler(CloneableMessageHandler cloneableMessageHandler) {
		this.cloneableMessageHandler = cloneableMessageHandler;
	}

	/**
	 * 启动
	 *
	 * @throws Exception 启动异常
	 */
	public void start() throws Exception {
		if (isAlive()) {
			logger.error("has been started.");
			return;
		}
		logger.info("starting as server...");
		startService();
		logger.info("started as server.");
	}

	/**
	 * 停止
	 *
	 * @throws Exception 停止异常
	 */
	public void stop() throws Exception {
		if (!isAlive()) {
			getLogger().error("has been stopped.");
			return;
		}
		getLogger().info("stopping...");
		stopService();
		getLogger().info("stopped.");
	}

	/**
	 * 启动服务
	 *
	 * @throws Exception 启动异常
	 */
	public abstract void startService() throws Exception;


	/**
	 * 停止服务
	 *
	 * @throws Exception 停止异常
	 */
	public abstract void stopService() throws Exception;


	/**
	 * 是否存活
	 *
	 * @return 存活，返回true；否则，返回false
	 */
	public abstract boolean isAlive();

	@Override
	public String toString() {
		return "SocketService{" +
				"name='" + name + '\'' +
				", host='" + host + '\'' +
				", port=" + port +
				", frameLength=" + frameLength +
				", cloneableMessageHandler=" + cloneableMessageHandler +
				'}';
	}
}
