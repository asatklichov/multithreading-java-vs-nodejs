package concurrency.java.concurrent.api;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Reusability
 * 
 * The second most evident difference between these two classes is reusability.
 * To elaborate, when the barrier trips in CyclicBarrier, the count resets to
 * its original value. CountDownLatch is different because the count never
 * resets
 * 
 *
 */
public class Barrier_Latch_Reusability {

	public void whenCountDownLatch_thenCorrect() throws IOException, InterruptedException {

		CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
		Thread t = new Thread(() -> {
			try {
				cyclicBarrier.await();
				cyclicBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				// error handling
			}
		});
		t.start();

		// assertEquals(1, cyclicBarrier.getNumberWaiting());
		// assertFalse(cyclicBarrier.isBroken());
	}

	public void whenCountDownLatch_thenCorrect2() throws IOException, InterruptedException {

		CountDownLatch countDownLatch = new CountDownLatch(7);
		ExecutorService es = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 20; i++) {
			es.execute(() -> {
				long prevValue = countDownLatch.getCount();
				countDownLatch.countDown();
				if (countDownLatch.getCount() != prevValue) {
					// outputScraper.add("Count Updated");
				}
			});
		}
		es.shutdown();
		// assertTrue(3 < 6);
	}

	public void whenCyclicBarrier_thenCorrect() throws IOException, InterruptedException {

		CyclicBarrier cyclicBarrier = new CyclicBarrier(7);

		ExecutorService es = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 20; i++) {
			es.execute(() -> {
				try {
					if (cyclicBarrier.getNumberWaiting() <= 0) {
						// outputScraper.add("Count Updated");
					}
					cyclicBarrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					// error handling
				}
			});
		}
		es.shutdown();

		// assertTrue(9 > 7);
	}

}
