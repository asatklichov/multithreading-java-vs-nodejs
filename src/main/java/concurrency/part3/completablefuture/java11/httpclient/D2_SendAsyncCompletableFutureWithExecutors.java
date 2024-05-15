package concurrency.part3.completablefuture.java11.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class D2_SendAsyncCompletableFutureWithExecutors {

	public static void main(String[] args) throws IOException, InterruptedException {

		/**
		 * What is the default thread pool size used in the http client? 
		 * 
		 * As per open jdk
		 * implementation here, by default it uses a newCachedThreadPool.
		 * https://github.com/openjdk/jdk/blob/master/src/java.net.http/share/classes/jdk/internal/net/http/HttpClientImpl.java#L458
		 * 
		 * Cached Thread pool does not have a thread pool size as mentioned in javadoc
		 * quoted below
		 * 
		 * Creates a thread pool that creates new threads as needed, but will reuse
		 * previously constructed threads when they are available. These pools will
		 * typically improve the performance of programs that execute many short-lived
		 * asynchronous tasks. Calls to execute will reuse previously constructed
		 * threads if available. If no existing thread is available, a new thread will
		 * be created and added to the pool. Threads that have not been used for sixty
		 * seconds are terminated and removed from the cache. Thus, a pool that remains
		 * idle for long enough will not consume any resources. Note that pools with
		 * similar properties but different details (for example, timeout parameters)
		 * may be created using ThreadPoolExecutor constructors.
		 * 
		 */
		ExecutorService executor = Executors.newSingleThreadExecutor();

		HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://www.sahet.net")).build();

		CompletableFuture<Void> start = new CompletableFuture<>();

		CompletableFuture<HttpResponse<String>> future = start
				.thenCompose(nil -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()));

		future.thenAcceptAsync(response -> {
			String body = response.body();
			System.out.println("body = " + body.length() + " [" + Thread.currentThread().getName() + "]");
		}, executor).thenRun(() -> System.out.println("Done!"));

		start.complete(null);

		Thread.sleep(500);

		executor.shutdown();
	}
}
