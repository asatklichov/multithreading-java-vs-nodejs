package concurrency.part3.async.api.completablefuture.java11.httpclient;

import static concurrency.part3.async.api.completablefuture.java11.httpclient.Util.DOMAINS_TXT2;
import static concurrency.part3.async.api.completablefuture.java11.httpclient.Util.heavySum;
import static concurrency.part3.async.api.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Z_HttpClientAsyncronousDemoUsingCachedThreadPoolAndParallelCF {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {

		/**
		 * Cached Thread Pool — keeps creating threads starting with 0 and max to 2³¹-1.
		 * Idea behind is, the task should not wait (SynchronousQueue) for execution.
		 * Only limitation is system resources may not be available. Removes idle
		 * threads after 1-min. Good for short-lived tasks.
		 * 
		 * Fixed Thread Pool — keeps adding more tasks to the queue
		 * (LinkedBlockingQueue) in case all threads (fixed number) are busy. Good to
		 * control resource consumption, stack size, and tasks with unpredictable
		 * execution times.
		 * 
		 * ForkJoinPool — ForkJoin provides parallel mechanism for CPU intensive tasks,
		 * uses work stealing alg. Has threads be default equals to
		 * Runtime.getRuntime().availableProcessors()
		 */
		System.out.println(
				"By default, the HttpClient uses executor java.util.concurrent.Executors.newCachedThreadPool() - threads to be used by asynchronous calls");

		ExecutorService executor = Executors.newFixedThreadPool(5);

		Instant start = Instant.now();

		httpClient = HttpClient.newHttpClient();

		List<CompletableFuture<String>> completableFutureStringListResponse = Files.lines(Path.of(DOMAINS_TXT2))
				.map(Z_HttpClientAsyncronousDemoUsingCachedThreadPoolAndParallelCF::validateLink).collect(Collectors.toList());

		/*
		 * By default Async tasks run via CompletableFuture using ForkJoin pool -
		 * Runtime.getRuntime().availableProcessors() threads
		 */
		System.out.println(
				"[custom] Async tasks run via CompletableFuture using Executors.newFixedThreadPool(5) - to run heavySum() parallely on different thread");

		for (CompletableFuture<String> completableFuture : completableFutureStringListResponse) {
			completableFuture.thenAcceptAsync(response -> {
				long s = (long) (Math.random() * 10 + 1);
				Random generator = new Random(s);
				heavySum(generator.nextInt()); // run on different thread asynchronously
				System.out.println(response);
			}, executor).thenRun(() -> System.out.println(""));// done
		}

		printElapsedTime(start);
		System.out.println(
				"Run this async tasks in parallel with HttpClientAsyncronousInPrallelDemo, and compare result");
	}

	private static CompletableFuture<String> validateLink(String link) {
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(link)).GET().build();

		// including exception handling, also discarding body
		return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding())
				.thenApply(
						asynResult -> 200 == asynResult.statusCode() ? link + " access OK  " : link + " access Failed")
				// Resiliency
				/*
				 * ASYNC methods does not throw exception – CompleteableFuture have a
				 * completeExceptionally and then handled by 3-methods….
				 * 
				 */
				.exceptionally(e -> "Error occured once accessing to " + link + ", reson is: " + e.getMessage());

	}

}