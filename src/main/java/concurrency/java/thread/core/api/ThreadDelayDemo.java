package concurrency.java.thread.core.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 
 * https://www.baeldung.com/java-delay-code-execution
 * 
 */

public class ThreadDelayDemo extends Thread {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("1. A Thread-Based Approach");

		Runnable r = () -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("I am sleeping");
		};

		Thread t = new Thread(r);
		t.start();

		t.join();

		try {
			int secondsToSleep = 2;
			Thread.sleep(secondsToSleep * 1000);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}

		System.out.println("\n2. Using TimeUnit.sleep");
		/*
		 * For better readability, we can use TimeUnit.XXX.sleep(y), where XXX is the
		 * time unit to sleep for (SECONDS, MINUTES, etc.), and y is the number of that
		 * unit to sleep for.
		 * 
		 * However, there are some disadvantages to using these thread-based methods:
		 * 
		 * The sleep times are not exactly precise, especially when using smaller time
		 * increments like milliseconds and nanoseconds When used inside of loops, sleep
		 * will drift slightly between loop iterations due to other code execution so
		 * the execution time could get imprecise after many iterations
		 */
		try {
			int secondsToSleep = 2;
			TimeUnit.SECONDS.sleep(secondsToSleep);
			System.out.println("I slept " + secondsToSleep + " TimeUnit.SECONDS");
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}

		System.out.println("\n3. An ExecutorService-Based Approach");
		/**
		 * Java provides the ScheduledExecutorService interface, which is a more robust
		 * and precise solution. This interface can schedule code to run once after a
		 * specified delay or at fixed time intervals.
		 */
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		int delayInSeconds = 2;
		executorService.schedule(r, delayInSeconds, TimeUnit.SECONDS);
		System.out.println("I delayed " + delayInSeconds + " TimeUnit.SECONDS");
		executorService.shutdown();

	}
}