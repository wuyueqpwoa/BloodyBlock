package game.util;

import game.business.Business;
import game.net.ClientNetService;
import game.net.NetService;
import game.net.ServerNetService;
import game.net.UserNetService;
import game.server.Server;
import game.server.UserServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 配置工具
 * Created by wuy on 2017/6/6.
 */
public class ConfigUtil {

	final private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	final private static Properties serverProperties = new Properties();

	static {
		try {
			loadServerProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加载服务器配置
	 *
	 * @return 服务器配置
	 * @throws IOException 配置文件读取异常
	 */
	public static Properties loadServerProperties() throws IOException {
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
		String jsonString = (String) serverProperties.get(key);
		logger.debug("getProperties(" + key + "):" + jsonString);
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
		JSONObject config3 = (JSONObject) config2.get("ServerNetService");
		if (config3 != null) {
			for (Object o : config3.entrySet()) {
				Map.Entry e = (Map.Entry) o;
				ServerNetService serverNetService = new ServerNetService();
				serverNetService.setName((String) e.getKey());
				// 初始化其它配置
				initNetService(serverNetService, server, (JSONObject) e.getValue());
				server.getServerNetServiceManager()
						.getServerNetServiceMap().put(serverNetService.getName(), serverNetService);
			}
		}
		config3 = (JSONObject) config2.get("ClientNetService");
		if (config3 != null) {
			for (Object o : config3.entrySet()) {
				Map.Entry e = (Map.Entry) o;
				ClientNetService clientNetService = new ClientNetService();
				clientNetService.setName((String) e.getKey());
				// 初始化其它配置
				initNetService(clientNetService, server, (JSONObject) e.getValue());
				server.getServerNetServiceManager()
						.getClientNetServiceMap().put(clientNetService.getName(), clientNetService);
			}
		}
		config3 = (JSONObject) config2.get("UserNetService");
		if (config3 != null) {
			for (Object o : config3.entrySet()) {
				Map.Entry e = (Map.Entry) o;
				UserNetService userNetService = new UserNetService();
				userNetService.setName((String) e.getKey());
				// 初始化其它配置
				initNetService(userNetService, server, (JSONObject) e.getValue());
				((UserServer) server).getUserNetServiceManager()
						.getUserNetServiceMap().put(userNetService.getName(), userNetService);
			}
		}
		config3 = (JSONObject) config2.get("ServerBusiness");
		if (config3 != null) {
			for (Object o : config3.entrySet()) {
				Map.Entry e = (Map.Entry) o;
				// 初始化其它配置
				initBusiness(server, (String) e.getKey(), (String) e.getValue(), true);
			}
		}
		config3 = (JSONObject) config2.get("UserBusiness");
		if (config3 != null) {
			for (Object o : config3.entrySet()) {
				Map.Entry e = (Map.Entry) o;
				// 初始化其它配置
				initBusiness(server, (String) e.getKey(), (String) e.getValue(), false);
			}
		}
	}

	/**
	 * 通过配置初始化网络服务
	 *
	 * @param netService 网络服务
	 * @param server     服务器
	 * @param config     配置
	 * @throws Exception 初始化异常
	 */
	private static void initNetService(NetService netService, Server server, JSONObject config) throws Exception {
		// host和port可以通过读取别的服务器配置获得
		if (config.containsKey("targetService")) {
			String[] targetService = ((String) config.get("targetService")).split("\\.");
			JSONObject config2 = ConfigUtil.getServerProperties(targetService[0]);
			config2 = (JSONObject) config2.get(targetService[1]);
			config2 = (JSONObject) config2.get(targetService[2]);
			config2 = (JSONObject) config2.get(targetService[3]);
			netService.setHost((String) config2.get("host"));
			netService.setPort(Integer.parseInt((String) config2.get("port")));
		} else {
			netService.setHost((String) config.get("host"));
			netService.setPort(Integer.parseInt((String) config.get("port")));
		}
		// 关联服务器和服务
		netService.setServer(server);
	}

	/**
	 * 通过配置初始化业务
	 *
	 * @param server           服务器
	 * @param name             业务名
	 * @param className        类名
	 * @param isServerBusiness 是否服务器业务
	 * @throws Exception 初始化异常
	 */
	private static void initBusiness(Server server, String name, String className, boolean isServerBusiness) throws Exception {
		if ("".equals(className)) {
			// 从名字找到对应的业务类，Server所在包名+业务类名
			className = server.getClass().getPackage().getName() + "." + name;
		}
		logger.debug("className:" + className);
		Business business = (Business) Class.forName(className).newInstance();
		business.setServer(server);
		business.setServerBusiness(isServerBusiness);
		// 关联服务器和服务
		server.getBusinessManager().add(business);
	}
}
