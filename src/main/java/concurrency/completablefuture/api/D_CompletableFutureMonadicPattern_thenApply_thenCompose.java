package concurrency.completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * https://www.baeldung.com/java-completablefuture Difference Between
 * thenApply() and thenCompose()
 *
 *
 * thenApply()- Returns a new CompletionStage where the type of the result is
 * based on the argument to the supplied function of thenApply() method.
 * 
 * thenCompose()- Returns a new CompletionStage where the type of the result is
 * same as the type of the previous stage.
 *  
 *  https://www.netjstech.com/2018/11/completablefuture-in-java-with-examples.html
 *
 * * The thenCompose method, together with thenApply, implement basic building
 * blocks of the monadic pattern. They closely relate to the map and flatMap
 * methods of Stream and Optional classes also available in Java 8.
 * 
 * Both methods receive a function and apply it to the computation result, but
 * the thenCompose (flatMap) method receives a function that returns another
 * object of the same type. This functional structure allows composing the
 * instances of these classes as building blocks.
 *
 *
 * thenApply()and thenCompose().Both APIs help chain different CompletableFuture
 * calls,but the usage of these 2 functions is different.
 * 
 * 
 * specifically map becomes thenApplyAsync, and flatMap becomes
 * thenComposeAsync.
 * 
 * thenCompose will then return a future with the result directly, rather than a
 * nested future.
 * 
 * 
 * 
 * Let me try to explain the difference between thenApply and thenCompose with
 * an example.
 * 
 * Let's suppose that we have 2 methods: getUserInfo(int userId) and
 * getUserRating(UserInfo userInfo):
 * 
 * public CompletableFuture<UserInfo> getUserInfo(userId)
 * 
 * public CompletableFuture<UserRating> getUserRating(UserInfo) Both method
 * return types are CompletableFuture.
 * 
 * We want to call getUserInfo() first, and on its completion, call
 * getUserRating() with the resulting UserInfo.
 * 
 * On the completion of getUserInfo() method, let's try both thenApply and
 * thenCompose. The difference is in the return types:
 * 
 * <pre>
 * CompletableFuture<CompletableFuture<UserRating>> f = userInfo.thenApply(this::getUserRating);
 * 
 * CompletableFuture<UserRating> relevanceFuture = userInfo.thenCompose(this::getUserRating);
 * 
 * </pre>
 */

public class D_CompletableFutureMonadicPattern_thenApply_thenCompose {
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		CompletableFuture<Integer> compute = CompletableFuture.supplyAsync(() -> 1);

		// both return a new Completion Stage.

		/**
		 * thenApply()
		 * 
		 * We can use this method to work with a result of the previous call. However, a
		 * key point to remember is that the return type will be combined of all calls.
		 * 
		 * useful when we want to transform the result of a CompletableFuture call
		 */
		CompletableFuture<Integer> finalResult = compute.thenApply(s -> s + 1);
		System.out.println(finalResult.get());

		/**
		 * thenCompose()
		 * 
		 * method is similar to thenApply() in that both return a new Completion Stage.
		 * However, thenCompose() uses the previous stage as the argument. It will
		 * flatten and return a Future with the result directly, rather than a nested
		 * future as we observed in thenApply():
		 */

		CompletableFuture<Integer> finalResult2 = compute
				.thenCompose(D_CompletableFutureMonadicPattern_thenApply_thenCompose::computeAnother);

		System.out.println(finalResult2.get());
		System.out.println();

		System.out
				.println("So if the idea is to chain CompletableFuture methods then it’s better to use thenCompose().");

	}

	static CompletableFuture<Integer> computeAnother(Integer i) {
		return CompletableFuture.supplyAsync(() -> i + 1);
	}

}
