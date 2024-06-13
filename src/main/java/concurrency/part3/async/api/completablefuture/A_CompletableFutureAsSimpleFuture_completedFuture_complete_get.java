package concurrency.part3.async.api.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class CompleteCompletableFuture {

	public static void main(String[] args) {

		// empty CF
		CompletableFuture<Void> cf = new CompletableFuture<>();

		Runnable task = () -> {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			// complete CF - can be helpful later to deal with complex chain of async-tasks
			cf.complete(null);			
			//see also: CompletableFuture<String> completeAsync = new CompletableFuture().completeAsync(() -> "CompleteAsync");
			
		};
		CompletableFuture.runAsync(task);

		Void nil = cf.join(); // not blocked anymore
		System.out.println("done");
	}
}

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
		 * 
		 * For example, we can create an instance of this class with a no-arg
		 * constructor to represent some future result, hand it out to the consumers,
		 * and complete it at some time in the future using the complete method. The
		 * consumers may use the get method to block the current thread until this
		 * result is provided.
		 */
		Future<String> completableFuture = calculateAsync();
		// BLOCKED
		System.out.println("Use the get method to block the current thread until result is provided");
		String result = completableFuture.get();
		System.out.println(result);
		System.out.println(result.startsWith("Hellooo"));

		/**
		 * If we already know the result of a computation, we can use the static
		 * completedFuture method with an argument that represents a result of this
		 * computation. Consequently, the get method of the Future will never block,
		 * immediately returning this result instead:
		 * 
		 * Returns a new CompletableFuture that is already completed with the given
		 * value
		 */
		Future<String> completableFuture2 = CompletableFuture.completedFuture("Hello");
		// NEVER BLOCKED
		System.out.println(
				"\nget() NEVER blocks here - immediately returning this result instead, If we already know (via completedFuture()) the result of a computation in advance");
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

		/**
		 * Alternative solution to TT ...
		 * 
		 * 
		 * <pre>
		 *
		 * public class SyncCompletableFuture extends CompletableFuture {
		 * 
		 * 	public static <U> CompletableFuture<U> supplySync(Supplier<U> supplier) {
		 * 		CompletableFuture future = CompletableFuture.supplyAsync(supplier);
		 * 		return syncFuture(future);
		 * 	}
		 * 
		 * 	public static CompletableFuture<Void> runSync(Runnable runnable) {
		 * 		CompletableFuture future = CompletableFuture.runAsync(runnable);
		 * 		return syncFuture(future);
		 * 	}
		 * 
		 * 	private static CompletableFuture syncFuture(CompletableFuture future) {
		 * 		try {
		 * 			future.get();
		 * 		} catch (InterruptedException | ExecutionException e) {
		 * 			future.cancel(true);
		 * 		}
		 * 		return future;
		 * 	}
		 * }
		 * 
		 * </pre>
		 */

		ExecutorService exec = Executors.newCachedThreadPool();
		exec.submit(() -> {
			Thread.sleep(500);
			completableFuture.complete("Hellooo - I am completed in other task");
			return null;
		});
		exec.shutdown();

		return completableFuture;
	}
}

class GetNowDemo {

	private static String process() {
		sleep(2000);
		System.out.println(
				"Current Execution thread where the supplier is executed - " + Thread.currentThread().getName());
		return "Hello Man";
	}

	private static CompletableFuture<String> createFuture() {
		return CompletableFuture.supplyAsync(GetNowDemo::process);
	}

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		CompletableFuture<String> stringCompletableFuture = createFuture();

		String valueToReturn = "Result not yet available";

		/**
		 * Returns the result value (or throws any encountered exception)if completed,
		 * else returns the given valueIfAbsent.
		 */
		String value = stringCompletableFuture.getNow(valueToReturn);

		sleep(1000);

		System.out.println("Completed, result = " + value);

	}
}