package game.common.net;

import game.common.message.MessageDecoder;
import game.common.message.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端套接字服务
 * Created by wuy on 2017/5/20.
 */
public class ServerSocketService extends SocketService {

	private EventLoopGroup eventLoopGroup;
	private ServerBootstrap serverBootstrap;
	private Channel channel;

	/**
	 * 构造服务端套接字服务
	 */
	public ServerSocketService() {
		super();
	}

	@Override
	public void startService() throws Exception {
		getLogger().info("initializing...");
		eventLoopGroup = new NioEventLoopGroup();
		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(eventLoopGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		// 保持TCP连接
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				ChannelPipeline channelPipeline = socketChannel.pipeline();
				channelPipeline.addLast("decoder", new MessageDecoder(getFrameLength()));
				channelPipeline.addLast("encoder", new MessageEncoder());
				channelPipeline.addLast(getCloneableMessageHandler().clone());
			}
		});
		getLogger().info("initialized.");
		channel = serverBootstrap.bind(getHost(), getPort()).sync().channel();
	}

	@Override
	public void stopService() {
		eventLoopGroup.shutdownGracefully();
		eventLoopGroup = null;
		serverBootstrap = null;
		channel = null;
	}

	@Override
	public boolean isAlive() {
		return channel != null;
	}
}
