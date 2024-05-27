package concurrency.part3.completablefuture.api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
		CompletableFuture<Void> thenAcceptBoth = CompletableFuture.supplyAsync(() -> "Hello").thenAcceptBoth(
				CompletableFuture.supplyAsync(() -> " World"),
				(s1, s2) -> System.out.println("Accept both: " + s1 + s2));
		System.out.println(thenAcceptBoth.get());

		// acceptEither
		/**
		 * when we just need the faster result of one of them.
		 */
		CompletableFuture<String> newFuture = CompletableFuture
				.completedFuture("HeavySum = " + _CompletableFutureDemo.heavySum());
		CompletableFuture<String> future = CompletableFuture.completedFuture("1");
		newFuture.acceptEither(future, s -> {
			System.out.println("Accept either: " + s);
		});

		// check results
		// System.out.println("Future result>> " + newFuture.get() + " and " +
		// future.get());

	}

}

class DisplayingResultOfTwoTasksInThird_thenAcceptBoth {

	public static void main(String[] args) {

		ExecutorService executor1 = Executors.newSingleThreadExecutor();

		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			return Arrays.asList(1L, 2L, 3L);
		};

		Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
			sleep(250); // 250
			Supplier<List<User>> userSupplier = () -> ids.stream().map(User::new).collect(Collectors.toList());
			return CompletableFuture.supplyAsync(userSupplier);
		};

		Function<List<Long>, CompletableFuture<List<Email>>> fetchEmails = ids -> {
			sleep(350); // 350
			Supplier<List<Email>> userSupplier = () -> ids.stream().map(Email::new).collect(Collectors.toList());
			return CompletableFuture.supplyAsync(userSupplier);
		};

		Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

		CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);

		CompletableFuture<List<User>> userFuture = completableFuture.thenCompose(fetchUsers);	
		CompletableFuture<List<Email>> emailFuture = completableFuture.thenCompose(fetchEmails);

		System.out.println("third completablefuture executer once executed both above completablefuture");

		// The two combined completable futures can return objects of different types
		// bi-consumer
		userFuture.thenAcceptBoth(emailFuture, (users, emails) -> {
			System.out.println(users.size() + " - " + emails.size());
		});

		sleep(1_000);
		executor1.shutdown();
	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}

class DisplayingResultOfTasksOncompletionOfFirstTask_acceptEither1 {

	public static void main(String[] args) {

		ExecutorService executor1 = Executors.newSingleThreadExecutor();

		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			return Arrays.asList(1L, 2L, 3L);
		};

		Function<List<Long>, CompletableFuture<List<User>>> fetchUsers1 = ids -> {
			sleep(150);
			Supplier<List<User>> userSupplier = () -> ids.stream().map(User::new).collect(Collectors.toList());
			System.out.println("fetchUsers1 " + Thread.currentThread().getName());
			return CompletableFuture.supplyAsync(userSupplier);
		};

		Function<List<Long>, CompletableFuture<List<User>>> fetchUsers2 = ids -> {
			sleep(5000);
			System.out.println("fetchUsers2 " + Thread.currentThread().getName());
			Supplier<List<User>> userSupplier = () -> ids.stream().map(User::new).collect(Collectors.toList());
			return CompletableFuture.supplyAsync(userSupplier);
		};

		Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

		CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);

		// synchronous - non-async way of creating functions
		// so saturated threads may cause fetchUsers2 will run first, because thread1 is busy
		CompletableFuture<List<User>> users1 = completableFuture.thenCompose(fetchUsers1);
		CompletableFuture<List<User>> users2 = completableFuture.thenCompose(fetchUsers2);

		users1.thenRun(() -> {
			System.out.println("Users 1");
			System.out.println("Users 1 running in " + Thread.currentThread().getName());
		});
		users2.thenRun(() -> {
			System.out.println("Users 2");
			System.out.println("Users 2 running in " + Thread.currentThread().getName());
		});

		// The two combined completable futures must return objects of the same type
		// (they are exchangeable)
		users1.acceptEither(users2, displayer);

		sleep(6_000);
		executor1.shutdown();

		/**
		 *
		 * <pre>
		Users 2
		User [id=1]
		User [id=2]
		User [id=3]
		Users 1
		 * </pre>
		 */

	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}

class DisplayingResultOfTasksOncompletionOfFirstTask_acceptEither2 {

	public static void main(String[] args) {

		ExecutorService executor1 = Executors.newSingleThreadExecutor();

		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			return Arrays.asList(1L, 2L, 3L);
		};

		Function<List<Long>, CompletableFuture<List<User>>> fetchUsers1 = ids -> {
			sleep(150);
			System.out.println("fetchUsers1 " + Thread.currentThread().getName());
			Supplier<List<User>> userSupplier = () -> ids.stream().map(User::new).collect(Collectors.toList());
			return CompletableFuture.supplyAsync(userSupplier);
		};

		Function<List<Long>, CompletableFuture<List<User>>> fetchUsers2 = ids -> {
			sleep(5000);
			System.out.println("fetchUsers2 " + Thread.currentThread().getName());
			Supplier<List<User>> userSupplier = () -> ids.stream().map(User::new).collect(Collectors.toList());
			return CompletableFuture.supplyAsync(userSupplier);
		};

		Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

		CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);

		// make both ASYNC
		CompletableFuture<List<User>> users1 = completableFuture.thenComposeAsync(fetchUsers1);
		CompletableFuture<List<User>> users2 = completableFuture.thenComposeAsync(fetchUsers2);

		users1.thenRun(() -> {
			System.out.println("Users 1");
			System.out.println("Users 1 running in " + Thread.currentThread().getName());
		});
		users2.thenRun(() -> {
			System.out.println("Users 2");
			System.out.println("Users 2 running in " + Thread.currentThread().getName());
		});

		// The two combined completable futures must return objects of the same type
		// (they are exchangeable)
		users1.acceptEither(users2, displayer);

		/**
		 *
		 * <pre>
		Users 1
		User [id=1]
		User [id=2]
		User [id=3]
		Users 2
		 * </pre>
		 */

		sleep(6_000);
		executor1.shutdown();
	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}