package game.server.login;

import game.business.Business;
import game.net.UserAgent;
import game.net.message.Message;
import game.util.security.AESUtil;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

/**
 * 登录业务
 * Created by wuy on 2017/6/26.
 */
public class LoginBusiness extends Business {

	// 握手
	public Message shakeHand(Message message) throws IOException {
		SecretKey aesKey = AESUtil.createSecretKey();
		String hexKey = ByteUtils.toHexString(aesKey.getEncoded());
		getLogger().info("key.getEncoded:" + ByteUtils.toHexString(aesKey.getEncoded()));
		Message newMessage = new Message();
		newMessage.setInvokeMethodName(message.getCallbackMethodName());
		Map<String, Object> newParameter = new HashMap<>();
		newParameter.put("code", "0");
		newParameter.put("aes_key", hexKey);
		newMessage.setParameter(newParameter);
		// 回调
		UserAgent userAgent = (UserAgent) message.getAgent();
		userAgent.writeAndFlush(newMessage);
		// 设置aesKey
		userAgent.setAesKey(aesKey);
		return null;
	}

	// 登录
	public Message login(Message message) throws IOException {
		MessagePack messagePack = new MessagePack();
		Map<String, String> parameter = messagePack.read(message.getParameterBytes(), Templates.tMap(Templates.TString, Templates.TString));
		getLogger().info("parameter:" + parameter);
		Message newMessage = new Message();
		newMessage.setInvokeMethodName(message.getCallbackMethodName());
		Map<String, Object> newParameter = new HashMap<>();
		newParameter.put("code", "0");
//		List<String> gameWorldServerList = new ArrayList<>();
//		gameWorldServerList.add("GWS_01");
//		gameWorldServerList.add("GWS_02");
//		gameWorldServerList.add("GWS_03");
//		newParameter.put("gws_list", gameWorldServerList);
		newMessage.setParameter(newParameter);
		newMessage.setAgent(getServer().getServerAgentManager().get("ES_01"));
		return newMessage;
	}

	// 选择游戏世界(返回的是网关服务器地址)
	public Message selectGameWorld(Message message) throws IOException {
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
		newMessage.setParameter(newParameter);
		// 回调
		UserAgent userAgent = (UserAgent) message.getAgent();
		userAgent.writeAndFlush(newMessage);
		// 设置token
		userAgent.setToken(token);
		return null;
	}
}
