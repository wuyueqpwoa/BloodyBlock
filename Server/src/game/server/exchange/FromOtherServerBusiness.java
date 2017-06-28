package game.server.exchange;

import game.business.Business;
import game.net.agent.ServerAgent;
import game.net.message.Message;

import java.io.IOException;

/**
 * 来自其它服务的业务
 * Created by wuy on 2017/6/27.
 */
public class FromOtherServerBusiness extends Business {

	// 默认处理，转发消息
	public Message defaultHandle(Message message) throws IOException {
		// 给交换服务器的消息
		if (message.getDestinationServerId() == null) {
			getLogger().error("error destination server id:" + null);
		} else if (getServer().getId().equals(message.getDestinationServerId())) {
			clientOnline(message);
		} else {
			// 需要转发的消息
			ServerAgent targetAgent = getServer().getServerAgentManager().get(message.getDestinationServerId());
			if (targetAgent == null) {
				getLogger().error("unknown destination server id:" + message.getDestinationServerId());
				// 服务器间连接出现问题
			} else {
				targetAgent.writeAndFlush(message);
			}
		}
		return null;
	}

	// 客户端上线
	public Message clientOnline(Message message) throws IOException {
		getLogger().info("client online:" + message.getSourceServerId());
		ServerAgent serverAgent = (ServerAgent) message.getAgent();
		serverAgent.setId(message.getSourceServerId());
		getServer().getServerAgentManager().updateId(serverAgent.getChannel());
		return null;
	}
}
