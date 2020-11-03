package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import concurrency.completablefuture.java11.httpclient.Util;

/**
 * https://openjdk.java.net/jeps/266
 * 
 * https://www.baeldung.com/java-completablefuture
 * 
 * Enhancements to the CompletableFuture API
 * 
 * Time-based enhancements are added that enable a future to complete with a
 * value or exceptionally after a certain duration, see methods orTimeout and
 * completeTimeout. In addition, a complementary Executor returned by the static
 * methods named delayedExecutor allow a task to execute after a certain
 * duration. This may be combined with Executor receiving methods on
 * CompletableFuture to support operations with time-delays.
 * 
 * Subclass enhancements are added making it easier to extend from
 * CompletableFuture, such as to provide a subclass that supports an alternative
 * default executor.
 * 
 * <pre>
 *Java 9 enhances the CompletableFuture API with the following changes:

New factory methods added
Support for delays and timeouts
Improved support for subclassing
and new instance APIs:

Executor defaultExecutor()
CompletableFuture<U> newIncompleteFuture()
CompletableFuture<T> copy()
CompletionStage<T> minimalCompletionStage()
CompletableFuture<T> completeAsync(Supplier<? extends T> supplier, Executor executor)
CompletableFuture<T> completeAsync(Supplier<? extends T> supplier)
CompletableFuture<T> orTimeout(long timeout, TimeUnit unit)
CompletableFuture<T> completeOnTimeout(T value, long timeout, TimeUnit unit)
We also now have a few static utility methods:

Executor delayedExecutor(long delay, TimeUnit unit, Executor executor)
Executor delayedExecutor(long delay, TimeUnit unit)
<U> CompletionStage<U> completedStage(U value)
<U> CompletionStage<U> failedStage(Throwable ex)
<U> CompletableFuture<U> failedFuture(Throwable ex)
Finally, to address timeout, Java 9 has introduced two more new functions:

orTimeout()
completeOnTimeout()
 * </pre>
 */

public class H_CompletableFutureJava9API_timeout_delay_fail {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

		// orTimeout()
		/**
		 * 
		 * Another possibility is timing out which resolves the future exceptionally
		 * with TimeoutException.
		 * 
		 * CompletableFuture<T> orTimeout(long timeout, TimeUnit unit)
		 * 
		 * Resolves the CompletableFuture exceptionally with TimeoutException, unless it
		 * is completed before the specified timeout.
		 */
		CompletableFuture<Long> orTimeout = new CompletableFuture()
				.orTimeout(1, TimeUnit.SECONDS);
		// orTimeout.complete(Util.calcHeavySum(8, 2000));
		orTimeout.complete(Util.calcHeavySum(8, 0));
		System.out.println(orTimeout.get());
		System.out.println();

		// completeOnTimeout()
		/**
		 * Completes the CompletableFuture normally with the specified value unless it
		 * is completed before the specified timeout.
		 *
		 * Other than delayedExecutor. Another way to achieve a delayed result is to use
		 * the completeOnTimeout method.
		 */
		CompletableFuture completeOnTimeout = new CompletableFuture()
				.completeOnTimeout("my Val", 1, TimeUnit.SECONDS);
		System.out.println(completeOnTimeout.get());
		System.out.println();

