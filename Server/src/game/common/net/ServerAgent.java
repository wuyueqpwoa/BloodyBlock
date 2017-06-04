package game.common.net;

import io.netty.channel.Channel;

/**
 * 服务端代理
 * 套接字连接以通道为单位，对该通道进行封装
 * Created by wuy on 2017/5/25.
 */
public class ServerAgent extends SocketAgent {

	// 服务器ID
	private String serverId;
	// 以服务器方式启动
	private boolean isAsServer;

	public ServerAgent(Channel channel, boolean isAsServer) {
		super(channel);
		this.isAsServer = isAsServer;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public boolean isAsServer() {
		return isAsServer;
	}

	public void setAsServer(boolean asServer) {
		isAsServer = asServer;
	}

	@Override
	public String toString() {
		return "ServerAgent{" +
				"serverId='" + serverId + '\'' +
				", channel=" + getChannel() +
				", isAsServer=" + isAsServer +
				'}';
	}
}
