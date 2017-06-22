package game.net.message;

import game.net.AgentManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息处理者
 * Created by wuy on 2017/6/22.
 */
public class MessageHandler extends SimpleChannelInboundHandler<byte[]> {

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	private AgentManager agentManager;
	private MessageManager messageManager;

	public AgentManager getAgentManager() {
		return agentManager;
	}

	public void setAgentManager(AgentManager agentManager) {
		this.agentManager = agentManager;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
		logger.info("channelRead0:" + ByteUtils.toHexString(bytes));
		// 解包
		Message message = MessagePacker.unpack(bytes);
		messageManager.putLast(message);
	}

	// 连接上时被调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelActive:" + ctx.channel());
		agentManager.add(ctx.channel());
		super.channelActive(ctx);
	}

	// 连接断开时被调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelInactive:" + ctx.channel());
		agentManager.remove(ctx.channel());
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("exceptionCaught:", cause);
		logger.error("disconnect channel:" + ctx.channel());
		// 出错了就断开连接，避免进一步危害
		ctx.disconnect();
	}
}
