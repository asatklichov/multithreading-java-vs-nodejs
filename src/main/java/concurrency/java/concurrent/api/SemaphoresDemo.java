package concurrency.java.concurrent.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;



class OwnSemaphore {
	private boolean signal = false;

	public synchronized void take() {
		this.signal = true;
		this.notify();
	}

	public synchronized void release() throws InterruptedException {
		while (!this.signal)
			wait();
		this.signal = false;
	}

}


/**
 * Desc
 *
 * <pre>
*The java.util.concurrent.Semaphore class is a counting semaphore. 
*That means that it has two main methods:

acquire()
release()
The counting semaphore is initialized with a given number of "permits". 
For each call to acquire() a permit is taken by the calling thread. 
For each call to release() a
 permit is returned to the semaphore. Thus, at most N threads can pass the 
 acquire() method without any release() calls, where N is the number of permits the semaphore was initialized with. The permits are just a simple counter. Nothing fancy here.

Semaphore Usage
As semaphore typically has two uses:

To guard a critical section against entry by more than N threads at a time.
To send signals between two threads.
Guarding Critical Sections
If you use a semaphore to guard a critical section, the thread trying to enter 
the critical section will typically first try to acquire a permit, enter the critical section, and then release the permit again after. Like this:
 * </pre>
 */
class SemaphoreIntro {

	// https://jenkov.com/tutorials/java-util-concurrent/semaphore.html
	public static void main(String[] args) throws InterruptedException {
		Semaphore semaphore = new Semaphore(1);

		// critical section
		// semaphore.acquire(1); blocked until permit
		semaphore.acquire();

		// ...

		semaphore.release();

	}

}

public class SemaphoresDemo {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(10);

		Semaphore semaphore = new Semaphore(5);

		Runnable longRunningTask = () -> {
			boolean permit = false;
			try {
				// get permit:
				// semaphore.acquire(1); blocked until permit
				// ask 1 permit here
				permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
				if (permit) {
					System.out.println("Semaphore acquired");
					// sleep(5);
				} else {
					System.out.println("Could not acquire semaphore");
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				if (permit) {
					semaphore.release();
				}
			}
		};

		IntStream.range(0, 10).forEach(i -> executor.submit(longRunningTask));

		// stop(executor);
	}
}




/**
 * 
 * Fairness
 *
 * <pre>
No guarantees are made about fairness of the threads acquiring permits from the Semaphore. 
That is, there is no guarantee that the first thread to call acquire() is also the first 
thread to obtain a permit. If the first thread is blocked waiting for a permit, then a 
second thread checking for a permit just as a permit is released, may actually obtain the 
permit ahead of thread 1.

If you want to enforce fairness, the Semaphore class has a constructor that takes a boolean 
telling if the semaphore should enforce fairness. Enforcing fairness comes at a 
performance / concurrency penalty, so don't enable it unless you need it.

Here is how to create a Semaphore in fair mode:

Semaphore semaphore = new Semaphore(1, true);
 * </pre>
 */
class FairnessExample {

}