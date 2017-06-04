package game.common.message;

import game.common.net.ClientAgent;
import game.common.net.ClientAgentManager;
import game.common.security.AESUtil;
import game.common.security.RSAPrivateKeyUtil;
import game.common.security.RSAPublicKeyUtil;
import game.server.Server;
import io.netty.channel.ChannelHandlerContext;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 面向客户端消息的处理者
 * Created by wuy on 2017/5/16.
 */
public abstract class ServerForClientMessageHandler extends CloneableMessageHandler {

	final private Server server;
	final private ClientAgentManager clientAgentManager;

	public ServerForClientMessageHandler(Server server, ClientAgentManager clientAgentManager) {
		this.server = server;
		this.clientAgentManager = clientAgentManager;
	}

	public Server getServer() {
		return server;
	}

	public ClientAgentManager getClientAgentManager() {
		return clientAgentManager;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
		getLogger().info("channelRead0:" + ByteUtils.toHexString(bytes));
		ClientAgent clientAgent = clientAgentManager.get(ctx.channel());
		// 解密
		if (clientAgent.getAesKey() == null) {
			bytes = RSAPrivateKeyUtil.decrypt(bytes);
//			getLogger().debug("RSA bytes:" + ByteUtils.toHexString(bytes));
		} else {
			bytes = AESUtil.decrypt(bytes, clientAgent.getAesKey());
//			getLogger().debug("AES bytes:" + ByteUtils.toHexString(bytes));
		}
		// 解包
		Message message = Message.unpack(bytes);
		getLogger().info("message:" + message);
		String invokeMethodName = message.getInvokeMethodName();
		Method method = this.getClass().getMethod(invokeMethodName, ClientAgent.class, Message.class);
		method.invoke(this, clientAgent, message);
	}

	// 连接上时被调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channelActive:" + ctx.channel());
		synchronized (clientAgentManager) {
			clientAgentManager.add(ctx.channel());
		}
		super.channelActive(ctx);
	}

	// 连接断开时被调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channelInactive:" + ctx.channel());
		synchronized (clientAgentManager) {
			clientAgentManager.remove(ctx.channel());
		}
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		getLogger().error("channel error:", cause);
		getLogger().error("disconnect channel:" + ctx.channel());
		// 出错了就断开连接，避免进一步危害
		ctx.disconnect();
	}

	// 发送内容给客户端(调整入参顺序，区别于RPC调用)
	public void send(Message message, ClientAgent clientAgent) throws IOException {
		if (clientAgent.getAesKey() == null) {
			byte[] plainBytes = Message.pack(message);
//			getLogger().debug("RSA plainBytes:" + plainBytes.length + ", " + ByteUtils.toHexString(plainBytes));
			byte[] cipherBytes = RSAPublicKeyUtil.encrypt(plainBytes);
//			getLogger().debug("RSA cipherBytes:" + cipherBytes.length + ", " + ByteUtils.toHexString(cipherBytes));
			clientAgent.getChannel().writeAndFlush(cipherBytes);
		} else {
			byte[] plainBytes = Message.pack(message);
//			getLogger().debug("AES plainBytes:" + plainBytes.length + ", " + ByteUtils.toHexString(plainBytes));
			byte[] cipherBytes = AESUtil.encrypt(plainBytes, clientAgent.getAesKey());
//			getLogger().debug("AES cipherBytes:" + cipherBytes.length + ", " + ByteUtils.toHexString(cipherBytes));
			clientAgent.getChannel().writeAndFlush(cipherBytes);
		}
	}
}
