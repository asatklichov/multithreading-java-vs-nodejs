package concurrency.part3.completablefuture.api;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class TriggerWithWhichThread {

	public static void main(String[] args) {

		ExecutorService executor = Executors.newSingleThreadExecutor();

		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			return Arrays.asList(1L, 2L, 3L);
		};

		Function<List<Long>, List<User>> fetchUsers = ids -> {
			sleep(300);
			return ids.stream().map(User::new).collect(Collectors.toList());
		};

		Consumer<List<User>> displayer = users -> {
			System.out.println("In thread " + Thread.currentThread().getName());
			users.forEach(System.out::println);
		};

		//this will never complete unless call complete()
		CompletableFuture<Void> start = new CompletableFuture<>();

		CompletableFuture<List<Long>> supply = start.thenApply(nil -> supplyIDs.get());
		CompletableFuture<List<User>> fetch = supply.thenApply(fetchUsers);
		CompletableFuture<Void> display = fetch.thenAccept(displayer);

		//here we complete CF async-way
		start.completeAsync(() -> null, executor);

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


class TriggerWithWhichThread2 {

	public static void main(String[] args) {
 

		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			return Arrays.asList(1L, 2L, 3L);
		};

		Function<List<Long>, List<User>> fetchUsers = ids -> {
			sleep(300);
			return ids.stream().map(User::new).collect(Collectors.toList());
		};

		Consumer<List<User>> displayer = users -> {
			System.out.println("In thread " + Thread.currentThread().getName());
			users.forEach(System.out::println);
		};

		//this will never complete unless call complete()
		CompletableFuture<Void> start = new CompletableFuture<>();

		CompletableFuture<List<Long>> supply = start.thenApply(nil -> supplyIDs.get());
		CompletableFuture<List<User>> fetch = supply.thenApply(fetchUsers);
		CompletableFuture<Void> display = fetch.thenAccept(displayer);

		//here we complete CF in main thread
		start.complete(null); 
		sleep(1_000); 
	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}

class TriggerWithWhichThread3 {

	public static void main(String[] args) {
 

		Supplier<List<Long>> supplyIDs = () -> {
			sleep(200);
			return Arrays.asList(1L, 2L, 3L);
		};

		Function<List<Long>, List<User>> fetchUsers = ids -> {
			sleep(300);
			return ids.stream().map(User::new).collect(Collectors.toList());
		};

		Consumer<List<User>> displayer = users -> {
			System.out.println("In thread " + Thread.currentThread().getName());
			users.forEach(System.out::println);
		};

		//this will never complete unless call complete()
		CompletableFuture<Void> start = new CompletableFuture<>();

		CompletableFuture<List<Long>> supply = start.thenApply(nil -> supplyIDs.get());
		CompletableFuture<List<User>> fetch = supply.thenApply(fetchUsers);
		CompletableFuture<Void> display = fetch.thenAccept(displayer);

		//here we complete CF in main thread
		//start.completeAsync(null); //NPE		
		start.completeAsync(()-> null); //Supplier can't be NULL
		 
		sleep(1_000); 
	}

	private static void sleep(int timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}

