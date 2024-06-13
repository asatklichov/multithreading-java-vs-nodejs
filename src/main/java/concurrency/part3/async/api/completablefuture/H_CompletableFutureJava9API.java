package concurrency.part3.async.api.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import concurrency.part3.async.api.completablefuture.java11.httpclient.Util;

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

public class H_CompletableFutureJava9API {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		// defaultExecutor
		/**
		 * Returns the default Executor used for async methods that do not specify an
		 * Executor
		 */
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
		Executor defaultExecutor = completableFuture.defaultExecutor();
		System.out.println(defaultExecutor);

		CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> "Hello",
				Executors.newCachedThreadPool());// ForkJoinPool.commonPool();
		Executor defaultExecutor2 = completableFuture2.defaultExecutor();
		System.out.println(defaultExecutor2);
		System.out.println();

		// Method newIncompleteFuture()
		/**
		 * it now has better support for subclassing, thanks to the newIncompleteFuture
		 * virtual constructor
		 * 
		 * 
		 * newIncompleteFuture, also known as the “virtual constructor”, is used to get
		 * a new completable future instance of the same type. Especially useful when
		 * subclassing CompletableFuture
		 */
		completableFuture = completableFuture.newIncompleteFuture();
		// System.out.println(completableFuture.get());
		completableFuture.complete("new");
		System.out.println(completableFuture.get());

		// copy()
		/**
		 * This method returns a new CompletableFuture which. This method may be useful
		 * as a form of “defensive copying”, to prevent clients from completing, while
		 * still being able to arrange dependent actions on a specific instance of
		 * CompletableFuture.
		 */
		CompletableFuture<String> copyCompletableFuture2 = completableFuture2.copy();
		System.out.println(copyCompletableFuture2.get());
		System.out.println();

		// minimalCompletionStage()
		/**
		 * returns a new CompletionStage which behaves in the exact same way as
		 * described by the copy method, however, such new instance throws
		 * UnsupportedOperationException in every attempt to retrieve or set the
		 * resolved value
		 * 
		 * A new CompletableFuture with all methods available can be retrieved by using
		 * the toCompletableFuture method available on the CompletionStage API.
		 */
		CompletionStage<String> minimalCompletionStage = completableFuture2.minimalCompletionStage();
		CompletableFuture<String> completableFuture3 = minimalCompletionStage.toCompletableFuture();
		System.out.println(completableFuture3.get());
		System.out.println();

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
		CompletableFuture<Long> orTimeout = new CompletableFuture().orTimeout(1, TimeUnit.SECONDS);
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
		CompletableFuture completeOnTimeout = new CompletableFuture().completeOnTimeout("my Val", 1, TimeUnit.SECONDS);
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
		// CompletionStage<String> failedStage = copyCompletableFuture6.failedStage(new
		// RuntimeException("failedStage error!"));
		// System.out.println(failedStage.toCompletableFuture().get());
		System.out.println();

		// failedFuture()
		/**
		 * The failedFuture method adds the ability to specify an already completed
		 * exceptionally CompleatebleFuture instance.
		 */
		// completableFuture7.completeExceptionally(new RuntimeException("Calculation
		// failed!"));
		CompletableFuture<Object> failedFuture = CompletableFuture
				.failedFuture(new RuntimeException("failedFuture error!"));
		System.out.println(failedFuture.get());
		System.out.println();

	}
}
