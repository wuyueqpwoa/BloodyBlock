package game.common.net;

import game.common.message.CloneableMessageHandler;
import game.common.message.MessageDecoder;
import game.common.message.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器套接字服务
 * Created by wuy on 2017/5/20.
 */
public class ServerSocketService {

	// 最大帧长度10Mb=1310720B
	final private static int MAX_FRAME_LENGTH = 1024 * 1024 * 10;
	private Logger logger;
	private String host;
	private int port;
	private EventLoopGroup eventLoopGroup;
	private ServerBootstrap serverBootstrap;
	private Channel channel;
	private CloneableMessageHandler cloneableMessageHandler;

	/**
	 * 构造服务器套接字服务
	 *
	 * @param cloneableMessageHandler 可克隆的消息处理者
	 */
	public ServerSocketService(CloneableMessageHandler cloneableMessageHandler) {
		logger = LoggerFactory.getLogger(this.getClass());
		this.cloneableMessageHandler = cloneableMessageHandler;
	}

	/**
	 * 获得通道初始化器
	 *
	 * @return 通道初始化器
	 */
	public ChannelInitializer<SocketChannel> getChannelInitializer() {
		return new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				ChannelPipeline channelPipeline = socketChannel.pipeline();
				channelPipeline.addLast("decoder", new MessageDecoder(MAX_FRAME_LENGTH));
				channelPipeline.addLast("encoder", new MessageEncoder());
				channelPipeline.addLast(cloneableMessageHandler.clone());
			}
		};
	}

	/**
	 * 以服务端方式启动
	 *
	 * @param host 地址
	 * @param port 端口
	 * @throws InterruptedException
	 */
	public void start(String host, int port) throws InterruptedException {
		if (isAlive()) {
			logger.error("has been started.");
			return;
		}
		logger.info("starting as server...");
		logger.info("initializing...");
		this.host = host;
		this.port = port;
		eventLoopGroup = new NioEventLoopGroup();
		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(eventLoopGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		// 保持TCP连接
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		serverBootstrap.childHandler(getChannelInitializer());
		logger.info("initialized.");
		channel = serverBootstrap.bind(host, port).sync().channel();
		logger.info("started as server.");
	}

	/**
	 * 停止
	 */
	public void stop() {
		if (!isAlive()) {
			logger.error("has been stopped.");
			return;
		}
		logger.info("stopping...");
		eventLoopGroup.shutdownGracefully();
		eventLoopGroup = null;
		serverBootstrap = null;
		channel = null;
		logger.info("stopped.");
	}

	/**
	 * 是否存活
	 *
	 * @return 存活，返回true；否则，返回false
	 */
	public boolean isAlive() {
		return channel != null;
	}

	/**
	 * 获得地址
	 *
	 * @return 地址
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 获得端口
	 *
	 * @return 端口
	 */
	public int getPort() {
		return port;
	}
}
