package game.common.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.util.Base64;

/**
 * 消息解码器
 * Created by wuy on 2017/5/16.
 */
public class MessageDecoder extends LineBasedFrameDecoder {

	public MessageDecoder(int maxLength) {
		super(maxLength);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
		if (frame == null) {
			return null;
		}
		byte[] bytes = new byte[frame.readableBytes()];
		frame.readBytes(bytes);
		return Base64.getDecoder().decode(bytes);
	}
}
