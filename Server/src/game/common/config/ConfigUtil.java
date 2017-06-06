package game.common.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
}
