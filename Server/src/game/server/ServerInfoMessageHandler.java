package game.server;

import game.net.ServerAgent;
import game.net.message.Message;
import game.net.message.ServerMessageHandler;
import org.msgpack.MessagePack;
import org.msgpack.type.MapValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务器信息的消息处理者
 * Created by wuy on 2017/6/10.
 */
public class ServerInfoMessageHandler extends ServerMessageHandler {

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
		newMessage.setParameter(newParameter);
//		send(newMessage, serverAgent);
	}
}
