package game.net.agent;

import io.netty.channel.Channel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 代理管理器
 * Created by wuy on 2017/6/22.
 */
public class AgentManager<T extends Agent> {

	// 通道ID为key
	private Map<String, T> agentMap = new LinkedHashMap<>();

	/**
	 * 添加代理
	 *
	 * @param channel    通道
	 * @param agentClass 代理类
	 */
	synchronized public void add(Channel channel, Class agentClass) {
		try {
			T agent = (T) agentClass.newInstance();
			agent.setChannel(channel);
			agentMap.put(channel.id().toString(), agent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得代理
	 *
	 * @param channel 通道
	 * @return 代理
	 */
	synchronized public T get(Channel channel) {
		return agentMap.get(channel.id().toString());
	}

	/**
	 * 获得代理
	 *
	 * @param id 通道ID
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
		return agentMap.remove(channel.id().toString());
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
