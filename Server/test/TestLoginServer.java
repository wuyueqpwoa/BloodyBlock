import game.server.login.LoginServer;

/**
 * 测试登录服务器
 * Created by wuy on 2017/5/21.
 */
public class TestLoginServer {

	private static void testLoginServer() throws Exception {
		LoginServer loginServer = new LoginServer();
		loginServer.init();
		System.out.println(loginServer);
		loginServer.start();
	}

	public static void main(String[] args) throws Exception {
		testLoginServer();
	}
}
