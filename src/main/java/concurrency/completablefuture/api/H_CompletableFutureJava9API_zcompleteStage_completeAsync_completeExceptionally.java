package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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

public class H_CompletableFutureJava9API_zcompleteStage_completeAsync_completeExceptionally {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
 
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
		 
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
