package game.common.message;

import game.common.net.ServerAgent;
import io.netty.channel.ChannelHandlerContext;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.IOException;

/**
 * 面向服务端的消息处理者
 * Created by wuy on 2017/5/16.
 */
public class ServerMessageHandler extends CloneableMessageHandler {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
		getLogger().info("channelRead0:" + ByteUtils.toHexString(bytes));
		// 解包
		Message message = Message.unpack(bytes);
		getMessageQueue().putLast(message);
	}

	// 连接上时被调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channel active:" + ctx.channel());
		getServerAgentManager().add(ctx.channel(), false);
		super.channelActive(ctx);
	}

	// 连接断开时被调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channel i:" + ctx.channel());
		getServerAgentManager().remove(ctx.channel());
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		getLogger().error("channel error:", cause);
		getLogger().error("disconnect channel:" + ctx.channel());
		// 出错了就断开连接，避免进一步危害
		ctx.disconnect();
	}

	// 发送内容给服务端(调整入参顺序，区别于RPC调用)
	public void send(Message message, ServerAgent serverAgent) throws IOException {
		serverAgent.getChannel().writeAndFlush(Message.pack(message));
	}
}
