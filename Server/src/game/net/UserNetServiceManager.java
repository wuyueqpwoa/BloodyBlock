package game.net;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户网络服务管理器
 * Created by wuy on 2017/6/28.
 */
public class UserNetServiceManager {

	// 用户网络服务Map
	private Map<String, UserNetService> userNetServiceMap = new LinkedHashMap<>();

	public Map<String, UserNetService> getUserNetServiceMap() {
		return userNetServiceMap;
	}

	/**
	 * 启动
	 */
	public void start() throws Exception {
		for (UserNetService s : userNetServiceMap.values()) {
			s.start();
		}
	}
}
