package game.server.center;

import game.common.net.ServerAgentManager;
import game.common.net.ServerSocketService;
import game.server.Server;

/**
 * 中心服务器，向所有服务器提供其他服务器信息查询服务
 * Created by wuy on 2017/5/26.
 */
public class CenterServer extends Server {
	private ServerAgentManager serverAgentManager;
	private ServerSocketService centerServerForServerSocketService;

//	public CenterServer(String serverId) {
//		super(serverId);
//	}

	public void startServerForServerSocketService(String host, int port) {
		serverAgentManager = new ServerAgentManager();
		centerServerForServerSocketService = new ServerSocketService();
//		try {
//			centerServerForServerSocketService.start(host, port, new CenterServerForServerMessageHandler(this, serverAgentManager));
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
}
