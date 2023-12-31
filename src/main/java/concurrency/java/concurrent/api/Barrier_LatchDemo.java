package concurrency.java.concurrent.api;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

 