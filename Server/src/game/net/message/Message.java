package game.net.message;

import game.net.agent.Agent;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息
 * Created by wuy on 2017/6/22.
 */
public class Message {

	// 目标服务器ID，如果是客户端的消息，一般由登录服务器或网关服务器强写本字段
	private String destinationServerId;
	// 来源服务器ID
	private String sourceServerId;
	// 调用方法名
	private String invokeMethodName;
	// 参数(MessagePack打包)
	private byte[] parameterBytes;
	// 回调方法名
	private String callbackMethodName;
	// 来源用户通道ID
	private String userChannelId;
	// 回调方法名(用户)
	private String userCallbackMethodName;

	// 代理(非打包字段)
	private Agent agent;

	public String getDestinationServerId() {
		return destinationServerId;
	}

	public void setDestinationServerId(String destinationServerId) {
		this.destinationServerId = destinationServerId;
	}

	public String getSourceServerId() {
		return sourceServerId;
	}

	public void setSourceServerId(String sourceServerId) {
		this.sourceServerId = sourceServerId;
	}

	public String getInvokeMethodName() {
		return invokeMethodName;
	}

	public void setInvokeMethodName(String invokeMethodName) {
		this.invokeMethodName = invokeMethodName;
	}

	public byte[] getParameterBytes() {
		return parameterBytes;
	}

	public void setParameterBytes(byte[] parameterBytes) {
		this.parameterBytes = parameterBytes;
	}

	public String getCallbackMethodName() {
		return callbackMethodName;
	}

	public void setCallbackMethodName(String callbackMethodName) {
		this.callbackMethodName = callbackMethodName;
	}

	public String getUserChannelId() {
		return userChannelId;
	}

	public void setUserChannelId(String userChannelId) {
		this.userChannelId = userChannelId;
	}

	public String getUserCallbackMethodName() {
		return userCallbackMethodName;
	}

	public void setUserCallbackMethodName(String userCallbackMethodName) {
		this.userCallbackMethodName = userCallbackMethodName;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public void setParameter(Map parameter) throws IOException {
		if (parameter == null) {
			return;
		}
		MessagePack messagePack = new MessagePack();
		parameterBytes = messagePack.write(parameter);
	}

	public Map getParameter() throws IOException {
		if (parameterBytes == null) {
			return null;
		}
		MessagePack messagePack = new MessagePack();
		return messagePack.read(parameterBytes).asMapValue();
	}

	/**
	 * 打包消息
	 *
	 * @return 消息字节
	 * @throws IOException 打包异常
	 */
	public byte[] pack() throws IOException {
		Map<String, Object> temp = new HashMap<>();
		if (userChannelId != null) {
			temp.put("u", userChannelId);
		}
		if (destinationServerId != null) {
			temp.put("d", destinationServerId);
		}
		if (sourceServerId != null) {
			temp.put("s", sourceServerId);
		}
		if (invokeMethodName != null) {
			temp.put("i", invokeMethodName);
		}
		if (parameterBytes != null) {
			temp.put("p", parameterBytes);
		}
		if (callbackMethodName != null) {
			temp.put("c", callbackMethodName);
		}
		if (userCallbackMethodName != null) {
			temp.put("uc", userCallbackMethodName);
		}
		return new MessagePack().write(temp);
	}

	/**
	 * 解包消息
	 *
	 * @param bytes 消息字节
	 * @throws IOException 解包异常
	 */
	public Message unpack(byte[] bytes) throws IOException {
		MessagePack messagePack = new MessagePack();
		MapValue mapValue = messagePack.read(bytes).asMapValue();
		for (Value k : mapValue.keySet()) {
			String key = messagePack.convert(k, Templates.TString);
			Value v = mapValue.get(k);
			if ("u".equals(key)) {
				userChannelId = messagePack.convert(v, Templates.TString);
			} else if ("d".equals(key)) {
				destinationServerId = messagePack.convert(v, Templates.TString);
			} else if ("s".equals(key)) {
				sourceServerId = messagePack.convert(v, Templates.TString);
			} else if ("i".equals(key)) {
				invokeMethodName = messagePack.convert(v, Templates.TString);
			} else if ("p".equals(key)) {
				parameterBytes = messagePack.convert(v, Templates.TByteArray);
			} else if ("c".equals(key)) {
				callbackMethodName = messagePack.convert(v, Templates.TString);
			} else if ("uc".equals(key)) {
				userCallbackMethodName = messagePack.convert(v, Templates.TString);
			}
		}
		return this;
	}

	/**
	 * 打包消息(用户)
	 *
	 * @return 消息字节
	 * @throws IOException 打包异常
	 */
	public byte[] packForUser() throws IOException {
		Map<String, Object> temp = new HashMap<>();
		if (getInvokeMethodName() != null) {
			temp.put("i", getInvokeMethodName());
		}
		if (getParameterBytes() != null) {
			temp.put("p", getParameterBytes());
		}
		if (getCallbackMethodName() != null) {
			temp.put("c", getCallbackMethodName());
		}
		return new MessagePack().write(temp);
	}

	/**
	 * 解包消息(用户)
	 *
	 * @param bytes 消息字节
	 * @throws IOException 解包异常
	 */
	public Message unpackForUser(byte[] bytes) throws IOException {
		MessagePack messagePack = new MessagePack();
		MapValue mapValue = messagePack.read(bytes).asMapValue();
		for (Value k : mapValue.keySet()) {
			String key = messagePack.convert(k, Templates.TString);
			Value v = mapValue.get(k);
			if ("i".equals(key)) {
				setInvokeMethodName(messagePack.convert(v, Templates.TString));
			} else if ("p".equals(key)) {
				setParameterBytes(messagePack.convert(v, Templates.TByteArray));
			} else if ("c".equals(key)) {
				setCallbackMethodName(messagePack.convert(v, Templates.TString));
			}
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Message{").append("invokeMethodName=").append(invokeMethodName);
		if (parameterBytes != null) {
			stringBuilder.append(", parameterBytes=")
					.append((parameterBytes == null ? null : ByteUtils.toHexString(parameterBytes)));
		}
		if (callbackMethodName != null) {
			stringBuilder.append(", callbackMethodName=").append(callbackMethodName);
		}
		if (destinationServerId != null) {
			stringBuilder.append(", destinationServerId=").append(destinationServerId);
		}
		if (sourceServerId != null) {
			stringBuilder.append(", sourceServerId=").append(sourceServerId);
		}
		if (userChannelId != null) {
			stringBuilder.append(", userChannelId=").append(userChannelId);
		}
		if (userCallbackMethodName != null) {
			stringBuilder.append(", userCallbackMethodName=").append(userCallbackMethodName);
		}
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	public Message clone() {
		Message newMessage = new Message();
		newMessage.setDestinationServerId(destinationServerId);
		newMessage.setSourceServerId(sourceServerId);
		newMessage.setInvokeMethodName(invokeMethodName);
		newMessage.setParameterBytes(parameterBytes);
		newMessage.setCallbackMethodName(callbackMethodName);
		newMessage.setUserChannelId(userChannelId);
		newMessage.setUserCallbackMethodName(userCallbackMethodName);
		return newMessage;
	}
}
