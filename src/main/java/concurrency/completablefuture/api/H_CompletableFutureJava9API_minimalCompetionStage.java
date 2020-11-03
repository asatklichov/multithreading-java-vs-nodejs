package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

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

public class H_CompletableFutureJava9API_minimalCompetionStage {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

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
		CompletionStage<String> minimalCompletionStage = completableFuture.minimalCompletionStage();
		CompletableFuture<String> completableFuture3 = minimalCompletionStage.toCompletableFuture();
		System.out.println(completableFuture3.get());
		System.out.println();

	}
}
