package concurrency.virtual.threads.api;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simulate thread block by sleep and see the jumping of carrier threads.
 * Because, once Virtual Thread gets blocked, then that carrier-thread is
 * released to run other virtual threads.
 * 
 * And blocked VT will wait until callback is available, once data is available,
 * then virtual thread continue to be executed by other carrier threads.
 * 
 * <pre>
 *  VirtualThread[#29]/runnable@ForkJoinPool-1-worker-1
	VirtualThread[#29]/runnable@ForkJoinPool-1-worker-9
	VirtualThread[#29]/runnable@ForkJoinPool-1-worker-10
	VirtualThread[#29]/runnable@ForkJoinPool-1-worker-11
 * </pre>
 * 
 * Such above thing you have not seen it in Java. Once you give a TASK to a
 * THREAD, it is ATTACHED to THREAD and there is NO WAY to DETACH it and ATTACH
 * to other THREAD again.
 * 
 * So, as you see, in Java 21, TASK is DETACHED from one THREAD and ATTACHED to
 * other PLATFORM THREAD (carrier)
 * 
 * 
 * 
 */
class BlockingVirtualThreadsUsingSleep {

	public static void main(String[] args) throws InterruptedException {

		Runnable task1 = () -> {
			System.out.println(Thread.currentThread());
			sleepFor(10, ChronoUnit.MICROS);
			System.out.println(Thread.currentThread());
			sleepFor(10, ChronoUnit.MICROS);
			System.out.println(Thread.currentThread());
			sleepFor(10, ChronoUnit.MICROS);
			System.out.println(Thread.currentThread());
		};
		Runnable task2 = () -> {
			sleepFor(10, ChronoUnit.MICROS);
			sleepFor(10, ChronoUnit.MICROS);
			sleepFor(10, ChronoUnit.MICROS);
		};

		int N_THREADS = 10; // 1, 5, 10
		var threads = new ArrayList<Thread>();
		for (int index = 0; index < N_THREADS; index++) {
			var thread = index == 0 ? Thread.ofVirtual().unstarted(task1) // only VT for displaying
					: Thread.ofVirtual().unstarted(task2);
			threads.add(thread);
		}

		for (Thread thread : threads) {
			thread.start();
		}

		for (Thread thread : threads) {
			thread.join();
		}
	}

	private static void sleepFor(int amount, ChronoUnit unit) {
		try {
			/**
			 * sleepFor() -> sleep() -> vthread.sleepNanos()->parkNanos() ->
			 * yieldContinuation() -> return Continuation.yield(VTHREAD_SCOPE);
			 * 
			 * This DETACHES Virtual thread from its carrier Thread
			 * 
			 */
			Thread.sleep(Duration.of(amount, unit));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}

class BlockingVirtualThreadsUsingReentrantLockPinningFix {

	private static int counter = 0;

	public static void main(String[] args) throws InterruptedException {

		var lock = new ReentrantLock();

		Runnable task1 = () -> {
			System.out.println(Thread.currentThread());
			lock.lock();
			try {
				counter++;
				sleepFor(1, ChronoUnit.MICROS);
			} finally {
				lock.unlock();
			}
			System.out.println(Thread.currentThread());
			lock.lock();
			try {
				counter++;
				sleepFor(1, ChronoUnit.MICROS);
			} finally {
				lock.unlock();
			}
			System.out.println(Thread.currentThread());
			lock.lock();
			try {
				counter++;
				sleepFor(1, ChronoUnit.MICROS);
			} finally {
				lock.unlock();
			}
			System.out.println(Thread.currentThread());
		};
		Runnable task2 = () -> {
			lock.lock();
			try {
				counter++;
				sleepFor(1, ChronoUnit.MICROS);
			} finally {
				lock.unlock();
			}
			lock.lock();
			try {
				counter++;
				sleepFor(1, ChronoUnit.MICROS);
			} finally {
				lock.unlock();
			}
			lock.lock();
			try {
				counter++;
				sleepFor(1, ChronoUnit.MICROS);
			} finally {
				lock.unlock();
			}
		};

		int N_THREADS = 2_000;

		var threads = new ArrayList<Thread>();

		for (int index = 0; index < N_THREADS; index++) {
			var thread = index == 0 ? Thread.ofVirtual().unstarted(task1) : Thread.ofVirtual().unstarted(task2);
			threads.add(thread);
		}

		for (Thread thread : threads) {
			thread.start();
		}

		for (Thread thread : threads) {
			thread.join();
		}

		System.out.println("# threads = " + N_THREADS);
		System.out.println("counter   = " + counter);
	}

	private static void sleepFor(int amount, ChronoUnit unit) {
		try {
			Thread.sleep(Duration.of(amount, unit));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}

/**
 *
 * <pre>
 * class VirtualThreadContinuation {
 * 
 * 	// --enable-preview --add-exports java.base/jdk.internal.vm=ALL-UNNAMED
 * 	public static void main(String[] args) {
 * 
 * 		var scope = new ContinuationScope("My scope");
 * 		var continuation = new Continuation(scope, () -> {
 * 			System.out.println("Running in a continuation");
 * 			Continuation.yield(scope);
 * 			System.out.println("After the call to yield");
 * 		});
 * 
 * 		System.out.println("Running in the main method");
 * 		continuation.run();
 * 		System.out.println("Back in the main method");
 * 		continuation.run();
 * 		System.out.println("Back again in the main method");
 * 
 * 	}
 * }
 * </pre>
 */
