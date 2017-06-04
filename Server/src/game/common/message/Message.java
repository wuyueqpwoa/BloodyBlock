package game.common.message;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息
 * Created by wuy on 2017/5/21.
 */
public class Message {
	// 来源客户端连接ID
	private Integer sourceConnectId;
	// 目标服务器ID，格式一定为"服务器类型_编号"，如果是客户端的消息，一般由登录服务器或网关强写本字段
	private String destinationServerId;
	// 调用函数名
	private String invokeMethodName;
	// 参数
	private byte[] parameterBytes;
	// 回调函数名
	private String callbackMethodName;
	// 传输路线，途经服务器ID列表
	private List<String> route;

	/**
	 * 从MessagePack字节解包
	 *
	 * @param bytes MessagePack字节
	 */
	public static Message unpack(byte[] bytes) throws IOException {
		MessagePack messagePack = new MessagePack();
		MapValue mapValue = messagePack.read(bytes).asMapValue();
		Message socketMessage = new Message();
		for (Value k : mapValue.keySet()) {
			String key = messagePack.convert(k, Templates.TString);
			Value v = mapValue.get(k);
			if ("s".equals(key)) {
				socketMessage.setSourceConnectId(messagePack.convert(v, Templates.TInteger));
			} else if ("d".equals(key)) {
				socketMessage.setDestinationServerId(messagePack.convert(v, Templates.TString));
			} else if ("i".equals(key)) {
				socketMessage.setInvokeMethodName(messagePack.convert(v, Templates.TString));
			} else if ("p".equals(key)) {
				socketMessage.setParameterBytes(messagePack.convert(v, Templates.TByteArray));
			} else if ("c".equals(key)) {
				socketMessage.setCallbackMethodName(messagePack.convert(v, Templates.TString));
			} else if ("r".equals(key)) {
				socketMessage.setRoute(messagePack.convert(v, Templates.tList(Templates.TString)));
			}
		}
		return socketMessage;
	}

	/**
	 * 打包成MessagePack字节
	 *
	 * @return MessagePack字节
	 */
	public static byte[] pack(Message socketMessage) throws IOException {
		Map<String, Object> temp = new HashMap<>();
		if (socketMessage.getSourceConnectId() != null) {
			temp.put("s", socketMessage.getSourceConnectId());
		}
		if (socketMessage.getDestinationServerId() != null) {
			temp.put("d", socketMessage.getDestinationServerId());
		}
		if (socketMessage.getInvokeMethodName() != null) {
			temp.put("i", socketMessage.getInvokeMethodName());
		}
		if (socketMessage.getParameterBytes() != null) {
			temp.put("p", socketMessage.getParameterBytes());
		}
		if (socketMessage.getCallbackMethodName() != null) {
			temp.put("c", socketMessage.getCallbackMethodName());
		}
		if (socketMessage.getRoute() != null) {
			temp.put("r", socketMessage.getRoute());
		}
		MessagePack messagePack = new MessagePack();
		return messagePack.write(temp);
	}

	public Integer getSourceConnectId() {
		return sourceConnectId;
	}

	public void setSourceConnectId(Integer sourceConnectId) {
		this.sourceConnectId = sourceConnectId;
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

	/**
	 * 入参需要二次打包(网关服务器是转发客户端消息，不进行解包)
	 *
	 * @param parameter 参数
	 * @throws IOException
	 */
	public void setAndPackParameterBytes(Object parameter) throws IOException {
		MessagePack messagePack = new MessagePack();
		this.parameterBytes = messagePack.write(parameter);
	}

	public String getCallbackMethodName() {
		return callbackMethodName;
	}

	public void setCallbackMethodName(String callbackMethodName) {
		this.callbackMethodName = callbackMethodName;
	}

	public List<String> getRoute() {
		return route;
	}

	public void setRoute(List<String> route) {
		this.route = route;
	}

	@Override
	public String toString() {
		return "Message{" +
				"sourceConnectId=" + sourceConnectId +
				", destinationServerId='" + destinationServerId + '\'' +
				", invokeMethodName='" + invokeMethodName + '\'' +
				", parameterBytes=" + (parameterBytes == null ? null : ByteUtils.toHexString(parameterBytes)) +
				", callbackMethodName='" + callbackMethodName + '\'' +
				'}';
	}
}
