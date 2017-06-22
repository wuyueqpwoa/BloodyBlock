package game.common.message;

import game.common.net.UserAgent;
import game.common.security.AESUtil;
import game.common.security.RSAPrivateKeyUtil;
import game.common.security.RSAPublicKeyUtil;
import io.netty.channel.ChannelHandlerContext;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.IOException;

/**
 * 面向用户端的消息处理者
 * Created by wuy on 2017/5/16.
 */
public class UserMessageHandler extends CloneableMessageHandler {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
		getLogger().info("channelRead0:" + ByteUtils.toHexString(bytes));
		UserAgent userAgent = getUserAgentManager().get(ctx.channel());
		// 解密
		if (userAgent.getAesKey() == null) {
			bytes = RSAPrivateKeyUtil.decrypt(bytes);
//			getLogger().debug("RSA bytes:" + ByteUtils.toHexString(bytes));
		} else {
			bytes = AESUtil.decrypt(bytes, userAgent.getAesKey());
//			getLogger().debug("AES bytes:" + ByteUtils.toHexString(bytes));
		}
		// 解包
		Message message = Message.unpack(bytes);
		getMessageQueue().putLast(message);
//		getLogger().info("message:" + message);
//		String invokeMethodName = message.getInvokeMethodName();
//		Method method = this.getClass().getMethod(invokeMethodName, UserAgent.class, Message.class);
//		method.invoke(this, userAgent, message);
	}

	// 连接上时被调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channel active:" + ctx.channel());
		getUserAgentManager().add(ctx.channel());
		super.channelActive(ctx);
	}

	// 连接断开时被调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channel inactive:" + ctx.channel());
		getUserAgentManager().remove(ctx.channel());
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
	public void send(Message message, UserAgent userAgent) throws IOException {
		if (userAgent.getAesKey() == null) {
			byte[] plainBytes = Message.pack(message);
//			getLogger().debug("RSA plainBytes:" + plainBytes.length + ", " + ByteUtils.toHexString(plainBytes));
			byte[] cipherBytes = RSAPublicKeyUtil.encrypt(plainBytes);
//			getLogger().debug("RSA cipherBytes:" + cipherBytes.length + ", " + ByteUtils.toHexString(cipherBytes));
			userAgent.getChannel().writeAndFlush(cipherBytes);
		} else {
			byte[] plainBytes = Message.pack(message);
//			getLogger().debug("AES plainBytes:" + plainBytes.length + ", " + ByteUtils.toHexString(plainBytes));
			byte[] cipherBytes = AESUtil.encrypt(plainBytes, userAgent.getAesKey());
//			getLogger().debug("AES cipherBytes:" + cipherBytes.length + ", " + ByteUtils.toHexString(cipherBytes));
			userAgent.getChannel().writeAndFlush(cipherBytes);
		}
	}
}
