package io.iot.common.throadTest;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TestLock {
	private static volatile int condition = 0;
	private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(9);
//	自定义线程池时调用ThreadPoolExecutor第四构造函数，添加自定义的线程池工厂ThreadFactory，自定义拒绝策略RejectedExecutionHandler
//	new ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
//	        BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory,RejectedExecutionHandler handler);
	private static final ThreadLocal<Integer> local = new ThreadLocal<Integer>();
	public static void main(String[] args) throws InterruptedException {
		local.set(condition);
		Integer integer = local.get();
		Thread A = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!(condition == 1)) {
					// 条件不满足，自旋
					System.out.println("旋转，跳跃");
				}
				System.out.println("闭着眼");
			}
		});
		A.start();
		Thread.sleep(2000);
		condition = 1;
	}

	static class aa {

	}
}
