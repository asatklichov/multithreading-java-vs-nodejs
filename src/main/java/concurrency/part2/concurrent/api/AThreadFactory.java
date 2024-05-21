package concurrency.part2.concurrent.api;

import java.util.concurrent.ThreadFactory;

/**
 * ThreadFactory acts as a thread (non-existing) pool which creates a new thread
 * on demand. It eliminates the need of a lot of boilerplate coding for
 * implementing efficient thread creation mechanisms.
 *
 */
public class AThreadFactory implements ThreadFactory {
	private int threadId;
	private String name;

	public AThreadFactory(String name) {
		threadId = 1;
		this.name = name;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, name + "-Thread_" + threadId);
		System.out.println("New new thread created, id: " + threadId + ", name : " + t.getName());
		threadId++;
		return t;
	}

	public static void main(String[] args) {
		AThreadFactory factory = new AThreadFactory("E_ThreadFactory");
		for (int i = 0; i < 10; i++) {
			Runnable task = () -> System.out.println("Congratulations!");
			Thread t = factory.newThread(task);
			t.start();
		}
	}
}