		// Static API Additions
		// delayedExecutor
		/**
		 * Returns a new Executor that submits a task to the given base executor after
		 * the given delay (or no delay if non-positive). Each delay commences upon
		 * invocation of the returned executor's execute method. If no executor is
		 * specified the default executor (ForkJoinPool.commonPool()) will be used.
		 * 
		 * Is there any way to schedule CompletableFuture in Java? What I wanted to do
		 * is to schedule a task to be executed with some delay, and chain it with other
		 * operations to be performed asynchronously when it completes. So far I didn't
		 * find any way to do this.
		 * 
		 * For good ol' Futures we have e.g. ScheduledExecutorService, where we can
		 * schedule a task to be executed with some delay like this:
		 */
		// Executor defaultExecutor = completableFuture.defaultExecutor();
		Executor delayedExecutor = CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS);
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "someValue", delayedExecutor);
		future.thenAccept(System.out::println).join();
		System.out.println(future.get());

		// or
		CompletableFuture<Object> futurez = new CompletableFuture<>();
		futurez.completeAsync(() -> "input", CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS));
		System.out.println(futurez.get());

		System.out.println();

		// completedStage
		/**
		 * This utility methods return already resolved CompletionStage instances,
		 * either completed normally with a value (completedStage) or completed
		 * exceptionally (failedStage) with the given exception.
		 */
		CompletionStage<String> completedStage = CompletableFuture.completedStage("completedStage");
		System.out.println(completedStage.toCompletableFuture().get());
		System.out.println();

		// and failedStage
		CompletionStage<String> failedStage = CompletableFuture.failedStage(new RuntimeException("failedStage error!"));
		System.out.println(failedStage.toCompletableFuture().get());
		System.out.println();

		// failedFuture()
		/**
		 * The failedFuture method adds the ability to specify an already completed
		 * exceptionally CompleatebleFuture instance.
		 */
		// completableFuture7.completeExceptionally(new RuntimeException("Calculation
		// failed!"));
//		CompletableFuture<Object> failedFuture = CompletableFuture
//				.failedFuture(new RuntimeException("failedFuture error!"));
//		System.out.println(failedFuture.get());
//		System.out.println();

	}
}

class DelayedExecutorTest {

	// https://grokonez.com/java/java-9/java-9-completablefuture-api-improvements-delay-timeout-support
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> future = new CompletableFuture<>();
		future.completeAsync(() -> {
			try {
				System.out.println("inside future: processing data...");

				return "grokonez.com";
			} catch (Throwable e) {
				return "not detected";
			}
		}, CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS))
				.thenAccept(result -> System.out.println("accept: " + result));

		// other statements
		for (int i = 1; i <= 5; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("running outside... " + i + " s");
		}

	}
}

class OrTimeOutTest {

	private static final int TIMEOUT = 3;

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		/**
		 * The method exceptionally completes current CompletableFuture by throwing a
		 * TimeoutException if not otherwise completed before the timeout.
		 * 
		 * For example, we have a doWork() method that takes 5 seconds to return a
		 * CompletableFuture. But we set TIMEOUT only 3 seconds.
		 * 
		 * 
		 */
		CompletableFuture<String> future = doWork("JavaSampleApproach").orTimeout(TIMEOUT, TimeUnit.SECONDS)
				.whenComplete((result, error) -> {
					if (error == null) {
						System.out.println("The result is: " + result);
					} else {
						System.out.println("Sorry, timeout in " + TIMEOUT + " seconds.");
					}
				});

		String content;
		content = future.get();
		System.out.println("Result >> " + content);

	}

	private static CompletableFuture<String> doWork(String s) {

		return CompletableFuture.supplyAsync(() -> {
			for (int i = 1; i <= 5; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("running inside doWork()... " + i + " s");
			}
			return s + ".com";
		});
	}

}

class CompleteOnTimeoutTest {

	private static final int TIMEOUT = 3;

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		/**
		 * The method completes current CompletableFuture by input value if not otherwise completed before the timeout.
		 * 
		 */
		CompletableFuture<String> future = doWork("JavaSampleApproach")
				.completeOnTimeout("JavaTechnology", TIMEOUT, TimeUnit.SECONDS).whenComplete((result, error) -> {
					if (error == null) {
						System.out.println("The result is: " + result);
					} else {
						// this statement will never run.
						System.out.println("Sorry, timeout in " + TIMEOUT + " seconds.");
					}
				});

		String content = future.get();
		System.out.println("Result >> " + content);
	}

	private static CompletableFuture<String> doWork(String s) {

		return CompletableFuture.supplyAsync(() -> {
			for (int i = 1; i <= 7; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("running inside doWork()... " + i + " s");
			}
			return s + ".com";
		});
	}

}