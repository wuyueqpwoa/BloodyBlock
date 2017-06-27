package game.net;

import game.net.message.MessageDecoder;
import game.net.message.MessageEncoder;
import game.net.message.ServerMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务器服务端网络服务，向其它服务器提供服务
 * Created by wuy on 2017/5/20.
 */
public class ServerNetService extends NetService {

	private EventLoopGroup eventLoopGroup;
	private ServerBootstrap serverBootstrap;
	private Channel channel;

	@Override
	public void startService() throws Exception {
		getLogger().info("initializing...");
		eventLoopGroup = new NioEventLoopGroup();
		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(eventLoopGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		// 保持TCP连接
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		ServerNetService context = this;
		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				ChannelPipeline channelPipeline = socketChannel.pipeline();
				channelPipeline.addLast("decoder", new MessageDecoder(getFrameLength()));
				channelPipeline.addLast("encoder", new MessageEncoder());
				ServerMessageHandler serverMessageHandler = new ServerMessageHandler();
				serverMessageHandler.setServer(context.getServer());
				serverMessageHandler.setNetService(context);
				channelPipeline.addLast(serverMessageHandler);
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
