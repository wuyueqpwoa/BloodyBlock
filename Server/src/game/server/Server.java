package game.server;

import game.common.config.ConfigUtil;
import game.common.net.ClientSocketService;
import game.common.net.ServerAgentManager;
import game.common.net.ServerSocketService;
import game.common.net.UserAgentManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 服务器，功能全面
 * Created by wuy on 2017/5/16.
 */
public class Server {

	// 服务器类型
	private String type = "Default";
	// 服务器ID
	private String id = "Default_01";
	// 服务端套接字服务Map
	private Map<String, ServerSocketService> serverSocketServiceMap = new LinkedHashMap<>();
	// 客户端套接字服务Map
	private Map<String, ClientSocketService> clientSocketServiceMap = new LinkedHashMap<>();
	// 用户代理管理器
	private UserAgentManager userAgentManager = new UserAgentManager();
	// 服务端代理管理器
	private ServerAgentManager serverAgentManager = new ServerAgentManager();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, ServerSocketService> getServerSocketServiceMap() {
		return serverSocketServiceMap;
	}

	public void setServerSocketServiceMap(Map<String, ServerSocketService> serverSocketServiceMap) {
		this.serverSocketServiceMap = serverSocketServiceMap;
	}

	public Map<String, ClientSocketService> getClientSocketServiceMap() {
		return clientSocketServiceMap;
	}

	public void setClientSocketServiceMap(Map<String, ClientSocketService> clientSocketServiceMap) {
		this.clientSocketServiceMap = clientSocketServiceMap;
	}

	public UserAgentManager getUserAgentManager() {
		return userAgentManager;
	}

	public void setUserAgentManager(UserAgentManager userAgentManager) {
		this.userAgentManager = userAgentManager;
	}

	public ServerAgentManager getServerAgentManager() {
		return serverAgentManager;
	}

	public void setServerAgentManager(ServerAgentManager serverAgentManager) {
		this.serverAgentManager = serverAgentManager;
	}

	/**
	 * 初始化
	 */
	public void init() throws Exception {
		ConfigUtil.initServerByConfig(this);
	}

	/**
	 * 添加服务端套接字服务
	 *
	 * @param serverSocketService 服务端套接字服务
	 */
	public void add(ServerSocketService serverSocketService) {
		serverSocketServiceMap.put(serverSocketService.getName(), serverSocketService);
	}

	/**
	 * 客户端套接字服务
	 *
	 * @param clientSocketService 客户端套接字服务
	 */
	public void add(ClientSocketService clientSocketService) {
		clientSocketServiceMap.put(clientSocketService.getName(), clientSocketService);
	}
}
