package concurrency.part3.completablefuture.java11.httpclient;

import static concurrency.part3.completablefuture.java11.httpclient.Util.DOMAINS_TXT2;
import static concurrency.part3.completablefuture.java11.httpclient.Util.heavySum;
import static concurrency.part3.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Z_HttpClientAsyncronousInPrallelDemoUsingFixedThreadPool {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {

		Instant start = Instant.now();

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
		// in our case, Cached Thread Pool creates more threads - heavy resource, and
		// our task is not a short lived task
		System.out.println(
				"[custom] HttpClient uses newFixedThreadPool(5) good for tasks with unpredictable execution times - 5 threads to be used by asynchronous calls");

		/**
		 * 
		 * Java 11 Http Client default thread pool size
		 * 
		 * What is the default thread pool size used in the http client? As per open jdk
		 * implementation here, by default it uses a newCachedThreadPool. Cached Thread
		 * pool does not have a thread pool size as mentioned in javadoc quoted below.
		 * 
		 * Creates a thread pool that creates new threads as needed, but will reuse
		 * previously constructed threads when they are available. These pools will
		 * typically improve the performance of programs that execute many short-lived
		 * asynchronous tasks. Calls to execute will reuse previously constructed
		 * threads if available. If no existing thread is available, a new thread will
		 * be created and added to the pool. Threads that have not been used for sixty
		 * seconds are terminated and removed from the cache. Thus, a pool that remains
		 * idle for long enough will not consume any resources. Note that pools with
		 * similar properties but different details (for example, timeout parameters)
		 * may be created using ThreadPoolExecutor constructors.
		 * 
		 */
		httpClient = HttpClient.newBuilder().followRedirects(Redirect.NORMAL).connectTimeout(Duration.ofSeconds(5))
				.executor(Executors.newFixedThreadPool(5)).build();

		List<CompletableFuture<String>> completableFutureStringListResponse = Files.lines(Path.of(DOMAINS_TXT2))
				.map(Z_HttpClientAsyncronousInPrallelDemoUsingFixedThreadPool::validateLink).collect(Collectors.toList());

		// completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(System.out::println);
		// disable not to enable CPU intensive calc

		/**
		 * System.out.println("By default Async tasks run via CompletableFuture using
		 * ForkJoin pool - Runtime.getRuntime().availableProcessors() threads");
		 */
		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(v -> {
			long s = (long) (Math.random() * 10 + 1);
			Random generator = new Random(s);
			heavySum(generator.nextInt());// run within same thread
			System.out.println(v);
		});

		printElapsedTime(start);
	}

	private static CompletableFuture<String> validateLink(String link) {
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(link)).GET().build();

		// including exception handling
		/*
		 * ASYNC methods does not throw exception – CompleteableFuture have a
		 * completeExceptionally and then handled by 3-methods….
		 * 
		 */
		return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding())
				.thenApply(
						asynResult -> 200 == asynResult.statusCode() ? link + " access OK  " : link + " access Failed")
				.exceptionally(e -> " Error occured " + "once accessing to " + link + ", reson is: " + e.getMessage());

	}

}