package game.net.message;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息打包者
 * Created by wuy on 2017/6/22.
 */
public class MessagePacker {

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
		if (message.getBusinessName() != null) {
			temp.put("b", message.getBusinessName());
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
			} else if ("b".equals(key)) {
				message.setBusinessName(messagePack.convert(v, Templates.TString));
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
}
