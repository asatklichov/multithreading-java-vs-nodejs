package concurrency.java.concurrent.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ACustomizableThreadPools {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int corePoolSize = 5;
		int maxPoolSize = 10;
		long keepAliveTime = 5000;

		// ExecutorService threadPoolExecutor
		ThreadPoolExecutor threadPoolExecutor = getMyExecutorService(corePoolSize, maxPoolSize, keepAliveTime);

		// to customize with any values
		threadPoolExecutor.setCorePoolSize(4);

		Callable<String> c = () -> "Nesibe Istanbul";
		Future<String> future = threadPoolExecutor.submit(c);
		System.out.println(future.get());
		invokeAllExample(threadPoolExecutor);
		threadPoolExecutor.shutdown();

		System.out.println("Scheduled customized pool");
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = getMyScheduledExecutorService(4);
		ScheduledFuture<String> schedule = scheduledThreadPoolExecutor.schedule(new Callable<String>() {
			public String call() throws Exception {
				System.out.println("Processed");
				return "OKI";
			}
		}, 5, TimeUnit.SECONDS);

		System.out.println(schedule.get());
		scheduledThreadPoolExecutor.shutdown();
	}

	public static ThreadPoolExecutor getMyExecutorService(int corePoolSize, int maxPoolSize, long keepAliveTime) {
		// ExecutorService threadPoolExecutor =
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

		// to customize with any values
		threadPoolExecutor.setCorePoolSize(4);
		return threadPoolExecutor;
	}

	public static ScheduledThreadPoolExecutor getMyScheduledExecutorService(int corePoolSize) {
		ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(corePoolSize);

		return threadPoolExecutor;

	}

	private static void invokeAllExample(ExecutorService executorService)
			throws InterruptedException, ExecutionException {
		Set<Callable<String>> callables = new HashSet<Callable<String>>();

		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Task 1";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Task 2";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Task 3";
			}
		});

		List<Future<String>> futures = executorService.invokeAll(callables);

		for (Future<String> future : futures) {
			System.out.println("future.get = " + future.get());
		}
	}

}
