package game.net;

import javax.crypto.SecretKey;

/**
 * 用户代理
 * Created by wuy on 2017/6/22.
 */
public class UserAgent extends Agent {

	// 600000毫秒=10分钟
	final private static long MAX_TOKEN_VALID_TIME = 600000;

	// AES密钥
	private SecretKey aesKey;
	// 认证用token
	private String token;
	// token有效期
	private long tokenValidTime;

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
		return "UserAgent{" +
				"aesKey=" + aesKey +
				", token='" + token + '\'' +
				", tokenValidTime=" + tokenValidTime +
				'}';
	}
}
