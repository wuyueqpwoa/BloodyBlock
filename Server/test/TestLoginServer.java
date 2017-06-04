import game.server.login.LoginServer;

/**
 * 测试游戏世界服务器
 * Created by wuy on 2017/5/21.
 */
public class TestLoginServer {

	private static void testServerForClient() {
		LoginServer loginServer = new LoginServer("LS_01");
		loginServer.startServerForClientSocketService("127.0.0.1", 18000);
	}

	public static void main(String[] args) {
		testServerForClient();
	}
}
