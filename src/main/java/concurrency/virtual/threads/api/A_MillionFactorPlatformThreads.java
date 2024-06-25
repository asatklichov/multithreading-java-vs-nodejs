package concurrency.virtual.threads.api;

import static concurrency.part3.async.api.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class A_MillionFactorPlatformThreads {
	public static void main(String[] args) throws InterruptedException {
		List<Thread> threads = new ArrayList<>();
		Instant start = Instant.now();
		int numTasks = 1000000;// million
		for (int i = 0; i < numTasks; i++) {
			// creates 1000000 platform threads
			Thread thread = new Thread(() -> {
				try {
					System.out.println(Thread.currentThread().getName() + " is sleeping");
					Thread.sleep(Duration.ofSeconds(10));
				} catch (InterruptedException e) {
				}
			});
			thread.start();
			threads.add(thread);
		}
		for (Thread thread : threads) {
			thread.join();
		}
		printElapsedTime(start);

		/**
		 *
		 * <pre>
		* Thread-74460 is sleeping
		[59.361s][warning][os,thread] Failed to start thread "Unknown thread" - _beginthreadex failed (EACCES) for attributes: stacksize: default, flags: CREATE_SUSPENDED STACK_SIZE_PARAM_IS.
		[59.361s][warning][os,thread] Failed to start the native thread for java.lang.Thread "Thread-74461"
		Exception in thread "main" java.lang.OutOfMemoryError: unable to create native thread: possibly out of memory or process/resource limits reached
		 *
		 *See for other languages as well
		 *
		 *https://pkolaczk.github.io/memory-consumption-of-async/
		 * </pre>
		 */

	}

}
