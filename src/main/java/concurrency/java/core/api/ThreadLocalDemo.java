package concurrency.java.core.api;

import java.util.Arrays;
import java.util.List;

/**
 * - We can easily create classes whose fields are thread-local by simply
 * defining private fields in Thread classes.
 * 
 * -or Similarly, we can create thread-local fields by
 * assigning ThreadLocal instances to a field.
 * 
 * In both implementations, the classes have their own state, but it's not
 * shared with other threads. Thus, the classes are thread-safe.
 *
 */
class ThreadA extends Thread {

	private final List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

	@Override
	public void run() {
		numbers.forEach(System.out::println);
	}
}

class ThreadB extends Thread {

	private final List<String> letters = Arrays.asList("a", "b", "c", "d", "e", "f");

	@Override
	public void run() {
		letters.forEach(System.out::println);
	}
}

class MyRunnable2 implements Runnable {
	/**
	 * Each thread has its own independently initialized copy of the variable.
	 */
	private ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

	@Override
	public void run() {
		threadLocal.set((int) (Math.random() * 100));
		System.out.println(threadLocal.get());
	}

	public static void main(String args[]) {
		MyRunnable2 shared = new MyRunnable2();
		Thread thread1 = new Thread(shared);
		Thread thread2 = new Thread(shared);
		thread1.start();
		thread2.start();
	}
}