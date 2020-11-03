package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * https://www.baeldung.com/java-completablefuture
 * 
 * For error handling in a chain of asynchronous computation steps, we have to
 * adapt the throw/catch idiom in a similar fashion.
 * 
 * Instead of catching an exception in a syntactic block, the CompletableFuture
 * class allows us to handle it in a special handle method.
 * 
 * This method receives two parameters: a result of a computation (if it
 * finished successfully), and the exception thrown (if some computation step
 * did not complete normally).
 * 
 * https://mincong.io/2020/05/30/exception-handling-in-completable-future/
 * https://medium.com/@knoldus/different-ways-of-handling-exceptions-in-completablefutures-12e4d770353f
 * 
 */

public class F_CompletableFutureHandlingErrors_supplyAsync_completeExeptionally {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		/**
		 * If an error occurs in the original supplyAsync() task, then none of the
		 * thenApply() callbacks will be called and future will be resolved with the
		 * exception occurred. If an error occurs in first thenApply() callback then 2nd
		 * and 3rd callbacks won’t be called and the future will be resolved with the
		 * exception occurred, and so on.
		 * 
		 */
		CompletableFuture.supplyAsync(() -> {
			// Code which might throw an exception
			return "Some result";
		}).thenApply(result -> {
			return "processed result";
		}).thenApply(result -> {
			return "result after further processing";
		}).thenAccept(result -> {
			// do something with the final result
		});

		// exceptionally() calback
		/**
		 * 
		 * 
		 * In method exceptionally(), you only have access to the exception and not the
		 * result.
		 * 
		 * 
		 * The exceptionally() callback gives you a chance to recover from errors
		 * generated from the original Future.
		 * 
		 * Note that, the error will not be propagated further in the callback chain if
		 * you handle it once
		 */
		Integer age = -1;

		CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(() -> {
			if (age < 0) {
				throw new IllegalArgumentException("Age (" + age + ") can not be negative");
			}
			if (age > 18) {
				return "Adult";
			} else {
				return "Child";
			}
		}).exceptionally(ex -> {
			System.out.println("Oops! We have an exception - " + ex.getMessage());
			return "Unknown!";
		});
		System.out.println("Maturity(exceptionally) : " + maturityFuture.get());

		// handle
		/**
		 * more generic method - handle() to recover from exceptions. It is called
		 * whether or not an exception occurs.
		 */
		maturityFuture = CompletableFuture.supplyAsync(() -> {
			if (age < 0) {
				throw new IllegalArgumentException("Age can not be negative");
			}
			if (age > 18) {
				return "Adult";
			} else {
				return "Child";
			}
		}).handle((res, ex) -> {
			if (ex != null) {
				System.out.println("Oops! We have an exception - " + ex.getMessage());
				return "Unknown!";
			}
			return res;
		});

		System.out.println("Maturity(handle) : " + maturityFuture.get());

		// handle example2
		String name = null; // lolo

		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
			if (name == null) {
				throw new RuntimeException("Computation error!");
			}
			return "Hello, " + name;
		}).handle((s, t) -> s != null ? s : "Hello, Stranger!");

		System.out.println("Hello, Stranger!".equalsIgnoreCase(completableFuture.get()));

		// whenComplete
		/**
		 * In method whenComplete(), you have access to the result and exception of the
		 * current completable future as arguments: you can consume them and perform
		 * your desired action. However, you cannot transform the current result or
		 * exception to another result. You cannot return a value like in handle(). This
		 * method is not designed to translate completion outcomes.
		 * 
		 * 
		 * Handle() methods are allowed to return a result (in case of exception a
		 * recovering result) thus they can handle the exception. On the other hand,
		 * whenComplete() methods cannot return a results. So they are used as merely
		 * callbacks that do not interfere in the processing pipeline of
		 * CompletionStages.
		 */
		CompletableFuture<String> cf0 = CompletableFuture.failedFuture(new RuntimeException("Oops"));
		CompletableFuture<String> cf1 = cf0.whenComplete((msg, ex) -> {
			if (ex != null) {
				System.out.println("Exception occurred *** ");
			} else {
				System.out.println(msg);
			}
			/*
			 * Cannot return value because method whenComplete is not designed to translate
			 * completion outcomes. It uses bi-consumer as input parameter: BiConsumer<?
			 * super T, ? super Throwable> action
			 */
		});

		try {
			cf1.join();
		} catch (CompletionException e) {
			System.out.println("Error: " + e.getMessage());
		}

		/**
		 * As an alternative scenario, suppose we want to manually complete the Future
		 * with a value, as in the first example, but also have the ability to complete
		 * it with an exception.
		 * 
		 * The completeExceptionally method is intended for just that. The
		 * completableFuture.get() method in the following example throws an
		 * ExecutionException with a RuntimeException as its cause:
		 */

		// completeExceptionally
		CompletableFuture<String> completableFuture2 = new CompletableFuture<>();
		boolean completeExceptionally = completableFuture2
				.completeExceptionally(new RuntimeException("Calculation failed!"));
		System.out.println(completeExceptionally + "==>" + completableFuture2.get()); // ExecutionException

		/**
		 * In the example above, we could have handled the exception with the handle
		 * method asynchronously, but with the get method we can use the more typical
		 * approach of a synchronous exception processing.
		 */
	}
}

/**
 * Can handle() and completeExceptionally work together? No, you can not do that. 
 * 
 * 
 * You are building a future, piping with an handle (so getting another future)
 * and then completing exceptionally the future returned by the handle.
 * 
 * You should complete exceptionally the inner future itself, instead of the
 * handle.
 * 
 * The point here is that handle returns another future; and you should not
 * complete the "outer" future exceptionally, because doing that will bypass the
 * handling behavior.
 * 
 * Below the code;
 *
 */
class TestHandle {
	BiFunction<String, Throwable, String> handle2 = new BiFunction<String, Throwable, String>() {
		@Override
		public String apply(String s, Throwable t) {
			try {
				throw t.getCause();
			} catch (Throwable e) {
				// I was hoping to record the custom exceptions here;
				System.out.println(e.toString());
			}
			return s != null ? s : "Hello, Stranger!" + t.toString();
		}
	};

	private void testCompleteExceptionally() {
		String name = "Hearen";
		Supplier<String> supplier2 = () -> {
			delay(500L);
			if (name == null) {
				throw new RuntimeException("Computation error!");
			}
			return "Hello, " + name;
		};
		CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier2);

		if (name != null) {
			// when bad things happen, I try to complete it by exception;
			completableFuture.completeExceptionally(new RuntimeException("Calculation failed!"));
		}
		System.out.println(completableFuture.handle(handle2).join());
	}

	public static void main(String[] args) {
		TestHandle th = new TestHandle();
		th.testCompleteExceptionally();
	}

	private static void delay(long milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
		}
	}
}