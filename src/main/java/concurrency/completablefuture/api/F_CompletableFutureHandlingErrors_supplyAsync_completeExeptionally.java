package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * https://www.baeldung.com/java-completablefuture
 * 
 * For error handling in a chain of asynchronous computation steps, we have to
 * adapt the throw/catch idiom in a similar fashion.
 * 
 * Instead of catching an exception in a syntactic block, the CompletableFuture
 * class allows us to handle it in a special handle method.
 * 
 * This method receives two parameters: a result of a computation (if it
 * finished successfully), and the exception thrown (if some computation step
 * did not complete normally).
 */

public class F_CompletableFutureHandlingErrors_supplyAsync_completeExeptionally {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		String name = null; // lolo

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
			if (name == null) {
				throw new RuntimeException("Computation error!");
			}
			return "Hello, " + name;
		}).handle((s, t) -> s != null ? s : "Hello, Stranger!");

		System.out.println("Hello, Stranger!".equalsIgnoreCase(completableFuture.get()));

		/**
		 * As an alternative scenario, suppose we want to manually complete the Future
		 * with a value, as in the first example, but also have the ability to complete
		 * it with an exception.
		 * 
		 * The completeExceptionally method is intended for just that. The
		 * completableFuture.get() method in the following example throws an
		 * ExecutionException with a RuntimeException as its cause:
		 */

		CompletableFuture<String> completableFuture2 = new CompletableFuture<>();
		completableFuture2.completeExceptionally(new RuntimeException("Calculation failed!"));
		System.out.println(completableFuture2.get()); // ExecutionException
		
		/**
		 * In the example above, we could have handled the exception with the handle
		 * method asynchronously, but with the get method we can use the more typical
		 * approach of a synchronous exception processing.
		 */
	}
}
