package game.common.net;

import game.common.message.MessageDecoder;
import game.common.message.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端套接字服务
 * Created by wuy on 2017/5/20.
 */
public class ClientSocketService extends SocketService {

	private EventLoopGroup eventLoopGroup;
	private Bootstrap bootstrap;
	private Channel channel;

	/**
	 * 构造客户端套接字服务
	 */
	public ClientSocketService() {
		super();
	}

	@Override
	public void startService() throws Exception {
		getLogger().info("initializing...");
		eventLoopGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup);
		bootstrap.channel(NioSocketChannel.class);
		// 保持TCP连接
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				ChannelPipeline channelPipeline = socketChannel.pipeline();
				channelPipeline.addLast("decoder", new MessageDecoder(getFrameLength()));
				channelPipeline.addLast("encoder", new MessageEncoder());
				channelPipeline.addLast(getCloneableMessageHandler().clone());
			}
		});
		getLogger().info("initialized.");
		channel = bootstrap.bind(getHost(), getPort()).sync().channel();
	}

	@Override
	public void stopService() {
		eventLoopGroup.shutdownGracefully();
		eventLoopGroup = null;
		bootstrap = null;
		channel = null;
	}

	@Override
	public boolean isAlive() {
		return channel != null;
	}
}