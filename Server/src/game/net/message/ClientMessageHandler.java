package game.net.message;

import game.net.ClientNetService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端消息处理者
 * Created by wuy on 2017/6/22.
 */
public class ClientMessageHandler extends SimpleChannelInboundHandler<byte[]> {

	final private Logger logger;
	final private ClientNetService clientNetService;

	public ClientMessageHandler(ClientNetService clientNetService) {
		logger = LoggerFactory.getLogger(this.getClass());
		this.clientNetService = clientNetService;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {

	}
}
