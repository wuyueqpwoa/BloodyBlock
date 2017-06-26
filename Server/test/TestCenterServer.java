import game.server.center.CenterServer;
import game.server.login.LoginServer;

/**
 * 测试中心服务器
 * Created by wuy on 2017/5/21.
 */
public class TestCenterServer {

	private static void testCenterServer() throws Exception {
		CenterServer centerServer = new CenterServer();
		centerServer.init();
		System.out.println(centerServer);
		centerServer.start();
	}

	public static void main(String[] args) throws Exception {
		testCenterServer();
	}
}
