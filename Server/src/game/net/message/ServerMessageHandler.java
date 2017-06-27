package game.net.message;

import game.net.Agent;
import game.net.ServerAgent;
import io.netty.channel.ChannelHandlerContext;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务器服务端消息处理者
 * Created by wuy on 2017/6/23.
 */
public class ServerMessageHandler extends MessageHandler {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
		getLogger().info("channelRead0:" + ByteUtils.toHexString(bytes));
		// 解包
		Message message = Message.unpack(bytes);
		message.setAgent(getServer().getServerAgentManager().get(ctx.channel()));
		getServer().getMessageManager().putLast(message);
	}

	// 连接上时被调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channelActive:" + ctx.channel());
		getServer().getServerAgentManager().add(ctx.channel(), ServerAgent.class);
		// 客户端上线
		Message message = new Message();
		message.setSourceServerId(getServer().getId());
		message.setInvokeMethodName("serverOnline");
		ServerAgent serverAgent = getServer().getServerAgentManager().get(ctx.channel());
		serverAgent.writeAndFlush(message);
		super.channelActive(ctx);
	}

	// 连接断开时被调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channelInactive:" + ctx.channel());
		getServer().getServerAgentManager().remove(ctx.channel());
		super.channelInactive(ctx);
	}
}
