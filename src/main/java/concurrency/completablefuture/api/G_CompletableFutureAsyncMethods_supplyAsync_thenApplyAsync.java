package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

public class G_CompletableFutureAsyncMethods_supplyAsync_thenApplyAsync {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		/**
		 * This allows us to parallelize our computation even more and use system
		 * resources more efficiently:
		 */
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

		//
		CompletableFuture<String> future = completableFuture.thenApplyAsync(s -> s + " World");

		System.out.println("Hello World".equals(future.get()));

	}
}
