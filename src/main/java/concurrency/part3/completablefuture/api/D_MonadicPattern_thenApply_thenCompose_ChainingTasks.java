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
 * https://www.baeldung.com/java-completablefuture Difference Between
 * thenApply() and thenCompose()
 *
 *
 * thenApply()- Returns a new CompletionStage where the type of the result is
 * based on the argument to the supplied function of thenApply() method.
 * 
 * thenCompose()- Returns a new CompletionStage where the type of the result is
 * same as the type of the previous stage.
 * 
 * https://www.netjstech.com/2018/11/completablefuture-in-java-with-examples.html
 *
 * * The thenCompose method, together with thenApply, implement basic building
 * blocks of the monadic pattern. They closely relate to the map and flatMap
 * methods of Stream and Optional classes also available in Java 8.
 * 
 * Both methods receive a function and apply it to the computation result, but
 * the thenCompose (flatMap) method receives a function that returns another
 * object of the same type. This functional structure allows composing the
 * instances of these classes as building blocks.
 *
 *
 * thenApply()and thenCompose().Both APIs help chain different CompletableFuture
 * calls,but the usage of these 2 functions is different.
 * 
 * 
 * specifically map becomes thenApplyAsync, and flatMap becomes
 * thenComposeAsync.
 * 
 * thenCompose will then return a future with the result directly, rather than a
 * nested future.
 * 
 * 
 * 
 * Let me try to explain the difference between thenApply and thenCompose with
 * an example.
 * 
 * Let's suppose that we have 2 methods: getUserInfo(int userId) and
 * getUserRating(UserInfo userInfo):
 * 
 * public CompletableFuture<UserInfo> getUserInfo(userId)
 * 
 * public CompletableFuture<UserRating> getUserRating(UserInfo) Both method
 * return types are CompletableFuture.
 * 
 * We want to call getUserInfo() first, and on its completion, call
 * getUserRating() with the resulting UserInfo.
 * 
 * On the completion of getUserInfo() method, let's try both thenApply and
 * thenCompose. The difference is in the return types:
 * 
 * <pre>
 * CompletableFuture<CompletableFuture<UserRating>> f = userInfo.thenApply(this::getUserRating);
 * 
 * CompletableFuture<UserRating> relevanceFuture = userInfo.thenCompose(this::getUserRating);
 * 
 * </pre>
 */

class CompletableFutureMonadicPattern_thenApply_thenCompose {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<Integer> compute = CompletableFuture.supplyAsync(() -> 1);

		// both return a new Completion Stage.

		/**
		 * thenApply()
		 * 
		 * We can use this method to work with a result of the previous call. However, a
		 * key point to remember is that the return type will be combined of all calls.
		 * 
		 * useful when we want to transform the result of a CompletableFuture call
		 */
		CompletableFuture<Integer> finalResult = compute.thenApply(s -> s + 1);
		System.out.println(finalResult.get());

		/**
		 * thenCompose()
		 * 
		 * method is similar to thenApply() in that both return a new Completion Stage.
		 * However, thenCompose() uses the previous stage as the argument. It will
		 * flatten and return a Future with the result directly, rather than a nested
		 * future as we observed in thenApply():
		 */

		CompletableFuture<Integer> finalResult2 = compute
				.thenCompose(CompletableFutureMonadicPattern_thenApply_thenCompose::computeAnother);

		System.out.println(finalResult2.get());
		System.out.println();

		System.out
				.println("So if the idea is to chain CompletableFuture methods then it’s better to use thenCompose().");

		// see below
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
		// do not use below, you will get CompletableFuture of CompletableFuture of List
		// of Emails
		CompletableFuture<CompletableFuture<List<Email>>> emailFutureWithThenApply = completableFuture
				.thenApply(fetchEmails);
		CompletableFuture<List<Email>> emailFuture = completableFuture.thenCompose(fetchEmails);
		System.out.println(emailFuture.get());

		sleep(1_000);
		executor1.shutdown();
	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}

	static CompletableFuture<Integer> computeAnother(Integer i) {
		return CompletableFuture.supplyAsync(() -> i + 1);
	}

}

//All tasks run with ForkJoinPool 
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
		// run with SAME thread
		completableFuture.thenApply(fetchUsers).thenAccept(displayer); // ForkJoinPool.commonPool-worker-1

		// without this JVM will be shutdown without giving a chance to run
		Thread.sleep(1_000);

		/**
		 *
		 * <pre>
		*supplyIDs running in ForkJoinPool.commonPool-worker-1
		fetchUsers is currently running in ForkJoinPool.commonPool-worker-1
		displayer is currently running in ForkJoinPool.commonPool-worker-1
		User [id=1]
		User [id=2]
		User [id=3]
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

//Two tasks run with ForkJoinPool, and one runs with newSingleThreadExecutor-pool-1-thread-1  
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
		// run with DIFFERENT thread
		completableFuture.thenApply(fetchUsers).thenAcceptAsync(displayer, executor); // pool-1-thread-1

		// without this JVM will be shutdown without giving a chance to run
		Thread.sleep(1_000);
		executor.shutdown();
		/**
		 *
		 * <pre>
		supplyIDs running in ForkJoinPool.commonPool-worker-1
		fetchUsers is currently running in ForkJoinPool.commonPool-worker-1
		displayer is currently running in pool-1-thread-1
		User [id=1]
		User [id=2]
		User [id=3]
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
			// in same pool pool FJ - but with different thread
			System.out.println("fetchUsers is currently running in " + Thread.currentThread().getName());
			Supplier<List<User>> userSupplier = () -> {
				System.out.println("userSupplier running in " + Thread.currentThread().getName()); // ForkJoinPool.commonPool-worker-2
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
		// return CompletableFuture<CompletableFuture<List<User>>> is not applicable for
		// the arguments (Consumer<List<User>>, ExecutorService)
		// completableFuture.thenApply(fetchUsers).thenAcceptAsync(displayer, executor);
		completableFuture.thenCompose(fetchUsers).thenAcceptAsync(displayer, executor);

		sleep(1_000);
		executor.shutdown();

		/**
		 *
		 * <pre>
		supplyIDs running in ForkJoinPool.commonPool-worker-1
		fetchUsers is currently running in ForkJoinPool.commonPool-worker-1
		userSupplier running in ForkJoinPool.commonPool-worker-2
		displayer in pool-1-thread-1
		User [id=1]
		User [id=2]
		User [id=3]
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
			// in different pool now - ExecutorService
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
		// return CompletableFuture<CompletableFuture<List<User>>> is not applicable for
		// the arguments (Consumer<List<User>>, ExecutorService)
		// completableFuture.thenApply(fetchUsers).thenAcceptAsync(displayer, executor);
		completableFuture.thenComposeAsync(fetchUsers, executor2).thenAcceptAsync(displayer, executor1);

		sleep(1_000);
		executor1.shutdown();
		executor2.shutdown();

		/**
		 *
		 * <pre>
		supplyIDs running in ForkJoinPool.commonPool-worker-1
		fetchUsers is currently running in pool-2-thread-1
		userSupplier running in pool-2-thread-1
		displayer in pool-1-thread-1
		User [id=1]
		User [id=2]
		User [id=3]		*
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
