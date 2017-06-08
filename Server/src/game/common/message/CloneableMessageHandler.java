package game.common.message;

import game.common.net.ServerAgentManager;
import game.common.net.UserAgentManager;
import game.server.Server;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可克隆的消息处理者
 * Created by wuy on 2017/5/26.
 */
public abstract class CloneableMessageHandler extends SimpleChannelInboundHandler<byte[]> implements Cloneable {

	final private Logger logger;
	final private Server server;

	public CloneableMessageHandler(Server server) {
		logger = LoggerFactory.getLogger(this.getClass());
		this.server = server;
	}

	public Logger getLogger() {
		return logger;
	}

	public Server getServer() {
		return server;
	}

	@Override
	public abstract CloneableMessageHandler clone();

	public UserAgentManager getUserAgentManager() {
		return server.getUserAgentManager();
	}

	public ServerAgentManager getServerAgentManager() {
		return server.getServerAgentManager();
	}
}
