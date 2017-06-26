package game.net.message;

import game.net.Agent;
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

	// 来源用户通道ID
	private String sourceUserChannelId;
	// 来源服务器ID
	private String sourceServerId;
	// 目标服务器ID，如果是客户端的消息，一般由登录服务器或网关服务器强写本字段
	private String destinationServerId;
	// 调用方法名
	private String invokeMethodName;
	// 参数(MessagePack打包)
	private byte[] parameterBytes;
	// 回调方法名
	private String callbackMethodName;
	// 代理(非打包字段)
	private Agent agent;

	public String getSourceUserChannelId() {
		return sourceUserChannelId;
	}

	public void setSourceUserChannelId(String sourceUserChannelId) {
		this.sourceUserChannelId = sourceUserChannelId;
	}

	public String getSourceServerId() {
		return sourceServerId;
	}

	public void setSourceServerId(String sourceServerId) {
		this.sourceServerId = sourceServerId;
	}

	public String getDestinationServerId() {
		return destinationServerId;
	}

	public void setDestinationServerId(String destinationServerId) {
		this.destinationServerId = destinationServerId;
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
	 * @param message 消息
	 * @return 消息字节
	 * @throws IOException 打包异常
	 */
	public static byte[] pack(Message message) throws IOException {
		Map<String, Object> temp = new HashMap<>();
		if (message.getSourceUserChannelId() != null) {
			temp.put("u", message.getSourceUserChannelId());
		}
		if (message.getSourceServerId() != null) {
			temp.put("s", message.getSourceServerId());
		}
		if (message.getDestinationServerId() != null) {
			temp.put("d", message.getDestinationServerId());
		}
		if (message.getInvokeMethodName() != null) {
			temp.put("i", message.getInvokeMethodName());
		}
		if (message.getParameterBytes() != null) {
			temp.put("p", message.getParameterBytes());
		}
		if (message.getCallbackMethodName() != null) {
			temp.put("c", message.getCallbackMethodName());
		}
		return new MessagePack().write(temp);
	}

	/**
	 * 解包消息
	 *
	 * @param bytes 消息字节
	 * @return 消息
	 * @throws IOException 解包异常
	 */
	public static Message unpack(byte[] bytes) throws IOException {
		MessagePack messagePack = new MessagePack();
		MapValue mapValue = messagePack.read(bytes).asMapValue();
		Message message = new Message();
		for (Value k : mapValue.keySet()) {
			String key = messagePack.convert(k, Templates.TString);
			Value v = mapValue.get(k);
			if ("u".equals(key)) {
				message.setSourceUserChannelId(messagePack.convert(v, Templates.TString));
			} else if ("s".equals(key)) {
				message.setSourceServerId(messagePack.convert(v, Templates.TString));
			} else if ("d".equals(key)) {
				message.setDestinationServerId(messagePack.convert(v, Templates.TString));
			} else if ("i".equals(key)) {
				message.setInvokeMethodName(messagePack.convert(v, Templates.TString));
			} else if ("p".equals(key)) {
				message.setParameterBytes(messagePack.convert(v, Templates.TByteArray));
			} else if ("c".equals(key)) {
				message.setCallbackMethodName(messagePack.convert(v, Templates.TString));
			}
		}
		return message;
	}

	@Override
	public String toString() {
		return "Message{" +
				"sourceUserChannelId='" + sourceUserChannelId + '\'' +
				", sourceServerId='" + sourceServerId + '\'' +
				", destinationServerId='" + destinationServerId + '\'' +
				", invokeMethodName='" + invokeMethodName + '\'' +
				", parameterBytes=" + (parameterBytes == null ? null : ByteUtils.toHexString(parameterBytes)) +
				", callbackMethodName='" + callbackMethodName + '\'' +
				'}';
	}
}
