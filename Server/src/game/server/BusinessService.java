package game.server;

import game.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 业务服务(单线程)
 * Created by wuy on 2017/6/22.
 */
public class BusinessService {

	final private Logger logger;
	// 服务器
	private Server server;

	public BusinessService() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public Logger getLogger() {
		return logger;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public MessageQueue getMessageQueue() {
		return server.getMessageQueue();
	}
}
