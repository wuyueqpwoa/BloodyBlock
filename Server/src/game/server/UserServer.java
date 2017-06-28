package game.server;

import game.net.UserNetServiceManager;
import game.net.agent.AgentManager;
import game.net.agent.UserAgent;
import game.net.agent.UserAgentManager;

/**
 * 面向用户的服务器
 * Created by wuy on 2017/6/28.
 */
public class UserServer extends Server {

	// 用户代理管理器
	private UserAgentManager userAgentManager = new UserAgentManager();
	// 用户网络服务管理器
	private UserNetServiceManager userNetServiceManager = new UserNetServiceManager();

	public AgentManager<UserAgent> getUserAgentManager() {
		return userAgentManager;
	}

	public UserNetServiceManager getUserNetServiceManager() {
		return userNetServiceManager;
	}

	/**
	 * 启动
	 */
	public void start() throws Exception {
		getServerNetServiceManager().start();
		userNetServiceManager.start();
		getBusinessManager().start();
	}
}
