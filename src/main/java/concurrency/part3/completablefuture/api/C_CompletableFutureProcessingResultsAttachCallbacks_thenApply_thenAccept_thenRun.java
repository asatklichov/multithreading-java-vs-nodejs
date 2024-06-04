package concurrency.part3.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * https://www.baeldung.com/java-completablefuture
 * 
 *
 *
 * The CompletableFuture.get() method is blocking. It waits until the Future is
 * completed and returns the result after its completion.
 * 
 * But, that’s not what we want right? For building asynchronous systems we
 * should be able to attach a callback to the CompletableFuture which should
 * automatically get called when the Future completes.
 * 
 * That way, we won’t need to wait for the result, and we can write the logic
 * that needs to be executed after the completion of the Future inside our
 * callback function.
 * 
 * You can attach a callback to the CompletableFuture using thenApply(),
 * thenAccept() and thenRun() methods -
 * 
 * 
 * https://www.callicoder.com/java-8-completablefuture-tutorial/
 * 
 */
public class C_CompletableFutureProcessingResultsAttachCallbacks_thenApply_thenAccept_thenRun {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

		/**
		 * * The most generic way to process the result of a computation is to feed it
		 * to a function. The thenApply method does exactly that; it accepts a Function
		 * instance, uses it to process the result, and returns a Future that holds a
		 * value returned by a function:
		 * 
		 * You can also write a sequence of transformations on the CompletableFuture by
		 * attaching a series of thenApply() callback methods. The result of one
		 * thenApply() method is passed to the next in the series -
		 */
		CompletableFuture<String> future = completableFuture.thenApply(s -> s + " World")
				.thenApply(s -> s + " of Physics, Sep. Nukus 1994 ");

		System.out.println(future.get().contains("Hello World"));
		System.out.println(future.get());
		System.out.println();

		/**
		 * If we don't need to return a value down the Future chain, we can use an
		 * instance of the Consumer functional interface. Its single method takes a
		 * parameter and returns void.
		 * 
		 * The thenAccept method receives a Consumer and passes it the result of the
		 * computation. Then the final future.get() call returns an instance of the Void
		 * type:
		 */
		CompletableFuture<Void> future2 = completableFuture
				.thenAccept(s -> System.out.println("Computation returned: " + s));
		// System.out.println("null".equalsIgnoreCase(future2.get()));//complains VOID
		System.out.println(future2.get());
		System.out.println();

		/**
		 * Finally, if we neither need the value of the computation, nor want to return
		 * some value at the end of the chain, then we can pass a Runnable lambda to the
		 * thenRun method. In the following example, we simply print a line in the
		 * console after calling the future.get():
		 */

		CompletableFuture<Void> future3 = completableFuture.thenRun(() -> System.out.println("Computation finished."));
		System.out.println(future3.get());

		CompletableFuture
				.runAsync(() -> System.out
						.println("\nBut be careful when chaining with Runnable. NULL value is provided"))
				.thenAccept(val -> {
					System.out.println(val);					
				});

	}

}
