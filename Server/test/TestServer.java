import game.server.database.DatabaseServer;
import game.server.exchange.ExchangeServer;
import game.server.gateway.GatewayServer;
import game.server.login.LoginServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 测试服务器
 * Created by wuy on 2017/6/28.
 */
public class TestServer {

	public static void testExchangeServer() throws Exception {
		ExchangeServer exchangeServer = new ExchangeServer();
		exchangeServer.init();
		System.out.println(exchangeServer);
		exchangeServer.start();
	}

	public static void testLoginServer() throws Exception {
		LoginServer loginServer = new LoginServer();
		loginServer.init();
		System.out.println(loginServer);
		loginServer.start();
	}

	public static void testDatabaseServer() throws Exception {
		DatabaseServer databaseServer = new DatabaseServer();
		databaseServer.init();
		System.out.println(databaseServer);
		databaseServer.start();
	}

	public static void testGatewayServer() throws Exception {
		GatewayServer gatewayServer = new GatewayServer();
		gatewayServer.init();
		System.out.println(gatewayServer);
		gatewayServer.start();
	}

	public static void main(String[] args) throws Exception {
		for (Method m : TestServer.class.getDeclaredMethods()) {
			if (m.getName().startsWith("test")) {
				new Thread() {
					@Override
					public void run() {
						try {
							m.invoke(null);
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		}
	}
}
