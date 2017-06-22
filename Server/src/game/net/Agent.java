package game.net;

import io.netty.channel.Channel;

/**
 * 代理
 * Created by wuy on 2017/6/22.
 */
public class Agent {

	private Channel channel;

	public Agent(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}
}
