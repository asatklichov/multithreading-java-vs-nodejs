package concurrency.part3.completablefuture.api;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class NoExceptionExample {

	public static void main(String[] args) {
		Supplier<List<Long>> supplyIDs = () -> {
			Sleep.sleep(200);
			return Arrays.asList(1L, 2L, 3L);
		};

		// define tasks
		Function<List<Long>, List<User>> fetchUsers = ids -> {
			Sleep.sleep(300);
			return ids.stream().map(User::new).collect(Collectors.toList());
		};
		Consumer<List<User>> displayer = users -> users.forEach(System.out::println);
		CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);

		// chain and compose
		CompletableFuture<List<Long>> exception = supply.exceptionally(e -> List.of()); // no exception, returns normal
																						// result not emptyList
		CompletableFuture<List<User>> fetch = exception.thenApply(fetchUsers);
		// CompletableFuture<List<User>> fetch = supply.thenApply(fetchUsers);
		CompletableFuture<Void> display = fetch.thenAccept(displayer);

		List<Long> resultWithJoin = supply.join(); // NO exception
		System.out.println("resultWithJoin = " + resultWithJoin);

		Sleep.sleep(1_000);
		System.out.println("Supply  : isDone=" + supply.isDone() + ", isCompletedExceptionally="
				+ supply.isCompletedExceptionally());
		System.out.println("exception  : isDone=" + supply.isDone() + " isCompletedExceptionally="
				+ exception.isCompletedExceptionally());

		System.out.println("Fetch   : isDone=" + fetch.isDone() + ", isCompletedExceptionally="
				+ fetch.isCompletedExceptionally());
		System.out.println("Display : isDone=" + display.isDone() + ", isCompletedExceptionally="
				+ display.isCompletedExceptionally());
		/**
		 * 
		 * <pre>
		resultWithJoin = [1, 2, 3]
		User [id=1]
		User [id=2]
		User [id=3]
		Supply  : isDone=true, isCompletedExceptionally=false
		exception  : isDone=true exception=false
		Fetch   : isDone=true, isCompletedExceptionally=false
		Display : isDone=true, isCompletedExceptionally=false
		 * </pre>
		 */
	}

}

class YesExceptionExample {

	public static void main(String[] args) {
		Supplier<List<Long>> supplyIDs = () -> {
			Sleep.sleep(200);
			throw new IllegalStateException("No data");
			// return Arrays.asList(1L, 2L, 3L);
		};

		// define tasks
		Function<List<Long>, List<User>> fetchUsers = ids -> {
			Sleep.sleep(300);
			return ids.stream().map(User::new).collect(Collectors.toList());
		};
		Consumer<List<User>> displayer = users -> users.forEach(System.out::println);
		CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);

		// chain and compose
		 CompletableFuture<List<Long>> exception = supply.exceptionally(e ->
		 List.of());  
		// CompletableFuture<List<User>> fetch = exception.thenApply(fetchUsers);
		CompletableFuture<List<User>> fetch = supply.thenApply(fetchUsers);
		CompletableFuture<Void> display = fetch.thenAccept(displayer);

		// List<Long> resultWithJoin = supply.join(); // YES exception
		// System.out.println("resultWithJoin = " + resultWithJoin);

		/**
		 *
		 * <pre>
		When a CompletableFuture completes "normally", it means that the associated computation has completed successfully and a result is available. 
		The isDone() method will return true in this case, and the get() method can be used to retrieve the result.
		
		On the other hand, when a CompletableFuture completes "exceptionally", it means that the associated computation has completed with an exception. This can happen, for example, if an exception is thrown 
		during the computation, or if the computation is cancelled. The isDone() method will return true in this case, but the get() method will throw an ExecutionException wrapping the exception that caused the future to complete exceptionally.
		
		For example, if you have a future that represents a task that could throw an exception during its execution:
		CompletableFuture<Integer> future = 
		CompletableFuture.supplyAsync(() -> {
		    if (someCondition) {
		       throw new IllegalArgumentException("some error");
		    } 
		    return 5;
		    });

		
		In this case, if the someCondition is true, the future will complete exceptionally, throwing an IllegalArgumentException. 
		The isDone() method will return TRUE, but the get() method will throw an ExecutionException wrapping the IllegalArgumentException.
		 * 
		 * </pre>
		 */

