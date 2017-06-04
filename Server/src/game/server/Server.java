package game.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 服务器
 * Created by wuy on 2017/5/16.
 */
public class Server {
	// 服务器类型
	private String serverType = "Default";
	// 服务器ID，格式一定为"服务器类型_编号"
	private String serverId = "Default_01";

	public Server() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("server.properties"));
			String configString = (String) properties.get(this.getClass().getSimpleName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Server(String serverId) {
		this.serverType = serverId.split("_")[0];
		this.serverId = serverId;
	}

	public String getServerType() {
		return serverType;
	}

	public String getServerId() {
		return serverId;
	}
}
