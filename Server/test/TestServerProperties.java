import game.common.config.ConfigUtil;
import game.server.login.LoginServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * 测试服务器配置文件
 * Created by wuy on 2017/6/6.
 */
public class TestServerProperties {

	private static void testLoginServer() throws IOException, ParseException {
		JSONObject config1 = ConfigUtil.getServerProperties("LoginServer");
		System.out.println(config1);
		for (Object serverId : config1.keySet()) {
			JSONObject config2 = (JSONObject) config1.get(serverId);
			LoginServer loginServer = new LoginServer((String) serverId);
			for (Object serverWay : config2.keySet()) {
				JSONObject config3 = (JSONObject) config2.get(serverWay);
				if ("serverForUser".equals(serverWay)) {
					String host = (String) config3.get("host");
					int port = Integer.parseInt((String) config3.get("port"));
					loginServer.startUserSocketService(host, port);
				}
				/*else if ("serverForServer".equals(serverWay)) {
					String host = (String) config3.get("host");
					int port = Integer.parseInt((String) config3.get("port"));
					loginServer.startServerForServerSocketService(host, port);
				}*/
			}
		}
	}

	public static void main(String[] args) throws Exception {
		testLoginServer();
	}
}
