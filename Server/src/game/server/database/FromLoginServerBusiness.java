package game.server.database;

import game.business.Business;
import game.net.message.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 来自登录服务器的业务
 * Created by wuy on 2017/6/26.
 */
public class FromLoginServerBusiness extends Business {

	// 获得结果消息，数据库操作的逻辑都一样，消息会原路返回
	private Message getResultMessage(Message message, Map<String, Object> parameter) throws IOException {
		message.setDestinationServerId(message.getSourceServerId());
		message.setSourceServerId(getServer().getId());
		message.setInvokeMethodName(message.getCallbackMethodName());
		message.setParameter(parameter);
		message.setCallbackMethodName(null);
		message.setAgent(getServer().getServerAgentManager().getByServerId("ES_01"));
		return message;
	}

	// 检查账号
	public Message checkAccount(Message message) throws IOException {
		getLogger().info("check account:" + message);
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("code", "0");
		return getResultMessage(message, parameter);
	}
}
