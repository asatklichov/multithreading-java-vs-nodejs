package concurrency.part2.concurrent.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * See from CORE API {@link concurrency.part1.thread.core.api.ThreadInterruptionDemo }
 * 
 * Java Thread interrupt() method
 * 
 * <pre>
 *The interrupt() method of thread class is used to interrupt the thread. 
 *
 *If any thread is in sleeping or waiting state (i.e.
 * sleep() or wait() is invoked) then using the interrupt() method, we can
 * interrupt the thread execution by throwing InterruptedException.
 * 
 * If the thread is not in the sleeping or waiting state then calling the
 * interrupt() method performs a normal behavior and doesn't interrupt the
 * thread but sets the interrupt flag to true.
 * 
 * https://www.javatpoint.com/java-thread-interrupt-method
 * </pre>
 */

public class ThreadInterruptionDemo extends Thread {

	/**
	 * To stop threads in Java, we rely on a co-operative mechanism called
	 * Interruption. The concept is very simple. To stop a thread, all we can do is
	 * deliver it a signal, aka interrupt it, requesting that the thread stops
	 * itself at the next available opportunity. That’s all. There is no telling
	 * what the receiver thread might do with the signal: it may not even bother to
	 * check the signal; or even worse ignore it.
	 */
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(3);
		executor.submit(new MyThread());
		executor.submit(new MyThread());
		executor.submit(new MyThread());
		executor.shutdownNow();
	}

}

class MyThread implements Runnable {
	@Override
	public void run() {
		for (int i = 1; i < 10000000; i++)
			try {
				System.out.println(i + " ThreadID: " + Thread.currentThread().getId());
				if (Thread.interrupted())
					throw new InterruptedException();
			} catch (InterruptedException e) {
				return;
			}
	}
}
