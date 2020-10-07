package concurrency.reactive;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * Or just open in browser:  http://localhost:8000/serve  
 *
 */
public class MyHttpClient {

	public static void main(String[] args) throws InterruptedException, IOException {

		HttpClient httpClient = HttpClient.newHttpClient();
		System.out.println(httpClient.version());

		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://localhost:8000/serve")).GET().build();

		CompletableFuture<HttpResponse<String>> completableFutureHttpResponse = httpClient.sendAsync(httpRequest,
				HttpResponse.BodyHandlers.ofString());

		CompletableFuture<String> completableFutureStringResponse = completableFutureHttpResponse
				.thenApply(HttpResponse::body);

		// to get result use thenAccept which accepts Lambda and executed once
		// ResponseFuture completed
		completableFutureStringResponse.thenAccept(System.out::println).join();

		// or you can continue again asynchronously
		CompletableFuture<Integer> completableFutureIntegerResponse = completableFutureStringResponse
				.thenApply(String::length);
		System.out.println();

		completableFutureIntegerResponse.thenAccept(System.out::println).join();

		System.out.println();
	}

}
