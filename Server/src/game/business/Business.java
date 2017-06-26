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
	// 服务名称
	private String name;

	public Logger getLogger() {
		return logger;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
