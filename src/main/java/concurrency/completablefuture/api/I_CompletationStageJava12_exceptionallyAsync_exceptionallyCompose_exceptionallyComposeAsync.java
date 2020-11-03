package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/util/concurrent/CompletionStage.html
 * 
 * https://www.logicbig.com/tutorials/core-java-tutorial/java-12-changes/completion-stage-new-methods.html
 * 
 * 
 */

class Ex1Exceptionally {
	public static void main(String[] args) throws Exception {
		CompletableFuture.supplyAsync(() -> {
			printThreadInfo("division task");
			return 10 / 0;
		}).exceptionally(exception -> {
			printThreadInfo("exceptionally Async");
			System.err.println("exception: " + exception);
			return 1;
		}).thenApply(input -> {
			printThreadInfo("multiply task");
			return input * 3;
		}).thenAccept(System.out::println);

		Thread.sleep(2000);
	}

	private static void printThreadInfo(String desc) {
		System.out.printf("%s, Thread: %s%n", desc, Thread.currentThread().getName());
	}
}

public class I_CompletationStageJava12_exceptionallyAsync_exceptionallyCompose_exceptionallyComposeAsync {
	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		CompletableFuture.supplyAsync(() -> {
			printThreadInfo("division task");
			return 10 / 0;
		}).exceptionallyAsync(exception -> {
			// As seen above, exceptionallyAsync stage is invoked in a new thread
			printThreadInfo("exceptionally Async");
			System.err.println("exception: " + exception);
			return 1;
		}, executor).thenApply(input -> {
			printThreadInfo("multiply task");
			return input * 3;
		}).thenAccept(System.out::println);

		Thread.sleep(2000);// let the stages complete
		executor.shutdown();
	}

	private static void printThreadInfo(String desc) {
		System.out.printf("%s, Thread: %s%n", desc, Thread.currentThread().getName());
	}
}

class Ex3ExceptionallyCompose {
	public static void main(String[] args) {
		CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
			printThreadInfo("supplyAsync");
			return 10 / 0;
		});
		CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> 1);

		CompletableFuture<Integer> exceptionallyCompose = completableFuture.exceptionallyCompose(throwable -> {
			printThreadInfo("exceptionallyCompose");
			System.err.println("exception: " + throwable);
			return completableFuture2;
		});
		exceptionallyCompose.thenApply(i -> i * 3).thenAccept(System.out::println);
	}

	private static void printThreadInfo(String desc) {
		System.out.printf("%s, Thread: %s%n", desc, Thread.currentThread().getName());
	}
}

class Ex4ExceptionallyCompose {
	public static void main(String[] args) {
		CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
			return 10 / 0;
		});
		CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> 1);

		CompletableFuture<Integer> exceptionallyCompose = completableFuture.exceptionallyComposeAsync(throwable -> {
			System.err.println("exception: " + throwable);
			return completableFuture2;
		});
		exceptionallyCompose.thenApply(i -> i * 3).thenAccept(System.out::println);
	}
}

class CompletableFutureExampleComposeAsync {

	public static void main(String[] args) throws Exception {
		printWithThread("Start CompletableFutureExampleComposeAsync...");

		CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
			printWithThread("Inside CF1 supplyAsync");
			if (System.currentTimeMillis() % 2 == 0) {
				throw new RuntimeException("Even time..."); // 50% chance to fail
			}
			return "Winter is Coming!";
		});

		CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
			printWithThread("Inside CF2 supplyAsync");
			return "The Winds of Winter!";
		});

		CompletableFuture<String> excCompose = cf1.exceptionallyComposeAsync(e -> {
			printWithThread("exceptionally: " + e.getMessage());
			return cf2;
		});

		excCompose.thenAcceptAsync(s -> {
			printWithThread("thenAcceptAsync: " + s);
		});

		Thread.sleep(500); // waiting for full response
		printWithThread("...End");
	}

	private static void printWithThread(String desc) {
		System.out.printf("[%s] - %s%n", Thread.currentThread().getName(), desc);
	}
}
