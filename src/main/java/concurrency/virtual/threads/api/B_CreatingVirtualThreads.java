package concurrency.virtual.threads.api;

import static concurrency.part3.async.api.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class B_CreatingVirtualThreads {

	public static void main(String[] args) throws InterruptedException {

		Runnable task = () -> {
			System.out.println("I am TASK running in " + Thread.currentThread().getName()
					+ ", which is a DAEMON thread? " + Thread.currentThread().isDaemon());
		};

		// PLatform thread by DEFAULT is not a DAEMON thread, you can change it
		Thread thread1 = new Thread(task);
		thread1.start();
		thread1.join();

		// set Platform thread as DAEMON
		Thread thread2 = Thread.ofPlatform().daemon().name("Platform thread 2").unstarted(task);
		thread2.start();
		thread2.join();

		Thread thread3 = Thread.ofVirtual().name("Virtual thread 3").unstarted(task);
		thread3.start();
		thread3.join();

		//// Virtual threads do not have a name by default, also it is DAEMON by default
		// you can not change it
		Thread thread4 = Thread.startVirtualThread(task);
		thread4.join();

		System.out.println();
		System.out.println(
				"Creating Virtual Threads using Executor - see how it is possible to create 1 million lightweight VTs");
		var set = ConcurrentHashMap.<String>newKeySet();
		task = () -> set.add(Thread.currentThread().toString());

		int N_TASKS = 1_000_000; // 1 million
		Instant start = Instant.now();
		// create VTs on demand, and let them die after task execution
		/*
		 * Creates an Executor that starts a new virtual Thread for each task.The number
		 * of threads created by the Executor is unbounded.
		 */
		try (var es1 = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int index = 0; index < N_TASKS; index++) {
				es1.submit(task);
			}
		}

		System.out.println("# threads used = " + set.size());
		printElapsedTime(start);
	}
}
