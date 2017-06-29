package game.server.login;

import game.business.Business;
import game.net.message.Message;
import game.server.UserServer;

import java.io.IOException;

/**
 * 来自数据库服务器的业务
 * Created by wuy on 2017/6/26.
 */
public class FromDatabaseBusiness extends Business {

	// 检查账号结果
	public Message checkAccountResult(Message message) throws IOException {
		message.setInvokeMethodName(message.getUserCallbackMethodName());
		message.setAgent(((UserServer) getServer()).getUserAgentManager().get(message.getUserChannelId()));
		return message;
	}
}
