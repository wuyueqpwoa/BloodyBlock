package game.net;

import game.net.message.MessageDecoder;
import game.net.message.MessageEncoder;
import game.net.message.UserMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 用户网络服务，向用户提供服务
 * Created by wuy on 2017/5/20.
 */
public class UserNetService extends NetService {

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
		UserNetService context = this;
		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				ChannelPipeline channelPipeline = socketChannel.pipeline();
				channelPipeline.addLast("decoder", new MessageDecoder(getFrameLength()));
				channelPipeline.addLast("encoder", new MessageEncoder());
				UserMessageHandler userMessageHandler = new UserMessageHandler();
				userMessageHandler.setAgentManager(context.getServer().getServerAgentManager());
				userMessageHandler.setMessageManager(context.getServer().getMessageManager());
				userMessageHandler.setUserAgentManager(context.getServer().getUserAgentManager());
				channelPipeline.addLast(userMessageHandler);
			}
		});
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
