package concurrency.part2.concurrent.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

public class Barrier_LatchDemo {

}

class CountDownLatchExample {
	/**
	 * A java.util.concurrent.CountDownLatch is a concurrency construct that allows
	 * one or more threads to wait for a given set of operations to complete.
	 * 
	 * A CountDownLatch is initialized with a given count. This count is decremented
	 * by calls to the countDown() method. Threads waiting for this count to reach
	 * zero can call one of the await() methods. Calling await() blocks the thread
	 * until the count reaches zero.
	 * 
	 * Below is a simple example. After the Decrementer has called countDown() 3
	 * times on the CountDownLatch, the waiting Waiter is released from the await()
	 * call.
	 */
	public static void main(String[] args) throws InterruptedException {
		// https://jenkov.com/tutorials/java-util-concurrent/countdownlatch.html
		// * https://www.baeldung.com/java-cyclicbarrier-countdownlatch
		CountDownLatch latch = new CountDownLatch(3);

		Waiter waiter = new Waiter(latch);
		Decrementer decrementer = new Decrementer(latch);

		Thread wt = new Thread(waiter);
		wt.start();
		new Thread(decrementer).start();

		Thread.sleep(4000);

		wt.join();

		CountDownLatch countDownLatch = new CountDownLatch(2);
		Thread t = new Thread(() -> {
			countDownLatch.countDown();
			countDownLatch.countDown();
		});
		t.start();
		countDownLatch.await();

		System.out.println(0 == countDownLatch.getCount());
	}

}

class Waiter implements Runnable {

	CountDownLatch latch = null;

	public Waiter(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Waiter Released");
	}
}

class Decrementer implements Runnable {

	CountDownLatch latch = null;

	public Decrementer(CountDownLatch latch) {
		this.latch = latch;
	}

	public void run() {

		try {
			System.out.println("wait .. ");
			Thread.sleep(1000);
			this.latch.countDown();

			System.out.println("wait a bit ..");
			Thread.sleep(2000);
			this.latch.countDown();

			System.out.println("wait a bit more ...");
			Thread.sleep(3000);
			this.latch.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

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
class Barrier_Latch_Reusability {

	@Test
	public void whenCountDownLatch_thenCorrect() throws IOException, InterruptedException {

		CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
		Thread t = new Thread(() -> {
			try {
				cyclicBarrier.await();
				// cyclicBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				// error handling
			}
		});
		t.start();

		assertFalse(cyclicBarrier.isBroken());
		assertEquals(1, cyclicBarrier.getNumberWaiting());

	}

	@Test
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
		assertTrue(3 < 6);
	}

	@Test
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

		assertTrue(9 > 7);
	}

}