		Sleep.sleep(1_000);
		System.out.println("Supply  : isDone=" + supply.isDone() + ", isCompletedExceptionally="
				+ supply.isCompletedExceptionally());

		System.out.println("Fetch   : isDone=" + fetch.isDone() + ", isCompletedExceptionally="
				+ fetch.isCompletedExceptionally());
		System.out.println("Display : isDone=" + display.isDone() + ", isCompletedExceptionally="
				+ display.isCompletedExceptionally());

		/**
		 * As you see, once exception raised, and not handled, all the downstream tasks
		 * completes Exceptionally
		 * 
		 * <pre>
		Supply  : isDone=true, isCompletedExceptionally=true
		Fetch   : isDone=true, isCompletedExceptionally=true
		Display : isDone=true, isCompletedExceptionally=true		 
		 * </pre>
		 */

	}

}

class ExceptionExampleExceptionally {

	public static void main(String[] args) {
		exceptionally(args);
	}

	public static void exceptionally(String[] args) {

		Supplier<List<Long>> supplyIDs = () -> {
			Sleep.sleep(200);
			throw new IllegalStateException("No data");// try disabling
			// return Arrays.asList(1L, 2L, 3L);
		};

		Function<List<Long>, List<User>> fetchUsers = ids -> {
			Sleep.sleep(300);
			return ids.stream().map(User::new).collect(Collectors.toList());
		};

		Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

		CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);

		// chain and compose
		CompletableFuture<List<Long>> exception = supply.exceptionally(e -> List.of());// exception catched, and returns
																						// emptyList

		CompletableFuture<List<User>> fetch = exception.thenApply(fetchUsers);
		CompletableFuture<Void> display = fetch.thenAccept(displayer);

		/**
		 *
		 * <pre>
		 * e.g. Once some exception raised, e.g. IllegalStateException or  SQLException etc.   
		
		Calling join() to get result will thrown a CompletionException 
		get() will throw an ExecutionException
		 *
		 * </pre>
		 */
		List<Long> resultWithJoin = null; // enable below to see exception
		// resultWithJoin = supply.join(); // CompletionException Caused by:
		// java.lang.IllegalStateException:
		System.out.println("resultWithJoin = " + resultWithJoin);

		Sleep.sleep(1_000);
		System.out.println("Supply  : isDone=" + supply.isDone() + ", isCompletedExceptionally="
				+ supply.isCompletedExceptionally());
		System.out.println("exception  : isDone=" + supply.isDone() + ", isCompletedExceptionally="
				+ exception.isCompletedExceptionally());
		System.out.println("Fetch   : isDone=" + fetch.isDone() + ", isCompletedExceptionally="
				+ fetch.isCompletedExceptionally());
		System.out.println("Display : isDone=" + display.isDone() + ", isCompletedExceptionally="
				+ display.isCompletedExceptionally());
		/**
		 * As you see no result provided, exceptionally() swallows once some exception
		 * raised, and covers it with with empty List
		 * 
		 * <pre>
		resultWithJoin = null
		Supply  : isDone=true, isCompletedExceptionally=true
		exception  : isDone=true, isCompletedExceptionally=false
		Fetch   : isDone=true, isCompletedExceptionally=false
		Display : isDone=true, isCompletedExceptionally=false	*
		 * </pre>
		 */

	}

}

class ExceptionExampleWhenComplete {

	public static void main(String[] args) {
		whenComplete(args);
	}

	public static void whenComplete(String[] args) {

		Supplier<List<Long>> supplyIDs = () -> {
			Sleep.sleep(200);
			throw new IllegalStateException("No data"); // try disabling
			// return Arrays.asList(1L, 2L, 3L);
		};

		Function<List<Long>, List<User>> fetchUsers = ids -> {
			Sleep.sleep(300);
			return ids.stream().map(User::new).collect(Collectors.toList());
		};

		Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

		CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);

		/**
		 *
		 * <pre>
		 *  
		whenComplete –  pattern takes a result and the exception if thrown (one of them is NULL).
		They are passed to a BiConsumer
		The returned completable future returns the same thing as the calling one 
		Can not swallow exception, not like exceptionally() method does. It throws exception
		 * </pre>
		 */

