package concurrency.completablefuture.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * https://www.baeldung.com/java-completablefuture
 * 
 * Most methods of the fluent API in CompletableFuture class have two additional
 * variants with the Async postfix. These methods are usually intended for
 * running a corresponding step of execution in another thread.
 * 
 * The methods without the Async postfix run the next execution stage using a
 * calling thread. In contrast, the Async method without the Executor argument
 * runs a step using the common fork/join pool implementation of Executor that
 * is accessed with the ForkJoinPool.commonPool() method. Finally, the Async
 * method with an Executor argument runs a step using the passed Executor.
 * 
 * Here's a modified example that processes the result of a computation with a
 * Function instance. The only visible difference is the thenApplyAsync method,
 * but under the hood the application of a function is wrapped into a
 * ForkJoinTask instance (for more information on the fork/join framework, see
 * the article “Guide to the Fork/Join Framework in Java”). This allows us to
 * parallelize our computation even more and use system resources more
 * efficiently:
 */

public class G_CompletableFutureAsyncMethods_thenApplyAsync_completeAsync {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		/**
		 * This allows us to parallelize our computation even more and use system
		 * resources more efficiently:
		 */

		/**
		 * <pre>
		 * <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
		 * 
		<U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
		 * </pre>
		 * 
		 * These async callback variations help you further parallelize your
		 * computations by executing the callback tasks in a separate thread.
		 */

		/**
		 * In the above case, the task inside thenApply() is executed in the same thread
		 * where the supplyAsync() task is executed, or in the main thread if the
		 * supplyAsync() task completes immediately (try removing sleep() call to
		 * verify).
		 */
		String res = CompletableFuture.supplyAsync(() -> "Some Result").thenApply(result ->
		/*
		 * Executed in the same thread where the supplyAsync() task is executed or in
		 * the main thread If the supplyAsync() task completes immediately
		 */
		"Processed Result(thenApply): " + result).get();
		System.out.println(res);

		/**
		 * To have more control over the thread that executes the callback task, you can
		 * use async callbacks.
		 * 
		 * If you use thenApplyAsync() callback, then it will be executed in a different
		 * thread obtained from ForkJoinPool.commonPool()
		 */
		String res2 = CompletableFuture.supplyAsync(() -> "Some Result").thenApplyAsync(result ->
		// Executed in a different thread from ForkJoinPool.commonPool()
		"Processed Result(thenApplyAsync) seprate thread in ForkJoinPool: " + result).get();

		System.out.println(res2);

		// OR
		ExecutorService threadPool = Executors.newCachedThreadPool();// ForkJoinPool.commonPool();
		String res3 = CompletableFuture.supplyAsync(() -> "Some Result").thenApplyAsync(result ->
		// Executed in a different thread from ForkJoinPool.commonPool()
		"Processed Result(thenApplyAsync) with provided Executor: " + result, threadPool).get();

		System.out.println(res3);

		ExecutorService customThreadPool = new ThreadPoolExecutor(4, 10, 1, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		String res4 = CompletableFuture.supplyAsync(() -> "Some Result").thenApplyAsync(result ->
		// Executed in a different thread from ForkJoinPool.commonPool()
		"Processed Result(thenApplyAsync) with provided customThreadPool: " + result, customThreadPool).get();
		// it stops accepting new tasks, waits for previously submitted tasks to
		// execute, and then terminates the executor.
		customThreadPool.shutdown();
		// shutdownNow() - this method interrupts the running task and shuts down the
		// executor immediately.

		System.out.println(res4);

		// or using a THREAD
		MyRunnable myRunnable = new MyRunnable();

		CompletableFuture.supplyAsync(() -> "Some Result").thenApplyAsync(result -> "myRunnable: " + result,
				r -> new Thread(myRunnable).start());
		if (true) { // add CHECK
			myRunnable.terminate();// otherwise keeps running infinitively
		}

		// https://medium.com/swlh/completablefuture-a-simplified-guide-to-async-programming-41cecb162308
		CompletableFuture<Double> futureResult = new CompletableFuture<>();
		new Thread(() -> {
			try {
				// some long process
				futureResult.complete(10.0); // orTimeout()
			} catch (Exception e) {
				futureResult.completeExceptionally(e);
			}
		}).start();

		// Methods completeAsync()
		/**
		 * The completeAsync method should be used to complete the CompletableFuture
		 * asynchronously using the value given by the Supplier provided.
		 * 
		 * The difference between this two overloaded methods is the existence of the
		 * second argument, where the Executor running the task can be specified. If
		 * none is provided, the default executor (returned by the defaultExecutor
		 * method) will be used.
		 */
		CompletableFuture<String> completeAsync = new CompletableFuture().completeAsync(() -> "CompleteAsync");
		// completableFuture4.completeAsync(() -> "CompleteAsync",
		// Executors.newFixedThreadPool(2));
		System.out.println(completeAsync.get());
		System.out.println();

	}
}

