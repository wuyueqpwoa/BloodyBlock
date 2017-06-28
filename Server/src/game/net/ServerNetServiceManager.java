package game.net;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 服务器网络服务管理器，包括客户端和服务端
 * Created by wuy on 2017/6/28.
 */
public class ServerNetServiceManager {

	// 服务器服务端网络服务Map
	private Map<String, ServerNetService> serverNetServiceMap = new LinkedHashMap<>();
	// 服务器客户端网络服务Map
	private Map<String, ClientNetService> clientNetServiceMap = new LinkedHashMap<>();

	public Map<String, ServerNetService> getServerNetServiceMap() {
		return serverNetServiceMap;
	}

	public Map<String, ClientNetService> getClientNetServiceMap() {
		return clientNetServiceMap;
	}

	/**
	 * 启动
	 */
	public void start() throws Exception {
		// 服务端模式
		for (ServerNetService s : serverNetServiceMap.values()) {
			s.start();
		}
		// 客户端模式，一直等待服务器上线
		for (ClientNetService s : clientNetServiceMap.values()) {
			s.start();
		}
	}
}
