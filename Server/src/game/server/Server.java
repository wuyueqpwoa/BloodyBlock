package game.server;

import game.business.BusinessManager;
import game.net.*;
import game.util.ConfigUtil;
import game.net.message.MessageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 服务器，功能全面
 * Created by wuy on 2017/5/16.
 */
public class Server {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	// 服务器类型
	private String type;
	// 服务器ID，格式为"服务器类型_编号"
	private String id;
	// 服务器代理管理器
	private AgentManager<ServerAgent> serverAgentManager = new AgentManager<>();
	// 用户代理管理器
	private AgentManager<UserAgent> userAgentManager = new AgentManager<>();
	// 服务器服务端网络服务Map
	private Map<String, ServerNetService> serverNetServiceMap = new LinkedHashMap<>();
	// 服务器客户端网络服务Map
	private Map<String, ClientNetService> clientNetServiceMap = new LinkedHashMap<>();
	// 用户网络服务Map
	private Map<String, UserNetService> userNetServiceMap = new LinkedHashMap<>();
	// 消息管理器
	private MessageManager messageManager = new MessageManager();
	// 业务管理器
	private BusinessManager businessManager = new BusinessManager();

	public Logger getLogger() {
		return logger;
	}

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
		this.type = id.split("_")[0];
	}

	public AgentManager<ServerAgent> getServerAgentManager() {
		return serverAgentManager;
	}

	public AgentManager<UserAgent> getUserAgentManager() {
		return userAgentManager;
	}

	public Map<String, ServerNetService> getServerNetServiceMap() {
		return serverNetServiceMap;
	}

	public Map<String, ClientNetService> getClientNetServiceMap() {
		return clientNetServiceMap;
	}

	public Map<String, UserNetService> getUserNetServiceMap() {
		return userNetServiceMap;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public BusinessManager getBusinessManager() {
		return businessManager;
	}

	/**
	 * 初始化
	 */
	public void init() throws Exception {
		// 通过配置文件初始化服务器
		ConfigUtil.initServerByConfig(this);
		businessManager.setServer(this);
		businessManager.setMessageManager(messageManager);
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
		// 服务端模式
		for (UserNetService s : userNetServiceMap.values()) {
			s.start();
		}
		businessManager.start();
	}

	@Override
	public String toString() {
		return "Server{" +
				"type='" + type + '\'' +
				", id='" + id + '\'' +
				", serverAgentManager size=" + serverAgentManager.size() +
				", userAgentManager size=" + userAgentManager.size() +
				", serverNetServiceMap size=" + serverNetServiceMap.size() +
				", clientNetServiceMap size=" + clientNetServiceMap.size() +
				", userNetServiceMap size=" + userNetServiceMap.size() +
				", messageManager size=" + messageManager.size() +
				", businessManager size=" + businessManager.size() +
				'}';
	}
}
