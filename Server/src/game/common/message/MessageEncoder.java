package game.common.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Base64;

/**
 * 消息编码器
 * Created by wuy on 2017/5/16.
 */
public class MessageEncoder extends MessageToByteEncoder<byte[]> {

	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, byte[] bytes, ByteBuf byteBuf) throws Exception {
		byteBuf.writeBytes(Base64.getEncoder().encode(bytes));
		byteBuf.writeByte('\n');
	}
}
