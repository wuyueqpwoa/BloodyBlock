package game.net.message;

import game.business.Business;
import game.server.UserServer;
import game.util.security.AESUtil;
import game.util.security.RSAPrivateKeyUtil;
import game.net.agent.UserAgent;
import io.netty.channel.ChannelHandlerContext;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.util.Map;

/**
 * 用户消息处理者
 * Created by wuy on 2017/6/23.
 */
public class UserMessageHandler extends MessageHandler {

	@Override
	public UserServer getServer() {
		return (UserServer) super.getServer();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
		getLogger().info("channelRead0:" + ByteUtils.toHexString(bytes));
		UserAgent userAgent = getServer().getUserAgentManager().get(ctx.channel());
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
		// 过滤非法请求
		Map<String, Business> businessMap = getServer().getBusinessManager().getBusinessMap();
		if (!businessMap.containsKey(message.getInvokeMethodName())
				|| businessMap.get(message.getInvokeMethodName()).isServerBusiness()) {
			throw new Exception("illegal message:" + message);
		}
		message.setSourceUserChannelId(ctx.channel().id().toString());
		message.setAgent(userAgent);
		getServer().getMessageManager().putLast(message);
	}

	// 连接上时被调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channelActive:" + ctx.channel());
		getServer().getUserAgentManager().add(ctx.channel(), UserAgent.class);
		super.channelActive(ctx);
	}

	// 连接断开时被调用
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		getLogger().info("channelInactive:" + ctx.channel());
		getServer().getUserAgentManager().remove(ctx.channel());
		super.channelInactive(ctx);
	}
}
