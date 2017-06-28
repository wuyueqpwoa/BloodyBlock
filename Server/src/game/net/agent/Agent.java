package game.net.agent;

import game.net.message.Message;
import io.netty.channel.Channel;

import java.io.IOException;

/**
 * 代理
 * Created by wuy on 2017/6/22.
 */
public class Agent {

	private Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * 输出消息
	 *
	 * @param message 消息
	 * @throws IOException 输出异常
	 */
	public void writeAndFlush(Message message) throws IOException {
		channel.writeAndFlush(Message.pack(message));
	}
}
