package game.common.message;

import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可克隆的消息处理者
 * Created by wuy on 2017/5/26.
 */
public abstract class CloneableMessageHandler extends SimpleChannelInboundHandler<byte[]> implements Cloneable {
	final private Logger logger;

	public CloneableMessageHandler() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	public Logger getLogger() {
		return logger;
	}

	@Override
	public abstract CloneableMessageHandler clone();
}
