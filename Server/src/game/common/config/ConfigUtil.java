package game.common.config;

import game.common.message.CloneableMessageHandler;
import game.common.net.ClientSocketService;
import game.common.net.ServerSocketService;
import game.common.net.SocketService;
import game.server.Server;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Properties;

/**
 * 配置工具
 * Created by wuy on 2017/6/6.
 */
public class ConfigUtil {

	final private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	private static Properties serverProperties;

	/**
	 * 加载服务器配置
	 *
	 * @return 服务器配置
	 * @throws IOException 配置文件读取异常
	 */
	public static Properties loadServerProperties() throws IOException {
		serverProperties = new Properties();
		serverProperties.load(ConfigUtil.class.getResourceAsStream("/server.properties"));
		return serverProperties;
	}

	/**
	 * 获得服务器配置
	 *
	 * @param key 配置名
	 * @return 配置
	 * @throws IOException    配置文件读取异常
	 * @throws ParseException JSON解析异常
	 */
	public static JSONObject getServerProperties(String key) throws IOException, ParseException {
		if (serverProperties == null) {
			loadServerProperties();
		}
		String jsonString = (String) serverProperties.get(key);
		logger.debug("getProperties(" + key + "):", jsonString);
		return (JSONObject) new JSONParser().parse(jsonString);
	}


	/**
	 * 通过配置文件初始化服务器
	 *
	 * @param server 服务器
	 * @throws Exception 初始化异常
	 */
	public static void initServerByConfig(Server server) throws Exception {
		JSONObject config = ConfigUtil.getServerProperties(server.getClass().getSimpleName());
		server.setType((String) config.get("type"));
		server.setId((String) config.get("id"));
		JSONArray config2 = (JSONArray) config.get("serverSocketService");
		if (config2 != null) {
			for (Object o : config2) {
				JSONObject tempConfig = (JSONObject) o;
				ServerSocketService serverSocketService = new ServerSocketService();
				initSocketService(serverSocketService, server, tempConfig);
				server.add(serverSocketService);
			}
		}
		config2 = (JSONArray) config.get("clientSocketService");
		if (config2 != null) {
			for (Object o : config2) {
				JSONObject tempConfig = (JSONObject) o;
				ClientSocketService clientSocketService = new ClientSocketService();
				initSocketService(clientSocketService, server, tempConfig);
				server.add(clientSocketService);
			}
		}
	}

	/**
	 * 通过配置初始化套接字服务
	 *
	 * @param socketService 套接字服务
	 * @param server        服务器
	 * @param config        配置
	 * @throws Exception 初始化异常
	 */
	private static void initSocketService(SocketService socketService, Server server, JSONObject config) throws Exception {
		socketService.setName((String) config.get("name"));
		socketService.setHost((String) config.get("host"));
		socketService.setPort(Integer.parseInt((String) config.get("host")));
		// 实例化messageHandler
		String className = (String) config.get("messageHandlerClassName");
		Class<?> messageHandlerClass = Class.forName(className);
		Constructor constructor = messageHandlerClass.getConstructor(Server.class);
		CloneableMessageHandler messageHandler = (CloneableMessageHandler) constructor.newInstance(server);
		socketService.setCloneableMessageHandler(messageHandler);
	}
}
