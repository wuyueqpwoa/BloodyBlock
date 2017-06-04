package game.common.net;

import io.netty.channel.Channel;

import javax.crypto.SecretKey;

/**
 * 套接字代理
 * 套接字连接以通道为单位，对该通道进行封装
 * Created by wuy on 2017/5/25.
 */
public class SocketAgent {

	// 唯一索引(自增)
	private static int uniqueIndex;

	private Integer connectId;
	private Channel channel;

	public SocketAgent(Channel channel) {
		this.connectId = ++uniqueIndex;
		this.channel = channel;
	}

	public Integer getConnectId() {
		return connectId;
	}

	public void setConnectId(Integer connectId) {
		this.connectId = connectId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SocketAgent that = (SocketAgent) o;
		return connectId == that.connectId;
	}

	@Override
	public int hashCode() {
		return connectId;
	}

	@Override
	public String toString() {
		return "SocketAgent{" +
				"connectId=" + connectId +
				", channel=" + channel +
				'}';
	}
}
