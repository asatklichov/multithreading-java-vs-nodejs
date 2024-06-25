package concurrency.virtual.threads.api;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class B_CreatingVirtualThreads {

	public static void main(String[] args) throws InterruptedException {

		Runnable task = () -> {
			System.out.println("I am running in the thread " + Thread.currentThread().getName());
			System.out.println("I am running in daemon thread? " + Thread.currentThread().isDaemon());
		};

		Thread thread1 = new Thread(task);
		thread1.start();
		thread1.join();

		Thread thread2 = Thread.ofPlatform().daemon().name("Platform thread 2").unstarted(task);
		thread2.start();
		thread2.join();

		Thread thread3 = Thread.ofVirtual().name("Virtual thread 3").unstarted(task);
		thread3.start();
		thread3.join();

		Thread thread4 = Thread.startVirtualThread(task);
		thread4.join();

	}
}

class B_CreatingVirtualThreadsUsingExecutorService {

	public static void main(String[] args) {

		var set = ConcurrentHashMap.<String>newKeySet();
		Runnable task = () -> set.add(Thread.currentThread().toString());

		int N_TASKS = 2000;

		try (var es1 = Executors.newVirtualThreadPerTaskExecutor()) {
			for (int index = 0; index < N_TASKS; index++) {
				es1.submit(task);
			}
		}

		System.out.println("# threads used = " + set.size());
	}
}
