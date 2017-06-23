package game.net;

import io.netty.channel.Channel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 代理管理器
 * Created by wuy on 2017/6/22.
 */
public class AgentManager<T extends Agent> {

	private Map<String, T> agentMap = new LinkedHashMap<>();

	/**
	 * 添加代理
	 *
	 * @param channel    通道
	 * @param agentClass 代理类
	 */
	synchronized public T add(Channel channel, Class agentClass) {
		try {
			T agent = (T) agentClass.newInstance();
			agent.setChannel(channel);
			agentMap.put(channel.id().asShortText(), agent);
			return agent;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得代理
	 *
	 * @param channel 通道
	 * @return 代理
	 */
	synchronized public T get(Channel channel) {
		return agentMap.get(channel.id().asShortText());
	}

	/**
	 * 获得代理
	 *
	 * @param id ID
	 * @return 代理
	 */
	synchronized public T get(String id) {
		return agentMap.get(id);
	}

	/**
	 * 移除代理
	 *
	 * @param channel 通道
	 * @return 代理
	 */
	synchronized public T remove(Channel channel) {
		return agentMap.remove(channel.id().asShortText());
	}

	/**
	 * 移除代理
	 *
	 * @param id ID
	 * @return 代理
	 */
	synchronized public T remove(String id) {
		return agentMap.remove(id);
	}

	/**
	 * 获得管理中的代理数量
	 *
	 * @return 管理中的代理数量
	 */
	synchronized public int size() {
		return agentMap.size();
	}
}
