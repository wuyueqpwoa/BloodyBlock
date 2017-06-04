package game.common.net;

import io.netty.channel.Channel;

import javax.crypto.SecretKey;

/**
 * 客户端代理
 * 套接字连接以通道为单位，对该通道进行封装
 * Created by wuy on 2017/5/25.
 */
public class ClientAgent extends SocketAgent {

	// 600000毫秒=10分钟
	final private static long MAX_TOKEN_VALID_TIME = 600000;

	// AES密钥
	private SecretKey aesKey;
	// 认证用token
	private String token;
	// token有效期
	private long tokenValidTime;

	public ClientAgent(Channel channel) {
		super(channel);
	}

	public SecretKey getAesKey() {
		return aesKey;
	}

	public void setAesKey(SecretKey aesKey) {
		this.aesKey = aesKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
		this.tokenValidTime = System.currentTimeMillis() + MAX_TOKEN_VALID_TIME;
	}

	public long getTokenValidTime() {
		return tokenValidTime;
	}

	public void setTokenValidTime(long tokenValidTime) {
		this.tokenValidTime = tokenValidTime;
	}

	@Override
	public String toString() {
		return "ClientAgent{" +
				"aesKey=" + aesKey +
				", token='" + token + '\'' +
				", tokenValidTime=" + tokenValidTime +
				'}';
	}
}
