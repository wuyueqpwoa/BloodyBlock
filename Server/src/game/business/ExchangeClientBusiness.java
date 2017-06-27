package game.business;

import game.net.ServerAgent;
import game.net.message.Message;

import java.io.IOException;

/**
 * 消息交换客户端业务
 * Created by wuy on 2017/6/27.
 */
public class ExchangeClientBusiness extends Business {

	// 服务端上线
	public Message serverOnline(Message message) throws IOException {
		ServerAgent serverAgent = (ServerAgent) message.getAgent();
		serverAgent.setId(message.getSourceServerId());
		return null;
	}
}
