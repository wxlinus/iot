package io.iot.common.throadTest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public interface Test {
	// 接口中只能定义常量，变量不符合接口的定义，默认非公平锁
	static final Lock lock = new ReentrantLock(true);
	default void getString() {
		System.out.println("hello word");
	}
	void aa();
	
//	Scanner scanner = new Scanner(System.in);
//	scanner.next();  
	public static void main(String[] args) {
//		ArrayDeque是继承自Deque接口，Deque继承自Queue接口，
//		Queue是队列，而Deque是双端队列，也就是可以从前或者从后插入或者取出元素，
//		也就是比队列存取更加方便一点，单向队列只能从一头插入，从另一头取出。
//		Deque<String> aa = new ArrayDeque<>();
//		aa.push("1");
//		aa.push("2");
//		System.out.println(aa.peek());
//		aa.push("3");
//		System.out.println(aa.peek());
//		System.out.println(aa.peekLast());

//		java.util.concurrent 包里的 BlockingQueue是一个接口， 继承Queue接口
//		单向队列只能从一头插入，从另一头取出。
		BlockingQueue queue = new ArrayBlockingQueue(1024);

		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);

		new Thread(producer).start();
		new Thread(consumer).start();

		try {
			Thread.sleep(4000);
			queue.put("4");
			Thread.sleep(1000);
			System.out.println(queue.size());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Test test = new Test() {
//			@Override
//			public void aa() {
//				System.out.println("hello");
//			}
//		};
//		test.aa();
//		test.getString();
//		test.lock.lock();
	}

	public class Producer implements Runnable {

		protected BlockingQueue queue = null;

		public Producer(BlockingQueue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				queue.put("1");
				Thread.sleep(1000);
				queue.put("2");
				Thread.sleep(1000);
				queue.put("3");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public class Consumer implements Runnable {

		protected BlockingQueue queue = null;

		public Consumer(BlockingQueue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				System.out.println(queue.take());
				System.out.println(queue.take());
				System.out.println(queue.take());
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
