package concurrency.part3.async.api.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * https://www.baeldung.com/java-completablefuture-threadpool
 * 
 * We can extend CompletableFuture and override the defaultExecutor(),
 * encapsulating a custom thread pool. As a result, we’ll be able to use the
 * async methods without specifying an Executor, and the functions will be
 * invoked by our thread pool instead of the common ForkJoinPool.
 * 
 */
public class J_ExtendableCompletableFuture {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> name = CompletableFuture.supplyAsync(() -> "DELL 5680");

		CompletableFuture<Integer> nameLength = name.thenApply(value -> {
			printCurrentThread(); // will print "main"
			return value.length();
		});

		CompletableFuture<String> name2 = CustomCompletableFuture.supplyAsync(() -> "DELL 5680 with Backpack");

		nameLength = name2.thenApplyAsync(value -> {
			printCurrentThread(); // will print "Custom-Single-Thread"
			return value.length();
		});
	}

	private static void printCurrentThread() {
		System.out.println(Thread.currentThread().getName());
	}

}

class CustomCompletableFuture<T> extends CompletableFuture<T> {
	private static final Executor executor = Executors
			.newSingleThreadExecutor(runnable -> new Thread(runnable, "Custom-Single-Thread"));

	@Override
	public Executor defaultExecutor() {
		return executor;
	}

	public static <TYPE> CustomCompletableFuture<TYPE> supplyAsync(Supplier<TYPE> supplier) {
		CustomCompletableFuture<TYPE> future = new CustomCompletableFuture<>();
		executor.execute(() -> {
			try {
				future.complete(supplier.get());
			} catch (Exception ex) {
				future.completeExceptionally(ex);
			}
		});
		return future;
	}
}