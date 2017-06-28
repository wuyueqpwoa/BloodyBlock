package game.net.agent;

/**
 * 服务器代理
 * Created by wuy on 2017/6/22.
 */
public class ServerAgent extends Agent {

	// 是否为服务端
	private boolean isServer;
	// 服务器ID
	private String id;
	// 服务器类型
	private String type;

	public boolean isServer() {
		return isServer;
	}

	public void setServer(boolean server) {
		isServer = server;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.type = id.split("_")[0];
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
