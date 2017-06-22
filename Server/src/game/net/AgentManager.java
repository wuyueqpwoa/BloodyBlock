package game.net;

import io.netty.channel.Channel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 代理管理器
 * Created by wuy on 2017/6/22.
 */
public class AgentManager {

	private Map<String, Agent> agentMap = new LinkedHashMap<>();

	/**
	 * 添加代理
	 *
	 * @param channel 通道
	 */
	synchronized public Agent add(Channel channel) {
		Agent agent = new Agent(channel);
		agentMap.put(channel.id().asShortText(), agent);
		return agent;
	}

	/**
	 * 获得代理
	 *
	 * @param channel 通道
	 * @return 代理
	 */
	synchronized public Agent get(Channel channel) {
		return agentMap.get(channel.id().asShortText());
	}

	/**
	 * 获得代理
	 *
	 * @param id ID
	 * @return 代理
	 */
	synchronized public Agent get(String id) {
		return agentMap.get(id);
	}

	/**
	 * 移除代理
	 *
	 * @param channel 通道
	 * @return 代理
	 */
	synchronized public Agent remove(Channel channel) {
		return agentMap.remove(channel.id().asShortText());
	}

	/**
	 * 移除代理
	 *
	 * @param id ID
	 * @return 代理
	 */
	synchronized public Agent remove(String id) {
		return agentMap.remove(id);
	}
}
