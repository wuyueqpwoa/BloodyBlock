package game.net.agent;

import io.netty.channel.Channel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 服务器代理管理器
 * Created by wuy on 2017/6/28.
 */
public class ServerAgentManager extends AgentManager<ServerAgent> {

	// 服务器ID为key
	private Map<String, ServerAgent> serverAgentMap = new LinkedHashMap<>();

	/**
	 * 更新ID(当ServerAgent修改了id时调用)
	 *
	 * @param channel 通道
	 */
	synchronized public void updateId(Channel channel) {
		ServerAgent serverAgent = super.get(channel);
		serverAgentMap.put(serverAgent.getId(), serverAgent);
	}

	/**
	 * 添加代理
	 *
	 * @param channel 通道
	 */
	synchronized public void add(Channel channel) {
		super.add(channel, ServerAgent.class);
	}

	/**
	 * 获得代理
	 *
	 * @param id 服务器ID
	 * @return 代理
	 */
	synchronized public ServerAgent getByServerId(String id) {
		return serverAgentMap.get(id);
	}

	/**
	 * 移除代理
	 *
	 * @param channel 通道
	 * @return 代理
	 */
	synchronized public ServerAgent remove(Channel channel) {
		ServerAgent serverAgent = super.remove(channel);
		if (serverAgentMap.containsKey(serverAgent.getId())) {
			serverAgentMap.remove(serverAgent.getId());
		}
		return serverAgent;
	}
}
