package game.server.login;

import game.common.message.CloneableMessageHandler;
import game.common.message.Message;
import game.common.message.UserMessageHandler;
import game.common.net.UserAgent;
import game.common.security.AESUtil;
import game.server.Server;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

/**
 * 登录服务器，面向用户端的消息处理者
 * Created by wuy on 2017/5/25.
 */
public class LoginUserMessageHandler extends UserMessageHandler {

	public LoginUserMessageHandler(Server server) {
		super(server);
	}

	@Override
	public CloneableMessageHandler clone() {
		return new LoginUserMessageHandler(getServer());
	}

	// 握手
	public void shakeHand(UserAgent userAgent, Message message) throws IOException {
		SecretKey aesKey = AESUtil.createSecretKey();
		String hexKey = ByteUtils.toHexString(aesKey.getEncoded());
		getLogger().info("key.getEncoded:" + ByteUtils.toHexString(aesKey.getEncoded()));
		Message newMessage = new Message();
		newMessage.setInvokeMethodName(message.getCallbackMethodName());
		Map<String, Object> newParameter = new HashMap<>();
		newParameter.put("code", "0");
		newParameter.put("aes_key", hexKey);
		newMessage.setAndPackParameterBytes(newParameter);
		send(newMessage, userAgent);
		// 设置aesKey
		userAgent.setAesKey(aesKey);
	}

	// 登录
	public void login(UserAgent userAgent, Message message) throws IOException {
		MessagePack messagePack = new MessagePack();
		Map<String, String> parameter = messagePack.read(message.getParameterBytes(), Templates.tMap(Templates.TString, Templates.TString));
		getLogger().info("parameter:" + parameter);
		Message newMessage = new Message();
		newMessage.setInvokeMethodName(message.getCallbackMethodName());
		Map<String, Object> newParameter = new HashMap<>();
		newParameter.put("code", "0");
		List<String> gameWorldServerList = new ArrayList<>();
		gameWorldServerList.add("GWS_01");
		gameWorldServerList.add("GWS_02");
		gameWorldServerList.add("GWS_03");
		newParameter.put("gws_list", gameWorldServerList);
		newMessage.setAndPackParameterBytes(newParameter);
		send(newMessage, userAgent);
	}

	// 选择游戏世界(返回的是网关服务器地址)
	public void selectGameWorld(UserAgent userAgent, Message message) throws IOException {
		MessagePack messagePack = new MessagePack();
		Map<String, String> parameter = messagePack.read(message.getParameterBytes(), Templates.tMap(Templates.TString, Templates.TString));
		getLogger().info("parameter:" + parameter);
		Message newMessage = new Message();
		newMessage.setInvokeMethodName(message.getCallbackMethodName());
		Map<String, Object> newParameter = new HashMap<>();
		newParameter.put("code", "0");
		newParameter.put("ip", "127.0.0.1");
		newParameter.put("port", "18100");
		String token = UUID.randomUUID().toString();
		newParameter.put("token", token);
		newMessage.setAndPackParameterBytes(newParameter);
		send(newMessage, userAgent);
		// 设置token
		userAgent.setToken(token);
	}
}
