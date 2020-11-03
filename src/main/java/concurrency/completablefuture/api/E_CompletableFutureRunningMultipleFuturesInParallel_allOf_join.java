package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import concurrency.completablefuture.springboot.asyncmethod.User;

/**
 * https://www.baeldung.com/java-completablefuture
 * 
 * When we need to execute multiple Futures in parallel, we usually want to wait
 * for all of them to execute and then process their combined results.
 * 
 * The CompletableFuture.allOf static method allows to wait for completion of
 * all of the Futures provided as a var-arg:
 * 
 */

public class E_CompletableFutureRunningMultipleFuturesInParallel_allOf_join {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
		CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
		CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");

		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);

		// ...

		combinedFuture.get();

		System.out.println(future1.isDone());
		System.out.println(future2.isDone());
		System.out.println(future3.isDone());
		System.out.println();

		// or
		Void join = CompletableFuture.allOf(future1, future2, future3).join();
		System.out.println();
		/**
		 * Notice that the return type of the CompletableFuture.allOf() is a
		 * CompletableFuture<Void>. The limitation of this method is that it does not
		 * return the combined results of all Futures. Instead, we have to manually get
		 * results from Futures. Fortunately, CompletableFuture.join() method and Java 8
		 * Streams API makes it simple:
		 */

		String combined = Stream.of(future1, future2, future3).map(CompletableFuture::join)
				.collect(Collectors.joining(" "));

		System.out.println("Hello Beautiful World".equalsIgnoreCase(combined));
		System.out.println(combined);
		/**
		 * The CompletableFuture.join() method is similar to the get method, but it
		 * throws an unchecked exception in case the Future does not complete normally.
		 * This makes it possible to use it as a method reference in the Stream.map()
		 * method
		 */

		CompletableFuture<User>[] completableFutures = new CompletableFuture[9];

		String[] names = "PivotalSoftware,CloudFoundry,Spring-Projects,asatklichov,teverett,johjones,LSP,DAP,code4z"
				.split(",");
		for (int i = 0; i < names.length; i++) {
			User user = new User();
			user.setName((names[i].toUpperCase()));
			user.setUrl("");
			completableFutures[i] = CompletableFuture.completedFuture(user);
		}

		// asynchronous lookups
		// CompletableFuture<User> user4 = lookupService.findUser("asatklichov");

		// Wait until completed
		CompletableFuture.allOf(completableFutures).get();
		// CompletableFuture.allOf(user1, user2, 9).join();

		for (CompletableFuture<User> completableFuture : completableFutures) {
			System.out.println(completableFuture.get());
		}

		/// anyOf
		/**
		 * CompletableFuture.anyOf() takes a varargs of Futures and returns
		 * CompletableFuture<Object>. The problem with CompletableFuture.anyOf() is that
		 * if you have CompletableFutures that return results of different types, then
		 * you won’t know the type of your final CompletableFuture.
		 */
		CompletableFuture<String> future1z = CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
			return "Result of Future 1";
		});

		CompletableFuture<String> future2z = CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
			return "Result of Future 2";
		});

		CompletableFuture<String> future3z = CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
			return "Result of Future 3";
		});

		CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(future1z, future2z, future3z);

		System.out.println(anyOfFuture.get()); // Result of Future 2

	}
}
