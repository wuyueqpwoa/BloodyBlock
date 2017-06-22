package game.common.net;

import io.netty.channel.Channel;

/**
 * 用户端代理管理器
 * Created by wuy on 2017/5/25.
 */
public class UserAgentManager extends SocketAgentManager<UserAgent> {

	/**
	 * 将通道托管(服务端添加客户端)
	 *
	 * @param channel 通道
	 * @return 用户端代理
	 */
	synchronized public UserAgent add(Channel channel) {
		return add(new UserAgent(channel));
	}
}
