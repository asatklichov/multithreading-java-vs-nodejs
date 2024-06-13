package concurrency.part3.async.api.completablefuture;

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
 * - supplyAsync: Supplier (like Callable) has single method that has no
 * arguments and returns a value of a parameterized type
 * 
 * allow us to create a CompletableFuture instance out of Runnable and Supplier
 * functional types correspondingly.
 *
 */
public class B_CompletableFutureWihEncapsulatedComputationLogic_runAsync_supplyAsync {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		/**
		 * runAsync() is used to create a new CompletableFuture instance and schedule a
		 * task to run asynchronously. It takes in a Runnable as its argument, which is
		 * a functional interface that takes in no argument and returns no result. This
		 * means that the task that is run asynchronously does not have any input and
		 * does not produce any output.
		 * 
		 * So in a nutshell, runAsync() is used when you want to run a task
		 * asynchronously and do not need to produce any result,
		 * 
		 * while  supplyAsync() is used when you want to run a task asynchronously and
		 * produce a result which can be used to complete the
		 * returned CompletableFuture instance. The supplyAsync() method also accepts an
		 * Executor as an optional argument, this allows you to specify the Executor on
		 * which the task should be executed and thus allowing you to control the thread
		 * pool on which the task is run.
		 * 
		 */
		Runnable r = () -> System.out.println("Hello");// ForkJoinPool.commonPool();
		CompletableFuture<Void> runAsync = CompletableFuture.runAsync(r);// default Fork/Join
		CompletableFuture<Void> runAsync2 = CompletableFuture.runAsync(r, Executors.newSingleThreadExecutor());
		System.out.println(runAsync.get() + " " + runAsync2.get());
		System.out.println();

		CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> "Hello");
		System.out.println("Hello".equalsIgnoreCase(supplyAsync.get()));

		CompletableFuture<String> supplyAsync2 = CompletableFuture.supplyAsync(() -> "Hello",
				Executors.newCachedThreadPool());
		System.out.println("Hello".equalsIgnoreCase(supplyAsync2.get()));
		System.out.println(supplyAsync.get() + " " + supplyAsync2.get());
	}

}
