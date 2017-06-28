package game.business;

import game.net.agent.ServerAgent;
import game.net.message.Message;

import java.io.IOException;

/**
 * 到交换服务器的业务
 * Created by wuy on 2017/6/27.
 */
public class ToExchangeServerBusiness extends Business {

	// 服务端上线
	public Message serverOnline(Message message) throws IOException {
		getLogger().info("server online:" + message.getSourceServerId());
		ServerAgent serverAgent = (ServerAgent) message.getAgent();
		serverAgent.setId(message.getSourceServerId());
		getServer().getServerAgentManager().updateId(serverAgent.getChannel());
		return null;
	}
}
