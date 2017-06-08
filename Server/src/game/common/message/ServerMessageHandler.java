package game.common.message;

import game.common.net.ServerAgent;
import game.server.Server;
import io.netty.channel.ChannelHandlerContext;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 面向服务端的消息处理者
 * Created by wuy on 2017/5/16.
 */
public abstract class ServerMessageHandler extends CloneableMessageHandler {

	public ServerMessageHandler(Server server) {
		super(server);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
		getLogger().info("channelRead0:" + ByteUtils.toHexString(bytes));
		ServerAgent serverAgent = getServerAgentManager().get(ctx.channel());
		// 解包
		Message message = Message.unpack(bytes);
		getLogger().info("message:" + message);
		String destinationServerId = message.getDestinationServerId();
		// 如果目标服务器为空，或者为当前服务器，则处理消息
		if (destinationServerId == null || getServer().getId().equals(destinationServerId)) {
			String invokeMethodName = message.getInvokeMethodName();
			try {
				Method method = this.getClass().getMethod(invokeMethodName, ServerAgent.class, Message.class);
				method.invoke(this, serverAgent, message);
			} catch (Exception e) {
				getLogger().error("method invoke error:", e);
				// 通知登录服务器就是网关服务器，客户端请求消息处理出现异常，断开客户端连接
				Message newMessage = new Message();
				newMessage.setSourceConnectId(message.getSourceConnectId());
				List<String> route = message.getRoute();
				route.add(getServer().getId());
				// 第一个服务器ID，不是登录服务器就是网关服务器
				newMessage.setDestinationServerId(route.get(0));
				newMessage.setInvokeMethodName("disconnectClient");
				send(newMessage, serverAgent);
			}
		} else if (getServerAgentManager().contains(destinationServerId)) {
			// 当前服务器能够找到目标服务器，转发消息
			ServerAgent destinationServerAgent = getServerAgentManager().get(destinationServerId);
			destinationServerAgent.getChannel().writeAndFlush(bytes);
		} else {
			getLogger().error("unknown destinationServerId:" + destinationServerId);
		}
	}

	// 连接上时被调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channel active:" + ctx.channel());
		getServerAgentManager().add(ctx.channel(), false);
		// 双方发起握手
		Message message = new Message();
		List<String> route = new ArrayList<>();
		route.add(getServer().getId());
		message.setRoute(route);
		ServerAgent serverAgent = getServerAgentManager().get(ctx.channel());
		send(message, serverAgent);
		super.channelActive(ctx);
	}

	// 连接断开时被调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channelInactive:" + ctx.channel());
		getServerAgentManager().remove(ctx.channel());
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 确保客户端消息造成的错误不会被抛到此处，此处是服务器之间的异常
		getLogger().error("channel error:", cause);
		getLogger().error("disconnect channel:" + ctx.channel());
		// 出错了就断开连接，避免进一步危害
		ctx.disconnect();
	}

	// 发送内容给服务端(调整入参顺序，区别于RPC调用)
	public void send(Message message, ServerAgent serverAgent) throws IOException {
		serverAgent.getChannel().writeAndFlush(Message.pack(message));
	}

	// 握手，交换服务器信息
	public void shakeHand(ServerAgent serverAgent, Message message) {
		List<String> route = message.getRoute();
		String fromServerId = route.get(route.size() - 1);
		getServerAgentManager().updateServerId(serverAgent, fromServerId);
	}
}
