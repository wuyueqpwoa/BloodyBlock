package game.net.message;

import game.net.ClientNetService;
import game.net.agent.ServerAgent;
import io.netty.channel.ChannelHandlerContext;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 * 服务器客户端消息处理者
 * Created by wuy on 2017/6/23.
 */
public class ClientMessageHandler extends MessageHandler {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
		getLogger().info("channelRead0:" + ByteUtils.toHexString(bytes));
		// 解包
		Message message = new Message().unpack(bytes);
		message.setAgent(getServer().getServerAgentManager().get(ctx.channel()));
		getServer().getMessageManager().putLast(message);
	}

	// 连接上时被调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channelActive:" + ctx.channel());
		getServer().getServerAgentManager().add(ctx.channel(), ServerAgent.class);
		// 服务端上线
		Message message = new Message();
		message.setSourceServerId(getServer().getId());
		message.setInvokeMethodName("clientOnline");
		ServerAgent serverAgent = getServer().getServerAgentManager().get(ctx.channel());
		serverAgent.writeAndFlush(message);
		serverAgent.setServer(true);
		super.channelActive(ctx);
	}

	// 连接断开时被调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channelInactive:" + ctx.channel());
		getServer().getServerAgentManager().remove(ctx.channel());
		super.channelInactive(ctx);
		// 重连
		((ClientNetService) getNetService()).reConnect();
	}
}
