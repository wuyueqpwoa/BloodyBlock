package game.server.center;

import game.common.message.CloneableMessageHandler;
import game.common.message.Message;
import game.common.message.ServerForServerMessageHandler;
import game.common.net.ServerAgent;
import game.common.net.ServerAgentManager;
import game.server.Server;
import org.msgpack.MessagePack;
import org.msgpack.type.MapValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录服务器，面向服务端的服务器套接字服务
 * Created by wuy on 2017/5/25.
 */
public class CenterServerForServerMessageHandler extends ServerForServerMessageHandler {

	public CenterServerForServerMessageHandler(Server server, ServerAgentManager serverAgentManager) {
		super(server, serverAgentManager);
	}

	@Override
	public CloneableMessageHandler clone() {
		return new CenterServerForServerMessageHandler(getServer(), getServerAgentManager());
	}

	// 登录
	public void login(ServerAgent serverAgent, Message message) throws IOException {
		MessagePack messagePack = new MessagePack();
		MapValue parameter = messagePack.read(message.getParameterBytes()).asMapValue();
		getLogger().info("parameter:" + parameter);
		Message newMessage = new Message();
		newMessage.setInvokeMethodName(message.getCallbackMethodName());
		Map<String, Object> newParameter = new HashMap<>();
		newParameter.put("code", "0");
		List<String> gameWorldServerList = new ArrayList<>();
		gameWorldServerList.add("GWS_01");
		gameWorldServerList.add("GWS_02");
		gameWorldServerList.add("GWS_03");
		newParameter.put("gws_list", gameWorldServerList);
		newMessage.setAndPackParameterBytes(newParameter);
//		send(newMessage, serverAgent);
	}
}
