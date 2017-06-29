import org.msgpack.MessagePack;
import org.msgpack.type.MapValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试MsgPack
 * Created by wuy on 2017/5/17.
 */
public class TestMsgPack {

	public static byte[] pack() throws IOException {
		Map<String, Object> data = new HashMap<>();
		data.put("S", "GAC");
		data.put("D", "GAS");
		Map<String, Object> param = new HashMap<>();
		param.put("url", "http://www.test.com");
		List<Integer> list = new ArrayList<>();
		list.add(10);
		list.add(60);
		param.put("other", list);
		data.put("P", param);
		MessagePack messagePack = new MessagePack();
		byte[] raw = messagePack.write(data);
		return raw;
	}

	public static Map unpack(byte[] raw) throws IOException {
		MessagePack messagePack = new MessagePack();
		MapValue mapValue = messagePack.read(raw).asMapValue();
//		for (Value v : mapValue.keySet()) {
//			Value temp = mapValue.getByServerId(v);
//			System.out.println(v + ":" + temp);
//		}
		return mapValue;
	}

	public static void main(String[] args) throws Exception {
		byte[] raw = pack();
		Map data = unpack(raw);
		System.out.println(data);
	}
}
