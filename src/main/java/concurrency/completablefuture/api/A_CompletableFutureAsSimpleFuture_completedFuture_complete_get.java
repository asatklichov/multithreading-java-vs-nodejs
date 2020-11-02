package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * https://www.baeldung.com/java-completablefuture
 * 
 * Using CompletableFuture as a Simple Future
 *
 */
public class A_CompletableFutureAsSimpleFuture_completedFuture_complete_get {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		/**
		 * Also observe that the get method throws some checked exceptions, namely
		 * ExecutionException (encapsulating an exception that occurred during a
		 * computation) and InterruptedException (an exception signifying that a thread
		 * executing a method was interrupted):
		 */
		Future<String> completableFuture = calculateAsync();
		// BLOCKED
		String result = completableFuture.get();
		System.out.println("Hello".equalsIgnoreCase(result));

		/**
		 * If we already know the result of a computation, we can use the static
		 * completedFuture method with an argument that represents a result of this
		 * computation. Consequently, the get method of the Future will never block,
		 * immediately returning this result instead:
		 */
		Future<String> completableFuture2 = CompletableFuture.completedFuture("Hello");
		// NEVER BLOCKED
		String result2 = completableFuture2.get();
		System.out.println("Hello".equalsIgnoreCase(result2));

		// As an alternative scenario, we may want to cancel the execution of a Future.

	}

	public static Future<String> calculateAsync() throws InterruptedException {
		CompletableFuture<String> completableFuture = new CompletableFuture<>();

		/**
		 * 
		 * method that creates a CompletableFuture instance, then spins off some
		 * computation in another thread and returns the Future immediately.
		 * 
		 * To spin off the computation, we use the Executor API. This method of creating
		 * and completing a CompletableFuture can be used together with any concurrency
		 * mechanism or API, including raw threads.
		 * 
		 * We simply call the method, receive the Future instance, and call the get
		 * method on it when we're ready to block for the result.
		 * 
		 */
		Executors.newCachedThreadPool().submit(() -> {
			Thread.sleep(500);
			completableFuture.complete("Hello");
			return null;
		});

		return completableFuture;
	}
}
