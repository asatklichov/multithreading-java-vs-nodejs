package concurrency.java.concurrent.api;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class RunnablePattern {

	public static void main(String[] args) {

		Runnable task = () -> System.out.println("I am in thread " + Thread.currentThread().getName());
		for (int i = 0; i < 10; i++) {
			// 10 new created
			new Thread(task).start();
		}
		// 10 threads dead without re-used
		// Non ordered, Thread scheduler not guarantee the order in Thread Core API
	}
}

class ExecutorAndRunnable {

	public static void main(String[] args) {

		Runnable task = () -> System.out.println(Thread.currentThread().getName()
				+ "I am a Runnable by Executor, can do task but can't return a value ");

		// ExecutorService service = Executors.newSingleThreadExecutor();

		// Executor service = Executors.newFixedThreadPool(4); //can not call shutdown()
		ExecutorService service = Executors.newFixedThreadPool(4);

		for (int i = 0; i < 10; i++) {
			/**
			 * 4 thread are re-used here, if you use Executors.newSingleThreadExecutor then
			 * single thread will be re-used
			 */
			service.execute(task);
		}

		service.shutdown();

	}
}

class ExecutorServiceAndRunnableAndFuture {

	public static void main(String[] args) throws ExecutionException, InterruptedException {

		Runnable task = () -> {
			System.out.println(Thread.currentThread().getName()
					+ " I am a Runnable by ExecutorService and can do task and can say If I am done ");
		};

		ExecutorService executor = Executors.newFixedThreadPool(4);
		System.out.println();

		for (int i = 0; i < 10; i++) {
			Future<?> future = executor.submit(task);
			if (future.get() == null) {
				System.out.println(
						"	" + future.get() + " means successfully completed for submit(Runnable task) method");
			} else {
				System.out.println("Task not completed yet");
			}
		}

		// using anonymous class
		Future<?> future = executor.submit(new Runnable() {
			public void run() {
				System.out.println("Asynchronous Runnable task by submit and return  FUTURE ");
			}
		});
		System.out.println("future.get1() = " + future.get()); // returns null if the task has finished correctly.

		System.out.println(" -- Runnable by ExecutorService with RETURN value -- ");
		String result = "I am a RETURN value";
		task = () -> System.out.println(" I am a Runnable by ExecutorService with RETURN value ");
		Future<String> obj = executor.submit(task, result);
		System.out.println("future.get1() = " + obj.get()); // returns null if the task has finished correctly.

		// if not enabled you see JVM still keep running
		// executor.shutdown();

	}
}

class ExecutorServiceAndCallableAndFuture {

	public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

		/**
		 * Submit Callable The Java ExecutorService submit(Callable) method is similar
		 * to the submit(Runnable) method except it takes a Java Callable instead of a
		 * Runnable.
		 */
		Callable<String> task = () -> Thread.currentThread().getName() + " I am a Callable by ExecutorService";
		ExecutorService executorService = Executors.newFixedThreadPool(4);

		try {
			for (int i = 0; i < 10; i++) {
				Future<String> future = executorService.submit(task);
				System.out.println("I get: " + future.get());
			}

			// 2-example by anonymous class
			Future<String> future = executorService.submit(new Callable<String>() {
				public String call() throws Exception {
					System.out.println("Asynchronous Callable by submit and return  FUTURE");
					return "Callable Result";
				}
			});
			System.out.println("future.get3() = " + future.get());
		} finally {
			executorService.shutdown();
		}

		// 3-handle timeouts, different future.get() method
		task = () -> {
			Thread.sleep(300);
			return "I am in thread " + Thread.currentThread().getName();
		};

		ExecutorService executor = Executors.newSingleThreadExecutor();

		for (int i = 0; i < 10; i++) {
			Future<String> future = executor.submit(task);
			System.out.println("I get: " + future.get(100, TimeUnit.MILLISECONDS));
		}
		// JVM not down, use below in try-finally as others, see picture: shutdown.JPG
		executor.shutdown();
	}
}

//to simulate java.util.concurrent.ExecutionException 
class ExecutorServiceAndCallableAndFuture2 {

	public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

		ExecutorService executor = Executors.newSingleThreadExecutor();

		// 4-example, task not completed just thrown illegal state exception
		Callable<String> task = () -> {
			throw new IllegalStateException("I throw an exception in thread " + Thread.currentThread().getName());
		};
		// Caused by: java.lang.IllegalStateException: I throw an exception in thread
		// pool-1-thread-1

		try {
			for (int i = 0; i < 10; i++) {
				Future<String> future = executor.submit(task);
				System.out.println("I get: " + future.get());
			}
		} finally {
			executor.shutdown();
		}
	}
}

class ExecutorServiceAndCallableAndFutureCancellation {

	public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future future = executor.submit(new Fibonacci(23));
		try {
			future.get(2, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			future.cancel(true);
			System.out.println("Task Cancelled");
		} finally {
			executor.shutdown();
		}

	}
}

class Fibonacci implements Callable<Integer>, Serializable {
	int input = 0;

	public Fibonacci() {
	}

	public Fibonacci(int input) {
		this.input = input;
	}

	public Integer call() throws InterruptedException {
		Thread.sleep(2000);
		return calculate(input);
	}

	private int calculate(int n) {
		if (Thread.currentThread().isInterrupted())
			return 0;
		if (n <= 1)
			return n;
		else
			return calculate(n - 1) + calculate(n - 2);
	}
}
