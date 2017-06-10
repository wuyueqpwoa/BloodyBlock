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
import java.util.Map;
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
		String id;
		// 如果服务器设置了id，则读取相应id的配置，否则默认第一个配置
		if (server.getId() == null) {
			id = (String) config.keySet().iterator().next();
			server.setId(id);
		} else {
			id = server.getId();
		}
		JSONObject config2 = (JSONObject) config.get(id);
		JSONObject config3 = (JSONObject) config2.get("serverSocketService");
		if (config3 != null) {
			for (Object o : config3.entrySet()) {
				Map.Entry e = (Map.Entry) o;
				ServerSocketService serverSocketService = new ServerSocketService();
				serverSocketService.setName((String) e.getKey());
				initSocketService(serverSocketService, server, (JSONObject) e.getValue());
				server.add(serverSocketService);
			}
		}
		config3 = (JSONObject) config2.get("clientSocketService");
		if (config3 != null) {
			for (Object o : config3.entrySet()) {
				Map.Entry e = (Map.Entry) o;
				ClientSocketService clientSocketService = new ClientSocketService();
				clientSocketService.setName((String) e.getKey());
				initSocketService(clientSocketService, server, (JSONObject) e.getValue());
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
		// host和port可以通过读取别的服务器配置获得
		if (config.containsKey("targetService")) {
			String[] targetService = ((String) config.get("targetService")).split("\\.");
			JSONObject config2 = ConfigUtil.getServerProperties(targetService[0]);
			config2 = (JSONObject) config2.get(targetService[1]);
			config2 = (JSONObject) config2.get(targetService[2]);
			config2 = (JSONObject) config2.get(targetService[3]);
			socketService.setHost((String) config2.get("host"));
			socketService.setPort(Integer.parseInt((String) config2.get("port")));
		} else {
			socketService.setHost((String) config.get("host"));
			socketService.setPort(Integer.parseInt((String) config.get("port")));
		}
		// 实例化messageHandler
		String className = (String) config.get("messageHandlerClassName");
		Class<?> messageHandlerClass = Class.forName(className);
		Constructor constructor = messageHandlerClass.getConstructor(Server.class);
		CloneableMessageHandler messageHandler = (CloneableMessageHandler) constructor.newInstance(server);
		socketService.setCloneableMessageHandler(messageHandler);
	}
}
