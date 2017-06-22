package game.net;

import game.common.message.MessageDecoder;
import game.common.message.MessageEncoder;
import game.net.message.ClientMessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端网络服务
 * Created by wuy on 2017/5/20.
 */
public class ClientNetService extends NetService {

	private EventLoopGroup eventLoopGroup;
	private Bootstrap bootstrap;
	private Channel channel;

	@Override
	public void startService() throws Exception {
		getLogger().info("initializing...");
		eventLoopGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup);
		bootstrap.channel(NioSocketChannel.class);
		// 保持TCP连接
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		ClientNetService context = this;
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				ChannelPipeline channelPipeline = socketChannel.pipeline();
				channelPipeline.addLast("decoder", new MessageDecoder(getFrameLength()));
				channelPipeline.addLast("encoder", new MessageEncoder());
				channelPipeline.addLast(new ClientMessageHandler(context));
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
