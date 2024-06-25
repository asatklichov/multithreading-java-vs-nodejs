package concurrency.virtual.threads.api;

import static concurrency.part3.async.api.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class A_MillionFactorVirtualThreads {
	public static void main(String[] args) throws InterruptedException {
		int numTasks = 1000000;// million
		Instant start = Instant.now();
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < numTasks; i++) {
			Thread thread = Thread.startVirtualThread(() -> {
				try {
					System.out.println(Thread.currentThread().getName() + " is sleeping");
					Thread.sleep(Duration.ofSeconds(10));
				} catch (InterruptedException e) {
				}
			});
			threads.add(thread);
		}
		for (Thread thread : threads) {
			thread.join();
		}

		printElapsedTime(start);
		/**
		 * <pre>
		*Elapsed time: 66793 ms
		 * </pre>
		 */

	}

}
