package concurrency.part2.concurrent.api;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * hreadFactory As the name suggests, ThreadFactory acts as a thread
 * (non-existing) pool which creates a new thread on demand. It eliminates the
 * need of a lot of boilerplate coding for implementing efficient thread
 * creation mechanisms.
 *
 */
public class MyThreadFactory implements ThreadFactory {
	private int threadId;
	private String name;

	public MyThreadFactory(String name) {
		threadId = 1;
		this.name = name;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, name + "-Thread_" + threadId);
		System.out.println("New thread id : " + threadId + ", name : " + t.getName());
		threadId++;
		return t;
	}

	public static void main(String[] args) {
		ThreadFactory factory = new MyThreadFactory("MThreadFactory");
		AtomicInteger ai = new AtomicInteger();
		ai.set(1);
		for (int i = 0; i < 10; i++) {
			// you can not use i here - it must be effective final
			Runnable r = () -> System.out.println("Ooo: " + ai.incrementAndGet());
			Thread t = factory.newThread(r);
			t.start();
		}
	}
}