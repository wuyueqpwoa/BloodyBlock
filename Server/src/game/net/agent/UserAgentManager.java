package game.net.agent;

import io.netty.channel.Channel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户代理管理器
 * Created by wuy on 2017/6/28.
 */
public class UserAgentManager extends AgentManager<UserAgent> {

	private Map<String, UserAgent> userAgentMap = new LinkedHashMap<>();

	/**
	 * 添加代理
	 *
	 * @param channel 通道
	 */
	synchronized public void add(Channel channel) {
		super.add(channel, UserAgent.class);
	}
}
