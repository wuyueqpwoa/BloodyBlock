package game.net.message;

import game.common.message.MessageQueue;
import game.common.net.ServerAgentManager;
import game.common.net.SocketService;
import game.common.net.UserAgentManager;
import game.server.Server;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可克隆的消息处理者
 * Created by wuy on 2017/6/22.
 */
public abstract class CloneableMessageHandler extends SimpleChannelInboundHandler<byte[]> implements Cloneable {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	private SocketService socketService;

	public Logger getLogger() {
		return logger;
	}

	public SocketService getSocketService() {
		return socketService;
	}

	public void setSocketService(SocketService socketService) {
		this.socketService = socketService;
	}

	public Server getServer() {
		return socketService.getServer();
	}

	public MessageQueue getMessageQueue() {
		return getServer().getMessageQueue();
	}

	public ServerAgentManager getServerAgentManager() {
		return getServer().getServerAgentManager();
	}

	public UserAgentManager getUserAgentManager() {
		return getServer().getUserAgentManager();
	}

	@Override
	public CloneableMessageHandler clone() {
		try {
			CloneableMessageHandler messageHandler = this.getClass().newInstance();
			messageHandler.setSocketService(socketService);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
