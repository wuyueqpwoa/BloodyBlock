package game.net.message;

import game.net.security.AESUtil;
import game.net.security.RSAPrivateKeyUtil;
import game.net.AgentManager;
import game.net.UserAgent;
import io.netty.channel.ChannelHandlerContext;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 * 用户消息处理者
 * Created by wuy on 2017/6/23.
 */
public class UserMessageHandler extends MessageHandler {

	private AgentManager<UserAgent> userAgentManager;

	public AgentManager<UserAgent> getUserAgentManager() {
		return userAgentManager;
	}

	public void setUserAgentManager(AgentManager<UserAgent> userAgentManager) {
		this.userAgentManager = userAgentManager;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes) throws Exception {
		getLogger().info("channelRead0:" + ByteUtils.toHexString(bytes));
		UserAgent userAgent = userAgentManager.get(ctx.channel());
		// 解密
		if (userAgent.getAesKey() == null) {
			bytes = RSAPrivateKeyUtil.decrypt(bytes);
//			getLogger().debug("RSA bytes:" + ByteUtils.toHexString(bytes));
		} else {
			bytes = AESUtil.decrypt(bytes, userAgent.getAesKey());
//			getLogger().debug("AES bytes:" + ByteUtils.toHexString(bytes));
		}
		// 解包
		Message message = MessagePacker.unpack(bytes);
		getMessageManager().putLast(message);
	}
}
