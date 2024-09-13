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

class ChainingTasksExample1 {

	public static void main(String[] args) throws InterruptedException {

		// simulate id of DB Users
		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			System.out.println("supplyIDs running in " + Thread.currentThread().getName());
			return Arrays.asList(1L, 2L, 3L);
		};

		// this function is synchronous
		Function<List<Long>, List<User>> fetchUsers = ids -> {
			sleep(300);
			System.out.println("fetchUsers is currently running in " + Thread.currentThread().getName());
			return ids.stream().map(User::new).collect(Collectors.toList());
		};

		Consumer<List<User>> displayer = users -> {
			System.out.println("displayer is currently running in " + Thread.currentThread().getName());
			users.forEach(System.out::println);
		};

		// CF - list of longs
		CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);
		// run with same thread
		completableFuture.thenApply(fetchUsers).thenAccept(displayer);

		// without this JVM will be shutdown without giving a chance to run
		Thread.sleep(1_000);
	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}

class ChainingTasksExample2 {

	public static void main(String[] args) throws InterruptedException {

		ExecutorService executor = Executors.newSingleThreadExecutor();

		// simulate id of DB Users
		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			System.out.println("supplyIDs running in " + Thread.currentThread().getName());
			return Arrays.asList(1L, 2L, 3L);
		};

		// this function is synchronous
		Function<List<Long>, List<User>> fetchUsers = ids -> {
			sleep(300);
			System.out.println("fetchUsers is currently running in " + Thread.currentThread().getName());
			return ids.stream().map(User::new).collect(Collectors.toList());
		};

		Consumer<List<User>> displayer = users -> {
			System.out.println("displayer is currently running in " + Thread.currentThread().getName());
			users.forEach(System.out::println);
		};

		// CF - list of longs
		CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);
		// run with different thread
		completableFuture.thenApply(fetchUsers).thenAcceptAsync(displayer, executor);

		// without this JVM will be shutdown without giving a chance to run
		Thread.sleep(1_000);
		executor.shutdown(); 
	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}

class ChainingTasksExample3 {

	public static void main(String[] args) {

		// to run with different thread of different pools
		ExecutorService executor = Executors.newSingleThreadExecutor();

		// simulate id of DB Users
		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			System.out.println("supplyIDs running in " + Thread.currentThread().getName());
			return Arrays.asList(1L, 2L, 3L);
		};

		// this function is Asynchronous NOW
		Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
			sleep(300);
			//in same pool pool FJ - but with different thread
			System.out.println("fetchUsers is currently running in " + Thread.currentThread().getName());
			Supplier<List<User>> userSupplier = () -> {
				System.out.println("userSupplier running in " + Thread.currentThread().getName());
				return ids.stream().map(User::new).collect(Collectors.toList());
			};
			return CompletableFuture.supplyAsync(userSupplier);
		};

		Consumer<List<User>> displayer = users -> {
			System.out.println("displayer in " + Thread.currentThread().getName());
			users.forEach(System.out::println);
		};

		// CF - list of longs
		// run with different threads - executor2, executor1
		CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);
		// this is changed from thenApply() to thenCompose() - Monadic design
		// pattern (as like flatMap case )
		// return CompletableFuture<CompletableFuture<List<User>>> is not applicable for the arguments (Consumer<List<User>>, ExecutorService)
		//completableFuture.thenApply(fetchUsers).thenAcceptAsync(displayer, executor);
		completableFuture.thenCompose(fetchUsers).thenAcceptAsync(displayer, executor);

		sleep(1_000);
		executor.shutdown(); 
	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}

class ChainingTasksExample4 {

	public static void main(String[] args) {

		// to run with different thread of different pools
		ExecutorService executor1 = Executors.newSingleThreadExecutor();
		ExecutorService executor2 = Executors.newSingleThreadExecutor();

		// simulate id of DB Users
		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			System.out.println("supplyIDs running in " + Thread.currentThread().getName());
			return Arrays.asList(1L, 2L, 3L);
		};

		// this function is Asynchronous NOW
		Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
			sleep(300);
			//in different pool now - ExecutorService
			System.out.println("fetchUsers is currently running in " + Thread.currentThread().getName());
			Supplier<List<User>> userSupplier = () -> {
				System.out.println("userSupplier running in " + Thread.currentThread().getName());
				return ids.stream().map(User::new).collect(Collectors.toList());
			};
			return CompletableFuture.supplyAsync(userSupplier, executor2);
		};

		Consumer<List<User>> displayer = users -> {
			System.out.println("displayer in " + Thread.currentThread().getName());
			users.forEach(System.out::println);
		};

		// CF - list of longs
		// run with different threads - executor2, executor1
		CompletableFuture<List<Long>> completableFuture = CompletableFuture.supplyAsync(supplyIDs);
		// this is changed from thenApply() to thenCompose() - Monadic design
				// pattern (as like flatMap case )
				// return CompletableFuture<CompletableFuture<List<User>>> is not applicable for the arguments (Consumer<List<User>>, ExecutorService)
				//completableFuture.thenApply(fetchUsers).thenAcceptAsync(displayer, executor);
		completableFuture.thenComposeAsync(fetchUsers, executor2).thenAcceptAsync(displayer, executor1);

		sleep(1_000);
		executor1.shutdown();
		executor2.shutdown();
	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}
