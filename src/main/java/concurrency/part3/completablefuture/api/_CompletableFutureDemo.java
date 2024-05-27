package concurrency.part3.completablefuture.api;

import java.nio.channels.IllegalSelectorException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

//https://forums.oracle.com/ords/apexds/post/completablefuture-for-asynchronous-programming-in-java-8-8539

class CompletableFutureNotRun {

	public static void main(String[] args) {
		// task is launched by ForkJoin thread - which is DAEMON, so not prevents
		CompletableFuture.runAsync(() -> System.out.println("I am running async way "));
		// you do not see anything, problem is
		// once we finish async-call, main-thread dies already, jvm even not gives a
		// chance to run it
	}
}

class CompletableFutureRun {

	public static void main(String[] args) throws InterruptedException {

		// similar experience with core thread APi, where you have use join()
		// task is launched by ForkJoin thread - which is DAEMON, so not prevents
		// JVM exiting, so add a SLEEP to able to give a chance
		CompletableFuture.runAsync(() -> System.out.println("I am running async way "));
		Thread.sleep(2000);
	}
}

class CompletableFutureBlocks {

	public static void main(String[] args) throws InterruptedException {

		ExecutorService es = Executors.newSingleThreadExecutor();
		Runnable task = () -> System.out.println("I am running async way ");
		CompletableFuture.runAsync(task, es);
		Thread.sleep(2000);

		// as you see, main-thread is done
		// but JVM is still blocked
		// because ExecutorService threads are still living, blocking
	}
}

class CompletableFutureNotBlocks {

	public static void main(String[] args) throws InterruptedException {

		ExecutorService es = Executors.newSingleThreadExecutor();
		Runnable task = () -> System.out.println("I am running async way ");
		CompletableFuture.runAsync(task, es);
		Thread.sleep(2000);

		es.shutdown();
	}
}

class SadaCompletableFuture1 {

	public static void main(String[] args) {

		// create CompletableFuture, not makes itself sense, it is empty
		CompletableFuture<Void> cf = new CompletableFuture<>();
		Void nil = cf.join(); // JVM blocks

		// you can complete CF by complete() or
		// see below how to complete this task asyncronously
	}
}

class SadaCompletableFuture2 {

	public static void main(String[] args) {

		// CompletableFuture is empty
		CompletableFuture<Void> cf = new CompletableFuture<>();

		Runnable task = () -> {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
			// can be completed here
			// this can be helpful later to deal with complex chain of async-tasks
			cf.complete(null);
		};
		CompletableFuture.runAsync(task);

		Void nil = cf.join(); // not blocked anymore
		System.out.println("Gutardyk - We are done");
	}
}

class CompleteAndObtrudeValueDemo { //CompletableFutureWithSupplier 
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newSingleThreadExecutor();

		Supplier<String> supplier = () -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return Thread.currentThread().getName();
		};

		// try above, disabling below line
		supplier = () -> Thread.currentThread().getName();

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executor);

		System.out.println("\n ---- complete() demo ----");
		boolean completedOk = completableFuture.complete("complete: oran uzyn - too long!");
		if (completedOk) {
			System.out.println("completed? = " + completedOk + ", isDone()=" + completableFuture.isDone());
		}
		/**
		 * The join() method doesn't throw checked exceptions.
		 * 
		 * get() requires you to catch checked exceptions. You should notice the
		 * difference when you change from get() to join(), as you will immediately get
		 * a compiler error saying that
		 * neither InterruptedException nor ExecutionException are thrown in
		 * the try block
		 * 
		 * Well get exists, because CompletableFuture implements the Future interface
		 * which mandates it. join() most likely has been introduced, to avoid needing
		 * to catch checked exceptions in lambda expressions when combining futures. In
		 * all other use cases, feel free to use whatever you prefer
		 * 
		 */
		String string = completableFuture.get();
		System.out.println("Result get() = " + string);
		string = completableFuture.join();
		System.out.println("Result join() = " + string);

		/**
		 * complete() – forces to return value: checks if task is done, if done then
		 * nothing just get(), join() returns result value computed already, otherwise
		 * completes the TASK and sets given value as a result value so get(), join()
		 * will return it
		 * 
		 * obtrudeValue() - like complete() but in both cases sets given value as a
		 * result value so get(), join() will return it
		 * 
		 */
		System.out.println("\n ---- obtrudeValue() demo ----");
		completableFuture.obtrudeValue("obtrudeValue: oran uzyn - too long!");// void
		// Returns true if completed in any fashion: normally,exceptionally, or via
		// cancellation
		System.out.println("isDone()=" + completableFuture.isDone());
		string = completableFuture.get();
		System.out.println("Result get() = " + string);

		string = completableFuture.join();
		System.out.println("Result join()  = " + string);

		/**
		 * complete() – forces to return value: checks if task is done, if done then
		 * nothing just get(), join() returns result value computed already, * otherwise
		 * completes the TASK and sets given value as a result value so get(), join()
		 * will return it
		 * 
		 * obtrudeValue() - like complete() but in both cases sets given value as a
		 * result value so get(), join() will return it
		 * 
		 * completeExceptionally() – check if done, if yes just return computed result,
		 * if NOT then forces to complete task and throws exception
		 * 
		 * obtrudeExceptionally() – like completeExceptionally() but in both case forces
		 * to complete task and throws exception
		 * 
		 */

		executor.shutdown();
	}
}

/**
 * completeExceptionally() – check if done, if yes just return computed result,
 * if NOT then forces to complete task and throws exception
 *
 */
class CompleteExceptionallyDemo {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		System.out.println("\n ---completeExceptionally() demo --- ");
		CompletableFuture<String> complete = new CompletableFuture<String>();
		// complete and then complete exceptionally
		complete.complete("I am done");
		complete.completeExceptionally(new IllegalSelectorException()); // no affet here
		System.out.println(complete.get());

		// or complete with exceptionally
		CompletableFuture<String> notCompletedF = new CompletableFuture<String>();
		// System.out.println(notCompletedF.get()); //blocking call
		notCompletedF.completeExceptionally(new IllegalSelectorException()); // "complete not completed one"
		System.out.println(notCompletedF.get());

	}
}

/**
 * 
 * obtrudeExceptionally() – like completeExceptionally() but in both case forces
 * to complete task and throws exception
 *
 */
class ObtrudeExceptionDemo {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		System.out.println("\n ---obtrudeExceptionally() demo --- ");
		CompletableFuture<String> complete = new CompletableFuture<String>();
		// complete and then obtrudeException
		complete.complete("I am done");
		complete.obtrudeException(new AssertionError("--> obtrudeException")); // obtrudeException
		System.out.println(complete.get());

	}
}

/**
 * More than 60 methods, .. *
 * 
 * https://www.baeldung.com/java-completablefuture
 * https://www.baeldung.com/java-9-completablefuture
 * 
 */
public class _CompletableFutureDemo {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<String> cf = new CompletableFuture<String>();
		boolean complete = cf.complete("done-If not already completed, sets the value returned by get()");
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

	public static void printElapsedTime(Instant start) {
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toMillis();

		System.out.println("\nElapsed time: " + timeElapsed + " ms");
	}
}
