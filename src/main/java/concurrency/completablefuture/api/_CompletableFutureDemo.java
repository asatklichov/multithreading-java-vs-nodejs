package concurrency.completablefuture.api;

import static concurrency.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.nio.channels.IllegalSelectorException;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * More than 60 methods, .. *
 * 
 * https://www.baeldung.com/java-completablefuture
 * https://www.baeldung.com/java-9-completablefuture
 * 
 */
public class _CompletableFutureDemo {
	
 
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// by Parahat C.
		CompletableFuture<String> cf = new CompletableFuture<String>();
		boolean complete = cf.complete("done");
		System.out.println(complete);
		System.out.println(cf.get());
		System.out.println(cf.join());
		
		System.out.println();

		// not completed future, some other thread should complete it
		CompletableFuture<String> notCompletedF = new CompletableFuture<String>();
		// System.out.println(notCompletedF.get()); //blocking call

		// or complete with exceptionally
		notCompletedF.completeExceptionally(new IllegalSelectorException()); // "complete not completed one"
		// System.out.println(notCompletedF.get());

		Instant start = Instant.now();
		// create CompletableFuture, supplyAsyn runs in ForkJoinPool by default, but you
		// can change it
		CompletableFuture<Long> futureResult = CompletableFuture.supplyAsync(() -> {
			return heavySum();
		});

		// continue with other work
		System.out.println("Processing something else ...");

		// now I need the result of sum (use get or join)
		// futureResult.join();
		try {
			futureResult.get();
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("Error " + e.getMessage());
		}
		printElapsedTime(start);

		CompletableFuture<String> futureString = CompletableFuture.supplyAsync(() -> {
			return "Sunny Sunday";
		});

		// thenAccept executed once ResponseFuture completed
		futureString.thenAccept(System.out::println).join();

		/*
		 * thenApply transforms Future to another Future - used to define a callback
		 * which is executed once the supplyAsync finishes
		 * 
		 */
		CompletableFuture<Integer> futureStringLength = futureString.thenApply(String::length);
		futureStringLength.thenAccept(System.out::println).join();
		System.out.println();

		/**
		 * On returned result you can also use 2 more methods
		 * 
		 * sync method of CompletableFuture<Integer> get and join
		 * 
		 * get - throws Checked Exception
		 * 
		 * join - throws Runtime Exception
		 */
		try {
			System.out.println(futureStringLength.get());
			System.out.println(futureStringLength.join());
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("Err: " + e);
		}

		// applyToEither - once I have two results ... aply to aither

		// runAsync
		// thenRun

		// getNow - return immediately

		// If we already know the result of a computation
		// copleteFuture

		// runAsync
		// supplyAsyc - returns value, but not take param

		// complete, obstrudeValue
		// completeExc, obstrudeValueExc
		// completed, obstrudeValueExc

	}

	public static long heavySum() {
		long sum = 0;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			sum += i;
		}
		System.out.println("Sum = " + sum);
		return sum;
	}
}
