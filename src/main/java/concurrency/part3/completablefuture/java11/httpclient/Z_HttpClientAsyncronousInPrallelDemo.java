package concurrency.part3.completablefuture.java11.httpclient;

import static concurrency.part3.completablefuture.java11.httpclient.Util.DOMAINS_TXT;
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

class HttpClientAsyncronousInPrallelDemo {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {

		Instant start = Instant.now();
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
		System.out.println("Async tasks run in parallel by 4 threads .. ");
		httpClient = HttpClient.newBuilder().followRedirects(Redirect.NORMAL).connectTimeout(Duration.ofSeconds(5))
				.executor(Executors.newFixedThreadPool(4)).build();

		List<CompletableFuture<String>> completableFutureStringListResponse = Files.lines(Path.of(DOMAINS_TXT))
				.map(HttpClientAsyncronousInPrallelDemo::validateLink).collect(Collectors.toList());

		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(System.out::println);
		// disable not to enable CPU intensive calc
		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(v -> {
			long s = (long) (Math.random() * 10 + 1);
			Random generator = new Random(s);
			heavySum(generator.nextInt());
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