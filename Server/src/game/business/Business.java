package game.business;

import game.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务
 * Created by wuy on 2017/6/22.
 */
public class Business {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	// 服务器
	private Server server;
	// 是否服务器业务
	private boolean isServerBusiness;

	public Logger getLogger() {
		return logger;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public boolean isServerBusiness() {
		return isServerBusiness;
	}

	public void setServerBusiness(boolean serverBusiness) {
		isServerBusiness = serverBusiness;
	}
}