//
class MyRunnable implements Runnable {

	private boolean terminated;

	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		System.out.println(name + " is running");
		while (!isTerminated()) {
			System.out.println(name + "  is sleeping");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}

		System.out.println(name + " FINISHED");
	}

	public synchronized boolean isTerminated() {
		return terminated;
	}

	public synchronized void terminate() {
		this.terminated = true;
	}
}

/**
 * https://www.nurkiewicz.com/2015/11/which-thread-executes.html
 */
class WhichThreadExecutes {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		/**
		 * supplyAsync() by default uses ForkJoinPool.commonPool(), thread pool shared
		 * between all CompletableFutures, all parallel streams and all applications
		 * deployed on the same JVM (if you are unfortunate to still use application
		 * server with many deployed artifacts).
		 * 
		 * This hard-coded, unconfigurable thread pool is completely outside of our
		 * control, hard to monitor and scale.
		 */
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			try (InputStream is = new URL("http://www.nurkiewicz.com").openStream()) {
				System.out.println("Downloading");
				return "ok4now";// IOUtils.toString(is, StandardCharsets.UTF_8);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		// specify your own Executor
		ExecutorService pool = Executors.newFixedThreadPool(2);
		future = CompletableFuture.supplyAsync(() -> {
			try (InputStream is = new URL("http://www.nurkiewicz.com").openStream()) {
				System.out.println("Downloading");
				return "ok4now";// IOUtils.toString(is, StandardCharsets.UTF_8);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}, pool);

		// more detailed
		/**
		 * The first transformation in thenApply() is registered while the task is still
		 * running. Thus it will be executed immediately after task completion in the
		 * same thread as the task.
		 * 
		 * However before registering second transformation we wait until the task
		 * actually completes.
		 * 
		 * Even worse, we shutdown the thread pool entirely, to make sure no other code
		 * can ever be executed there. So which thread will run second transformation?
		 * 
		 * We know it must happen immediately since the future we register callback on
		 * already completed. It turns out that by default client thread (!) is used!
		 * The output is as follows:
		 */
		future = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
			return "ABC";
		}, pool);

		future.thenApply(s -> {
			/**
			 * The first transformation in thenApply() is registered while the task is still
			 * running. Thus it will be executed immediately after task completion in the
			 * same thread as the task.
			 */
			System.out.println("First transformation by pool-1-thread-1");
			return s.length();
		});

		future.get();
		pool.shutdownNow();
		pool.awaitTermination(1, TimeUnit.MINUTES);

		/**
		 * However before registering second transformation we wait until the task
		 * actually completes.
		 * 
		 * So which thread will run second transformation? We know it must happen
		 * immediately since the future we register callback on already completed. It
		 * turns out that by default client thread (!) is used!
		 */
		future.thenApply(s -> {
			System.out.println("Second transformation by main");
			return s.length();
		});
		// pool-1-thread-1 | First transformation main | Second transformation

		// now try with thenApplyAsync
		CompletableFuture.supplyAsync(() -> "Some Result").thenApplyAsync(s -> {
			System.out.println("Second transformation by ForkJoinPool.commonPool() differnet thread");
			return s.length();
		});

		// but we want our own
		ExecutorService pool2 = Executors.newFixedThreadPool(2);
		CompletableFuture.supplyAsync(() -> "Some Result").thenApplyAsync(s -> {
			System.out.println("Second transformation by pool2");
			return s.length();
		}, pool2);

		// Treating callback like another computation step
		/**
		 * if you are having troubles with long-running callbacks and transformations
		 * (remember that this article applies to almost all other methods on
		 * CompletableFuture), you should simply use another explicit CompletableFuture,
		 * like here:
		 */
		// Imagine this is slow and costly
		String init = "9";

		CompletableFuture<String> valFuture = CompletableFuture.supplyAsync(() -> "4");
		/**
		 * However we must replace thenApply() with thenCompose(), otherwise we'll end
		 * up with CompletableFuture<CompletableFuture<Integer>>.
		 */
		CompletableFuture<Long> composedFuture = valFuture.thenCompose(WhichThreadExecutes::heavyCalc);
		System.out.println(composedFuture.join());

		// in separate thread from ForkJOin
		composedFuture = valFuture.thenComposeAsync(WhichThreadExecutes::heavyCalc);
		System.out.println(composedFuture.join());

		// in separate thread from ForkJOin
		composedFuture = valFuture.thenComposeAsync(WhichThreadExecutes::heavyCalc);
		System.out.println(composedFuture.join());
		System.out.println();

		ExecutorService customThreadPool = new ThreadPoolExecutor(4, 10, 1, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		composedFuture = valFuture.thenComposeAsync(WhichThreadExecutes::heavyCalc, customThreadPool);
		customThreadPool.shutdown();
		// shutdownNow() - this method interrupts the running task and shuts down the
		// executor immediately.
		System.out.println(composedFuture.join());
		System.out.println();

		// or using a THREAD
		MyRunnable myRunnable = new MyRunnable();
		composedFuture = valFuture.thenComposeAsync(WhichThreadExecutes::heavyCalc,
				r -> new Thread(myRunnable).start());
		if (true) { // add CHECK
			myRunnable.terminate();// otherwise keeps running infinitively
		}
		System.out.println(composedFuture.join());
		System.out.println();

	}

	// slow opt
	private static CompletableFuture<Long> heavyCalc(String init) {
		ExecutorService pool3 = Executors.newFixedThreadPool(2);
		return CompletableFuture.supplyAsync(() -> Long.valueOf(init) + heavySum(), pool3);
	}

	private static long heavySum() {
		long sum = 0;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			sum += i;
		}
		System.out.println("Sum = " + sum);
		return sum;
	}
}

class HowTo {
	/**
	 * https://stackoverflow.com/questions/23320407/how-to-cancel-java-8-completable-future
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		/**
		 * https://stackoverflow.com/questions/23320407/how-to-cancel-java-8-completable-future
		 * 
		 * 
		 * When you call CompletableFuture#cancel, you only stop the downstream part of
		 * the chain. Upstream part, i. e. something that will eventually call
		 * complete(...) or completeExceptionally(...), doesn't get any signal that the
		 * result is no more needed.
		 * 
		 * <pre>
		When you call CompletableFuture#cancel, you only stop the downstream part of the chain. Upstream part, i. e. something that will eventually call complete(...) or completeExceptionally(...), doesn't get any signal that the result is no more needed.
		
		What are those 'upstream' and 'downstream' things?
		
		Let's consider the following code:
		
		CompletableFuture
		.supplyAsync(() -> "hello")               //1
		.thenApply(s -> s + " world!")            //2
		.thenAccept(s -> System.out.println(s));  //3
		Here, the data flows from top to bottom - from being created by supplier, through being modified by function, to being consumed by println. The part above particular step is called upstream, and the part below is downstream. E. g. steps 1 and 2 are upstream for step 3.
		
		Here's what happens behind the scenes. This is not precise, rather it's a convenient mind model of what's going on.
		
		Supplier (step 1) is being executed (inside the JVM's common ForkJoinPool).
		The result of the supplier is then being passed by complete(...) to the next CompletableFuture downstream.
		Upon receiving the result, that CompletableFuture invokes next step - a function (step 2) which takes in previous step result and returns something that will be passed further, to the downstream CompletableFuture's complete(...).
		Upon receiving the step 2 result, step 3 CompletableFuture invokes the consumer, System.out.println(s). After consumer is finished, the downstream CompletableFuture will receive it's value, (Void) null
		As we can see, each CompletableFuture in this chain has to know who are there downstream waiting for the value to be passed to their's complete(...) (or completeExceptionally(...)). But the CompletableFuture don't have to know anything about it's upstream (or upstreams - there might be several).
		
		Thus, calling cancel() upon step 3 doesn't abort steps 1 and 2, because there's no link from step 3 to step 2.
		
		It is supposed that if you're using CompletableFuture then your steps are small enough so that there's no harm if a couple of extra steps will get executed.
		
		If you want cancellation to be propagated upstream, you have two options:
		
		Implement this yourself - create a dedicated CompletableFuture (name it like cancelled) which is checked after every step (something like step.applyToEither(cancelled, Function.identity()))
		Use reactive stack like RxJava 2, ProjectReactor/Flux or Akka Streams
		 * </pre>
		 * 
		 * 
		 */

		// https://dzone.com/articles/20-examples-of-using-javas-completablefuture
		/**
		 * For CompletableFuture, the boolean parameter is not used because the
		 * implementation does not employ interrupts to do the cancelation. Instead,
		 * cancel() is equivalent to completeExceptionally(new CancellationException()).
		 */
		CompletableFuture<String> cf = CompletableFuture.completedFuture("habar").thenApplyAsync(String::toUpperCase,
				CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS, Executors.newCachedThreadPool()));

		CompletableFuture<String> cf2 = cf.exceptionally(ex -> {
			System.out.println("Oops! We have an exception - " + ex.getMessage());
			return "Unknown!";
		});

		cf.cancel(true);
		System.out.println(cf.isCompletedExceptionally());
		System.out.println(cf2.join());

	}
}
 