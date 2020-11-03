package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * https://www.baeldung.com/java-completablefuture
 * 
 * what if we want to skip this boilerplate and simply execute some code
 * asynchronously?
 * 
 * Static methods
 * 
 * - runAsync: Runnable interface run does not allow to return a value
 * 
 * - supplyAsync: Supplier (like Callable) has single method that has no arguments and returns a
 * value of a parameterized type
 * 
 * allow us to create a CompletableFuture instance out of Runnable and Supplier
 * functional types correspondingly.
 *
 */
public class B_CompletableFutureWihEncapsulatedComputationLogic_runAsync_supplyAsync {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Runnable r = () -> System.out.println("Hello");
		CompletableFuture<Void> runAsync = CompletableFuture.runAsync(r);
		CompletableFuture<Void> runAsync2 = CompletableFuture.runAsync(r, Executors.newCachedThreadPool());// ForkJoinPool.commonPool();
		System.out.println(runAsync.get() + " " + runAsync2.get());
		System.out.println();

		CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> "Hello");
		System.out.println("Hello".equalsIgnoreCase(supplyAsync.get()));

		CompletableFuture<String> supplyAsync2 = CompletableFuture.supplyAsync(() -> "Hello", Executors.newCachedThreadPool());
		System.out.println("Hello".equalsIgnoreCase(supplyAsync2.get()));
		System.out.println(supplyAsync.get() + " " + supplyAsync2.get());
	}

}
