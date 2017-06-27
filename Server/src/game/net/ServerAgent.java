package game.net;

import io.netty.channel.Channel;

/**
 * 服务器代理
 * Created by wuy on 2017/6/22.
 */
public class ServerAgent extends Agent {

	// 是否为服务端
	private boolean isServer;
	// 服务器ID
	private String id;

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
	}
}
