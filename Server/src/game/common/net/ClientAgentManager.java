package game.common.net;

import io.netty.channel.Channel;

/**
 * 客户端代理管理器
 * Created by wuy on 2017/5/25.
 */
public class ClientAgentManager extends SocketAgentManager<ClientAgent> {

	/**
	 * 将通道托管(服务端添加客户端)
	 *
	 * @param channel 通道
	 * @return 客户端代理
	 */
	public ClientAgent add(Channel channel) {
		return add(new ClientAgent(channel));
	}
}
