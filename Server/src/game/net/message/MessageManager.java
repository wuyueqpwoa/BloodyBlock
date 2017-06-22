package game.net.message;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 消息管理器
 * Created by wuy on 2017/6/22.
 */
public class MessageManager {

	final private LinkedBlockingDeque<Message> queue = new LinkedBlockingDeque<>();

	/**
	 * 将消息加入队列(阻塞)
	 *
	 * @param message 消息
	 * @throws InterruptedException 中断异常
	 */
	public void putLast(Message message) throws InterruptedException {
		queue.putLast(message);
	}

	/**
	 * 从队列中取出消息(阻塞)
	 *
	 * @return 消息
	 * @throws InterruptedException 中断异常
	 */
	public Message takeFirst() throws InterruptedException {
		return queue.takeFirst();
	}

	/**
	 * 获得队列中消息的数量
	 *
	 * @return 队列中消息的数量
	 */
	public int size() {
		return queue.size();
	}
}
