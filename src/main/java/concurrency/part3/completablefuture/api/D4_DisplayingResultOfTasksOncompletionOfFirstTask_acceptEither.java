package concurrency.part3.completablefuture.api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

		// syncronous - non-async way of creating functions
		// so saturated threads may cause fetchUsers2 will run first, because thread1 is
		// busy
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

		// make both async
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
