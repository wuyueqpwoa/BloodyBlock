import game.server.exchange.ExchangeServer;

/**
 * 测试交换服务器
 * Created by wuy on 2017/5/21.
 */
public class TestExchangeServer {

	private static void testExchangeServer() throws Exception {
		ExchangeServer exchangeServer = new ExchangeServer();
		exchangeServer.init();
		System.out.println(exchangeServer);
		exchangeServer.start();
	}

	public static void main(String[] args) throws Exception {
		testExchangeServer();
	}
}
