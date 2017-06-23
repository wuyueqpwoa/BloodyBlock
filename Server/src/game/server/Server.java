package game.server;

import game.business.BusinessService;
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
	// 服务器服务端网络服务Map
	private Map<String, ServerNetService> serverNetServiceMap = new LinkedHashMap<>();
	// 服务器客户端网络服务Map
	private Map<String, ClientNetService> clientNetServiceMap = new LinkedHashMap<>();
	// 服务器代理管理器
	private AgentManager<ServerAgent> serverAgentManager = new AgentManager<>();
	// 用户代理管理器
	private AgentManager<UserAgent> userAgentManager = new AgentManager<>();
	// 消息管理器
	private MessageManager messageManager = new MessageManager();
	// 业务服务
	private BusinessService businessService = new BusinessService();

	public Server() {
		businessService.setServer(this);
	}

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

	public Map<String, ServerNetService> getServerNetServiceMap() {
		return serverNetServiceMap;
	}

	public void setServerNetServiceMap(Map<String, ServerNetService> serverNetServiceMap) {
		this.serverNetServiceMap = serverNetServiceMap;
	}

	public Map<String, ClientNetService> getClientNetServiceMap() {
		return clientNetServiceMap;
	}

	public void setClientNetServiceMap(Map<String, ClientNetService> clientNetServiceMap) {
		this.clientNetServiceMap = clientNetServiceMap;
	}

	public AgentManager<ServerAgent> getServerAgentManager() {
		return serverAgentManager;
	}

	public void setServerAgentManager(AgentManager<ServerAgent> serverAgentManager) {
		this.serverAgentManager = serverAgentManager;
	}

	public AgentManager<UserAgent> getUserAgentManager() {
		return userAgentManager;
	}

	public void setUserAgentManager(AgentManager<UserAgent> userAgentManager) {
		this.userAgentManager = userAgentManager;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public BusinessService getBusinessService() {
		return businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
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
	 * @param serverNetService 服务端套接字服务
	 */
	public void add(ServerNetService serverNetService) {
		serverNetServiceMap.put(serverNetService.getName(), serverNetService);
	}

	/**
	 * 客户端套接字服务
	 *
	 * @param clientNetService 客户端套接字服务
	 */
	public void add(ClientNetService clientNetService) {
		clientNetServiceMap.put(clientNetService.getName(), clientNetService);
	}

	@Override
	public String toString() {
		return "Server{" +
				"type='" + type + '\'' +
				", id='" + id + '\'' +
				", serverNetServiceMap size=" + serverNetServiceMap.size() +
				", clientNetServiceMap size=" + clientNetServiceMap.size() +
				", serverAgentManager size=" + serverAgentManager.size() +
				", userAgentManager size=" + userAgentManager.size() +
				'}';
	}
}
