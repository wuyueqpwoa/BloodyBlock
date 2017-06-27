package game.server.gateway;

import game.business.Business;
import game.net.message.Message;

import java.io.IOException;

/**
 * 登录服务器业务
 * Created by wuy on 2017/6/26.
 */
public class LoginServerBusiness extends Business {

	// 准备接收用户
	public Message prepareAcceptUser(Message message) throws IOException {
		return null;
	}
}