		CompletableFuture<List<Long>> exception = supply.whenComplete((ids, e) -> {// result exception duo
			if (ids != null) {
				System.out.println("List is Read  - OK");
			} else {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		});

		CompletableFuture<List<User>> fetch = exception.thenApply(fetchUsers);
		CompletableFuture<Void> display = fetch.thenAccept(displayer);

		Sleep.sleep(1_000);
		System.out.println("Supply  : isDone=" + supply.isDone() + ", isCompletedExceptionally="
				+ supply.isCompletedExceptionally());
		System.out.println("exception  : isDone=" + supply.isDone() + ", isCompletedExceptionally="
				+ exception.isCompletedExceptionally());
		System.out.println("Fetch   : isDone=" + fetch.isDone() + ", isCompletedExceptionally="
				+ fetch.isCompletedExceptionally());
		System.out.println("Display : isDone=" + display.isDone() + ", isCompletedExceptionally="
				+ display.isCompletedExceptionally());
		/**
		 *
		 * <pre>
		 ..  As you see NOt SWALLOWs exception, it is thrown 
		 
		 
		at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
		Caused by: java.lang.IllegalStateException: No data
		at concurrency.part3.completablefuture.api.ExceptionExampleWhenComplete.lambda$0(F_HandlingExceptionInCompletableFuture.java:196)
		at java.base/java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1700)
		... 6 more
		Supply  : isDone=true, isCompletedExceptionally=true
		exception  : isDone=true, isCompletedExceptionally=true
		Fetch   : isDone=true, isCompletedExceptionally=true
		Display : isDone=true, isCompletedExceptionally=true		*
		 * </pre>
		 */

	}

}

class ExceptionExampleHandle {

	public static void main(String[] args) {
		handle(args);
	}

	public static void handle(String[] args) {

		Supplier<List<Long>> supplyIDs = () -> {
			Sleep.sleep(200);
			throw new IllegalStateException("No data");// try disabling
			// return Arrays.asList(1L, 2L, 3L);
		};

		Function<List<Long>, List<User>> fetchUsers = ids -> {
			Sleep.sleep(300);
			return ids.stream().map(User::new).collect(Collectors.toList());
		};

		Consumer<List<User>> displayer = users -> users.forEach(System.out::println);

		CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);

		/**
		 *
		 * <pre>
		
		handle –  pattern takes a result and the exception if thrown (one of them is NULL).
		They are passed to a BiConsumer
		The result is returned by this completable future. 
		Swallows exception – like exceptionally, but can throw as needed
		 *
		 * 
		 * </pre>
		 */

		CompletableFuture<List<Long>> exception = supply.handle((ids, e) -> {
			if (e != null) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				return List.of();
			} else {
				return ids;
			}
		});

		CompletableFuture<List<User>> fetch = exception.thenApply(fetchUsers);
		CompletableFuture<Void> display = fetch.thenAccept(displayer);

		Sleep.sleep(1_000);
		System.out.println("Supply  : isDone=" + supply.isDone() + ", isCompletedExceptionally="
				+ supply.isCompletedExceptionally());
		System.out.println("exception  : isDone=" + supply.isDone() + ", isCompletedExceptionally="
				+ exception.isCompletedExceptionally());
		System.out.println("Fetch   : isDone=" + fetch.isDone() + ", isCompletedExceptionally="
				+ fetch.isCompletedExceptionally());
		System.out.println("Display : isDone=" + display.isDone() + ", isCompletedExceptionally="
				+ display.isCompletedExceptionally());
		/**
		 *
		 * <pre>
		* 
		* actually handle() SWALLOWs Exception, just for demo we printed it out.
		* 
		* 
		.. 
		Caused by: java.lang.IllegalStateException: No data
		at concurrency.part3.completablefuture.api.ExceptionExampleHanle.lambda$0(F_HandlingExceptionInCompletableFuture.java:272)
		at java.base/java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1700)
		... 6 more
		Supply  : isDone=true, isCompletedExceptionally=true
		exception  : isDone=true, isCompletedExceptionally=false
		Fetch   : isDone=true, isCompletedExceptionally=false
		Display : isDone=true, isCompletedExceptionally=false		*
		 * </pre>
		 */

	}

}

class Sleep {
	static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}