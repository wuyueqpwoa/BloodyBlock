import game.common.message.Message;
import game.common.message.MessageQueue;
import org.msgpack.MessagePack;
import org.msgpack.type.MapValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试MessageQueue
 * Created by wuy on 2017/6/22.
 */
public class TestMessageQueue {

	public static void startCustomer(MessageQueue messageQueue) {
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						System.out.println(messageQueue.takeFirst());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static void startProducer(MessageQueue messageQueue) {
		for (int i = 0; i < 10; ++i) {
			new Thread() {
				@Override
				public void run() {
					try {
						for (int i = 0; i < 100; ++i) {
							Message message = new Message();
							message.setSourceConnectId(i);
							message.setDestinationServerId(String.valueOf(this.getId()));
							message.setInvokeMethodName(this.getName());
							messageQueue.putLast(message);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}


	public static void main(String[] args) throws Exception {
		MessageQueue messageQueue = new MessageQueue();
		startCustomer(messageQueue);
		startProducer(messageQueue);
	}
}
