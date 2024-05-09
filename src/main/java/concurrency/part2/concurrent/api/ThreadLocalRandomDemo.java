package concurrency.part2.concurrent.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import concurrency.part1.thread.core.api.ThreadLocalDemo;

/**
 * * See also {@link ThreadLocalDemo }
 * 
 * https://www.baeldung.com/java-thread-local-random
 */
public class ThreadLocalRandomDemo {

	public static void main(String[] args) throws InterruptedException {

		int unboundedRndVal = ThreadLocalRandom.current().nextInt();
		int boundedRndVal = ThreadLocalRandom.current().nextInt(0, 100);

		/**
		 * Java 8 also adds the nextGaussian() method to generate the next
		 * normally-distributed value with a 0.0 mean and 1.0 standard deviation from
		 * the generator’s sequence.
		 */
		double nextGaussian = ThreadLocalRandom.current().nextGaussian();
		
		ExecutorService executor = Executors.newWorkStealingPool();
		List<Callable<Integer>> callables = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
		    callables.add(() -> {
		         return random.nextInt();
		    });
		}
		executor.invokeAll(callables);

	}

}

/**
 * See also {@link ThreadLocalDemo }
 *
 *
 * Using a ThreadLocal with a Thread Pool or ExecutorService If you plan to use
 * a Java ThreadLocal from inside a task passed to a Java Thread Pool or a Java
 * ExecutorService, keep in mind that you do not have any guarantees which
 * thread will execute your task. However, if all you need is to make sure that
 * each thread uses its own instance of some object, this is not a problem. Then
 * you can use a Java ThreadLocal with a thread pool or ExecutorService just
 * fine.
 * 
 * https://jenkov.com/tutorials/java-concurrency/threadlocal.html see
 * https://www.baeldung.com/java-thread-local-random
 */
class ThreadLocalDemo2 {
	public static void main(String[] args) throws InterruptedException {
		MyRunnablez sharedRunnableInstance = new MyRunnablez();

		Thread thread1 = new Thread(sharedRunnableInstance);
		Thread thread2 = new Thread(sharedRunnableInstance);

		thread1.start();
		thread2.start();

		thread1.join(); // wait for thread 1 to terminate
		thread2.join(); // wait for thread 2 to terminate
	}

}

class MyRunnablez implements Runnable {

	private ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

	@Override
	public void run() {
		threadLocal.set((int) (Math.random() * 100D));

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		System.out.println(threadLocal.get());
	}
}

/**
 * 
 * InheritableThreadLocal The InheritableThreadLocal class is a subclass of
 * ThreadLocal. Instead of each thread having its own value inside a
 * ThreadLocal, the InheritableThreadLocal grants access to values to a thread
 * and all child threads created by that thread. Here is a full Java
 * InheritableThreadLocal example:
 */
class InheritableThreadLocalBasicExample {

	public static void main(String[] args) {

		ThreadLocal<String> threadLocal = new ThreadLocal<>();
		InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

		Thread thread1 = new Thread(() -> {
			System.out.println("===== Thread 1 =====");
			threadLocal.set("Thread 1 - ThreadLocal");
			inheritableThreadLocal.set("Thread 1 - InheritableThreadLocal");

			System.out.println(threadLocal.get());
			System.out.println(inheritableThreadLocal.get());

			Thread childThread = new Thread(() -> {
				System.out.println("===== ChildThread =====");
				System.out.println(threadLocal.get());
				System.out.println(inheritableThreadLocal.get());
			});
			childThread.start();
		});

		thread1.start();

		Thread thread2 = new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("===== Thread2 =====");
			System.out.println(threadLocal.get());
			System.out.println(inheritableThreadLocal.get());
		});
		thread2.start();
	}
}
