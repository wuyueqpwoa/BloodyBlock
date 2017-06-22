package game.common.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 套接字代理管理器
 * Created by wuy on 2017/5/25.
 */
public class SocketAgentManager<T extends SocketAgent> {

	// channelId映射到socketAgent
	private Map<ChannelId, T> channelIdMap = new LinkedHashMap<>();
	// connectId映射到socketAgent
	private Map<Integer, T> connectIdMap = new LinkedHashMap<>();

	public Map<ChannelId, T> getChannelIdMap() {
		return channelIdMap;
	}

	public Map<Integer, T> getConnectIdMap() {
		return connectIdMap;
	}

	/**
	 * 将套接字代理托管
	 *
	 * @param socketAgent 套接字代理
	 * @return 套接字代理
	 */
	synchronized public T add(T socketAgent) {
		// 清除旧关系
		remove(socketAgent.getChannel());
		channelIdMap.put(socketAgent.getChannel().id(), socketAgent);
		connectIdMap.put(socketAgent.getConnectId(), socketAgent);
		return socketAgent;
	}

	/**
	 * 是否包含套接字代理
	 *
	 * @param channel 通道
	 * @return 包含，返回true；不包含，返回false
	 */
	synchronized public boolean contains(Channel channel) {
		return channelIdMap.containsKey(channel.id());
	}

	/**
	 * 是否包含套接字代理
	 *
	 * @param connectId 连接ID
	 * @return 包含，返回true；不包含，返回false
	 */
	synchronized public boolean contains(Integer connectId) {
		return connectIdMap.containsKey(connectId);
	}

	/**
	 * 获得套接字代理
	 *
	 * @param channel 通道
	 * @return 套接字代理
	 */
	synchronized public T get(Channel channel) {
		return channelIdMap.get(channel.id());
	}

	/**
	 * 获得套接字代理
	 *
	 * @param connectId 连接ID
	 * @return 套接字代理
	 */
	synchronized public T get(Integer connectId) {
		return connectIdMap.get(connectId);
	}

	/**
	 * 将通道取消托管
	 *
	 * @param channel 通道
	 * @return 成功，返回true；失败 ，返回false
	 */
	synchronized public T remove(Channel channel) {
		if (!channelIdMap.containsKey(channel.id())) {
			return null;
		}
		T socketAgent = channelIdMap.remove(channel.id());
		connectIdMap.remove(socketAgent.getConnectId());
		return socketAgent;
	}

	/**
	 * 将通道取消托管
	 *
	 * @param connectId 连接ID
	 * @return 成功，返回true；失败 ，返回false
	 */
	synchronized public T remove(Integer connectId) {
		if (!connectIdMap.containsKey(connectId)) {
			return null;
		}
		T socketAgent = connectIdMap.remove(connectId);
		channelIdMap.remove(socketAgent.getChannel().id());
		return socketAgent;
	}

	/**
	 * 获得套接字代理数量
	 *
	 * @return 套接字代理数量
	 */
	synchronized public int size() {
		return connectIdMap.size();
	}
}
