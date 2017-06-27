package game.server.exchange;

import game.business.Business;
import game.net.ServerAgent;
import game.net.message.Message;

import java.io.IOException;

/**
 * 消息交换服务端业务
 * Created by wuy on 2017/6/27.
 */
public class ExchangeServerBusiness extends Business {

	// 默认处理
	public Message defaultHandle(Message message) throws IOException {
		ServerAgent serverAgent = (ServerAgent) message.getAgent();
		// 给交换服务器的消息
		if (message.getDestinationServerId() == null
				|| getServer().getId().equals(message.getDestinationServerId())) {
			serverAgent.setId(message.getSourceServerId());
		} else {
			// 需要转发的消息
			ServerAgent targetAgent = getServer().getServerAgentManager().get(message.getDestinationServerId());
			targetAgent.writeAndFlush(message);
		}
		return null;
	}
}
