package game.common.net;

import io.netty.channel.Channel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 服务端代理管理器
 * Created by wuy on 2017/5/25.
 */
public class ServerAgentManager extends SocketAgentManager<ServerAgent> {

	// serverId映射到serverAgent
	private Map<String, ServerAgent> serverIdMap = new LinkedHashMap<>();

	public Map<String, ServerAgent> getServerIdMap() {
		return serverIdMap;
	}

	/**
	 * 将通道托管(服务端添加客户端)
	 *
	 * @param channel  通道
	 * @param isServer 是否以服务器模式启动
	 * @return 服务端代理
	 */
	synchronized public ServerAgent add(Channel channel, boolean isServer) {
		return add(new ServerAgent(channel, isServer));
	}

	/**
	 * 更新服务器ID
	 *
	 * @param serverAgent 服务端代理
	 * @param serverId    服务器ID
	 * @return 成功，返回true；失败 ，返回false
	 */
	synchronized public boolean updateServerId(ServerAgent serverAgent, String serverId) {
		if (serverId == null) {
			return false;
		}
		String oldServerId = serverAgent.getServerId();
		if (oldServerId != null) {
			serverIdMap.containsKey(oldServerId);
			serverIdMap.remove(oldServerId);
		}
		serverAgent.setServerId(serverId);
		serverIdMap.put(serverId, serverAgent);
		return true;
	}

	/**
	 * 是否包含套接字代理
	 *
	 * @param serverId 服务器ID
	 * @return 包含，返回true；不包含，返回false
	 */
	synchronized public boolean contains(String serverId) {
		return serverIdMap.containsKey(serverId);
	}

	/**
	 * 获得套接字代理
	 *
	 * @param serverId 服务器ID
	 * @return 套接字代理
	 */
	synchronized public ServerAgent get(String serverId) {
		return serverIdMap.get(serverId);
	}

	/**
	 * 将通道取消托管
	 *
	 * @param channel 通道
	 * @return 成功，返回true；失败 ，返回false
	 */
	synchronized public ServerAgent remove(Channel channel) {
		if (!contains(channel)) {
			return null;
		}
		ServerAgent serverAgent = super.remove(channel);
		String serverId = serverAgent.getServerId();
		if (serverId != null) {
			serverIdMap.containsKey(serverId);
			serverIdMap.remove(serverId);
		}
		return serverAgent;
	}

	/**
	 * 将通道取消托管
	 *
	 * @param connectId 连接ID
	 * @return 成功，返回true；失败 ，返回false
	 */
	synchronized public ServerAgent remove(Integer connectId) {
		if (!contains(connectId)) {
			return null;
		}
		ServerAgent serverAgent = super.remove(connectId);
		String serverId = serverAgent.getServerId();
		if (serverId != null) {
			serverIdMap.containsKey(serverId);
			serverIdMap.remove(serverId);
		}
		return serverAgent;
	}

	/**
	 * 将通道取消托管
	 *
	 * @param serverId 服务器ID
	 * @return 成功，返回true；失败 ，返回false
	 */
	synchronized public ServerAgent remove(String serverId) {
		if (!serverIdMap.containsKey(serverId)) {
			return null;
		}
		ServerAgent serverAgent = serverIdMap.remove(serverId);
		super.remove(serverAgent.getChannel());
		return serverAgent;
	}
}
