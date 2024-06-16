package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class H_HttpClientConfigDemo {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {

		System.out.println("Async tasks run in parallel by 5 therads .. ");
		httpClient = HttpClient.newBuilder()
				.followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(5))
				//.priority(200) //Sets the default priority for any HTTP/2 requests sent from this client. The value provided must be between 1 and 256(inclusive).
				.executor(Executors.newFixedThreadPool(5)).build();

		List<CompletableFuture<String>> completableFutureStringListResponse = Files
				.lines(Path
						.of(Util.DOMAINS_TXT))
				.map(H_HttpClientConfigDemo::validateLink).collect(Collectors.toList());

		// later these futures executed in parallel, is faster than
		// D_HttpClientSynchronous
		completableFutureStringListResponse.stream()
											.map(CompletableFuture::join)
											.forEach(System.out::println);
	}

	private static CompletableFuture<String> validateLink(String link) {
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(link)).GET().build();

		// including exception handling
		return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding())
				.thenApply(
						asynResult -> 200 == asynResult.statusCode() ? link + " access OK  " : link + " access Failed")
				.exceptionally(e -> " Error occured " + "once accessing to " + link + ", reson is: " + e.getMessage());

	}

	@SuppressWarnings("unused")
	private static CompletableFuture<String> validateLink2(String link) {
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(link)).GET().build();

		// thenApply only works for success case, in case Exception happens then nothing
		return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding()).thenApply(
				asynResult -> 200 == asynResult.statusCode() ? link + " access OK  " : link + " access Failed");

	}

}

class G_HttpClientConfig {

	public static void main(String[] args) throws IOException, InterruptedException {

		/**
		 * Configuration
		 * 
		 * * The default settings include: <code>
				prefer HTTP/2
				no connection timeout - Don't confuse with request timeout!
				redirection policy of NEVER
				no cookie handler
				no authenticator
				default thread pool executor
				default proxy selector
				default SSL context
		 * </code>
		 * 
		 * 
		 * Custome settings e.g. <code>
		 * var client = HttpClient.newBuilder()
		    .authenticator(Authenticator.getDefault())
		    .connectTimeout(Duration.ofSeconds(30))
		    .cookieHandler(CookieHandler.getDefault())
		    .executor(Executors.newFixedThreadPool(2))
		    .followRedirects(Redirect.NEVER)
		    .priority(1) //HTTP/2 priority
		    .proxy(ProxySelector.getDefault())
		    .sslContext(SSLContext.getDefault())
		    .version(Version.HTTP_2)
		    .sslParameters(new SSLParameters())
		    .build();
		 * </code>
		 */

		System.out.println(
				"Once created, an HttpClient instance is immutable, thus automatically thread-safe, and you can send multiple requests with it.");
		HttpClient httpClient = HttpClient.newBuilder()
				// .version(Version.HTTP_1_1)
				// default is HTTP_2, if Server not supports HTTP2 then it fallbacks to HTTP1.1
				.version(Version.HTTP_2).priority(1) // only works with HTTP2 requests, priority range 1-256
				.followRedirects(Redirect.NORMAL) // NEVER, ALWAYS,..
				// establishing TCP conn to server, default un-limited, not you want
				.connectTimeout(Duration.ofSeconds(5))
				/**
				 * sometimes you can share executor among different httpclients, which will help
				 * to reuse threads.
				 * 
				 * Executor exec = Executors.newCachedThreadPool(); HttpClient.newBuilder()
				 * .executor(exec)
				 */
				.executor(Executors.newSingleThreadExecutor()).build();

		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://sahet.net")).build();

		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		System.out.println(httpResponse.body());

	}

}