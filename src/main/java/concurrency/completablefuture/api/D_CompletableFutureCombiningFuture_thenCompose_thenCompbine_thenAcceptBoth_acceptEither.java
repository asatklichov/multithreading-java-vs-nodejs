package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * https://www.baeldung.com/java-completablefuture
 * 
 * Combine CompletableFuture instances in a chain of computation steps.
 * thenCompose method to chain two Futures sequentially
 *
 * monadic design pattern. -
 *
 * The result of this chaining is itself a CompletableFuture that allows further
 * chaining and combining. This approach is ubiquitous in functional languages
 * and is often referred to as a monadic design pattern.
 *
 *
 *
 */
public class D_CompletableFutureCombiningFuture_thenCompose_thenCompbine_thenAcceptBoth_acceptEither {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

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
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
				.thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));

		System.out.println("Hello World".equalsIgnoreCase(completableFuture.get()));
		System.out.println(completableFuture.get());
		System.out.println();

		/**
		 * If we want to execute two independent Futures and do something with their
		 * results, we can use the thenCombine method that accepts a Future and a
		 * Function with two arguments to process both results:
		 */
		CompletableFuture<String> completableFuturez = CompletableFuture.supplyAsync(() -> "Hello")
				.thenCombine(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> s1 + " Beautiful " + s2);

		System.out.println("Hello Beautiful  World".equalsIgnoreCase(completableFuturez.get()));
		System.out.println(completableFuturez.get());
		System.out.println();

		/**
		 * A simpler case is when we want to do something with two Futures‘ results, but
		 * don't need to pass any resulting value down a Future chain. The
		 * thenAcceptBoth method is there to help:
		 */
		CompletableFuture<Void> thenAcceptBoth = CompletableFuture.supplyAsync(() -> "Hello")
				.thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> System.out.println(s1 + s2));
		System.out.println(thenAcceptBoth.get());

		// acceptEither
		/**
		 * when we just need the faster result of one of them.
		 */
		CompletableFuture<String> future = CompletableFuture.completedFuture("1");
		CompletableFuture<String> newFuture = CompletableFuture.completedFuture("2");
		future.acceptEither(newFuture, s -> {
			System.out.println("the future which finishes first is: " + s);
		});

		// check results
		System.out.println("Future result>> " + newFuture.get() + " and " + future.get());

	}

}
