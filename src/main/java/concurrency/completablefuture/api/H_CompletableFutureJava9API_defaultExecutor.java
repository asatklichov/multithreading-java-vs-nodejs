package concurrency.completablefuture.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * https://openjdk.java.net/jeps/266
 * 
 * https://www.baeldung.com/java-completablefuture
 * 
 * Enhancements to the CompletableFuture API
 * 
 * Time-based enhancements are added that enable a future to complete with a
 * value or exceptionally after a certain duration, see methods orTimeout and
 * completeTimeout. In addition, a complementary Executor returned by the static
 * methods named delayedExecutor allow a task to execute after a certain
 * duration. This may be combined with Executor receiving methods on
 * CompletableFuture to support operations with time-delays.
 * 
 * Subclass enhancements are added making it easier to extend from
 * CompletableFuture, such as to provide a subclass that supports an alternative
 * default executor.
 * 
 * <pre>
 *Java 9 enhances the CompletableFuture API with the following changes:

New factory methods added
Support for delays and timeouts
Improved support for subclassing
and new instance APIs:

Executor defaultExecutor()
CompletableFuture<U> newIncompleteFuture()
CompletableFuture<T> copy()
CompletionStage<T> minimalCompletionStage()
CompletableFuture<T> completeAsync(Supplier<? extends T> supplier, Executor executor)
CompletableFuture<T> completeAsync(Supplier<? extends T> supplier)
CompletableFuture<T> orTimeout(long timeout, TimeUnit unit)
CompletableFuture<T> completeOnTimeout(T value, long timeout, TimeUnit unit)
We also now have a few static utility methods:

Executor delayedExecutor(long delay, TimeUnit unit, Executor executor)
Executor delayedExecutor(long delay, TimeUnit unit)
<U> CompletionStage<U> completedStage(U value)
<U> CompletionStage<U> failedStage(Throwable ex)
<U> CompletableFuture<U> failedFuture(Throwable ex)
Finally, to address timeout, Java 9 has introduced two more new functions:

orTimeout()
completeOnTimeout()
 * </pre>
 */

public class H_CompletableFutureJava9API_defaultExecutor {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		// defaultExecutor
		/**
		 * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/CompletableFuture.html
		 * 
		 * 
		 * Returns the default Executor used for async methods that do not specify an
		 * Executor
		 * 
		 * a new Thread is created to run each task. This may be overridden for
		 * non-static methods in subclasses by defining method defaultExecutor()
		 * 
		 * To simplify monitoring, debugging, and tracking, all generated asynchronous
		 * tasks are instances of the marker interface
		 * CompletableFuture.AsynchronousCompletionTask.
		 */
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
		Executor defaultExecutor = completableFuture.defaultExecutor();
		System.out.println(defaultExecutor);

		CompletableFuture<String> completableFuture2 = MyCompletableFuture.supplyAsync(() -> "Hello",
				Executors.newCachedThreadPool());// ForkJoinPool.commonPool();
		defaultExecutor = completableFuture2.defaultExecutor();
		System.out.println(defaultExecutor);
		System.out.println();

	}
}

class MyCompletableFuture<T> extends CompletableFuture<T> {
	public static <T> CompletableFuture<T> supplyAsync(Supplier<T> s, Executor e) {
		return my(CompletableFuture.supplyAsync(s, e), e);
	}

	private static <T> CompletableFuture<T> my(CompletableFuture<T> f, Executor e) {
		MyCompletableFuture<T> my = new MyCompletableFuture<>(f, e);
		f.whenComplete((v, t) -> {
			if (t != null)
				my.completeExceptionally(t);
			else
				my.complete(v);
		});
		return my;
	}

	private final CompletableFuture<T> baseFuture;
	private final Executor executor;

	MyCompletableFuture(CompletableFuture<T> base, Executor e) {
		baseFuture = base;
		executor = e;
	}

	private <T> CompletableFuture<T> my(CompletableFuture<T> base) {
		return my(base, executor);
	}

	@Override
	public Executor defaultExecutor() {
		ExecutorService customThreadPool = new ThreadPoolExecutor(4, 10, 1, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		return customThreadPool;
	}
	
	public Executor defaultExecutor2() { 
		return CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS, Executors.newCachedThreadPool());
	}
	
	

	@Override
	public CompletableFuture<T> orTimeout(long timeout, TimeUnit unit) {
		//
		return super.orTimeout(timeout, unit);
	}

	@Override
	public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action) {
		return my(baseFuture.acceptEitherAsync(other, action, executor));
	}

	@Override
	public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn) {
		return my(baseFuture.applyToEitherAsync(other, fn, executor));
	}

	@Override
	public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn) {
		return my(baseFuture.handleAsync(fn, executor));
	}

	@Override
	public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
		return my(baseFuture.runAfterBothAsync(other, action, executor));
	}

	@Override
	public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
		return my(baseFuture.runAfterEitherAsync(other, action, executor));
	}

	@Override
	public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action) {
		return my(baseFuture.thenAcceptAsync(action, executor));
	}

	@Override
	public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,
			BiConsumer<? super T, ? super U> action) {
		return my(baseFuture.thenAcceptBothAsync(other, action, executor));
	}

	@Override
	public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
		return my(baseFuture.thenApplyAsync(fn, executor));
	}

	@Override
	public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other,
			BiFunction<? super T, ? super U, ? extends V> fn) {
		return my(baseFuture.thenCombineAsync(other, fn, executor));
	}

	@Override
	public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) {
		return my(baseFuture.thenComposeAsync(fn, executor));
	}

	@Override
	public CompletableFuture<Void> thenRunAsync(Runnable action) {
		return my(baseFuture.thenRunAsync(action, executor));
	}

	@Override
	public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
		return my(baseFuture.whenCompleteAsync(action, executor));
	}
}
