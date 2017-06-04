package game.server.login;

import game.common.net.*;
import game.server.Server;

/**
 * 登录服务器，向客户端提供登录服务
 * Created by wuy on 2017/5/24.
 */
public class LoginServer extends Server {

	private ClientAgentManager clientAgentManager;
	private ServerSocketService loginServerForClientSocketService;

	public LoginServer(String serverId) {
		super(serverId);
	}

	public void startServerForClientSocketService(String host, int port) {
		clientAgentManager = new ClientAgentManager();
		loginServerForClientSocketService = new ServerSocketService(new LoginServerForClientMessageHandler(this, clientAgentManager));
		try {
			loginServerForClientSocketService.start(host, port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
