package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * https://www.baeldung.com/java-completablefuture
 *
 * 
 */
public class C_CompletableFutureProcessingResultsOfAsynCalc_thenApply_thenAccept_thenRun {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");

		/**
		 * * The most generic way to process the result of a computation is to feed it
		 * to a function. The thenApply method does exactly that; it accepts a Function
		 * instance, uses it to process the result, and returns a Future that holds a
		 * value returned by a function:
		 */
		/*
		 * The thenCompose method, together with thenApply, implement basic building
		 * blocks of the monadic pattern. They closely relate to the map and flatMap
		 * methods of Stream and Optional classes also available in Java 8.
		 * 
		 * Both methods receive a function and apply it to the computation result, but
		 * the thenCompose (flatMap) method receives a function that returns another
		 * object of the same type. This functional structure allows composing the
		 * instances of these classes as building blocks.
		 */
		CompletableFuture<String> future = completableFuture.thenApply(s -> s + " World");

		System.out.println("Hello World".equalsIgnoreCase(future.get()));
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
		// System.out.println("null".equalsIgnoreCase(future2.get()));
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

	}

}
