package game.net.message;

import game.net.NetService;
import game.server.Server;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息处理者
 * Created by wuy on 2017/6/22.
 */
public abstract class MessageHandler extends SimpleChannelInboundHandler<byte[]> {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	// 服务器
	private Server server;
	// 网络服务
	private NetService netService;

	public Logger getLogger() {
		return logger;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public NetService getNetService() {
		return netService;
	}

	public void setNetService(NetService netService) {
		this.netService = netService;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("exceptionCaught:", cause);
		logger.error("disconnect channel:" + ctx.channel());
		// 出错了就断开连接，避免进一步危害
		ctx.disconnect();
	}
}
