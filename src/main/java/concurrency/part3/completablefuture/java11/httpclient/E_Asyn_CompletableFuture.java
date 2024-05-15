package concurrency.part3.completablefuture.java11.httpclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class E_Asyn_CompletableFuture {

	public static void main(String[] args) {

		var client = HttpClient.newHttpClient();
		var request = HttpRequest.newBuilder()
				.uri(URI.create("https://www.baeldung.com/java-flight-recorder-monitoring")).build();

		/**
		 * Difference of Async than Sync is, return TYPE
		 * 
		 * - Also Async not throw any Exception, it handled by CompletableFuture
		 * 
		 * - CompletableFuture holds a task (Future value) that is yet to be completed
		 */
		CompletableFuture<HttpResponse<String>> completableFutureHttpResponse = client.sendAsync(request,
				HttpResponse.BodyHandlers.ofString());

		/**
		 * Non blocking, it gets result via Callbacks, result is again
		 * CompletableFuture<String>
		 * 
		 * thenApply transforms Future to another Future //
		 */
		CompletableFuture<String> completableFutureStringResponse = completableFutureHttpResponse
				.thenApply(HttpResponse::body);

		// to get result use thenAccept which accepts Lambda and executed once ResponseFuture completed
		completableFutureStringResponse.thenAccept(System.out::println).join();

		// or you can continue again asynchronously
		CompletableFuture<Integer> completableFutureIntegerResponse = completableFutureStringResponse
				.thenApply(String::length);
		System.out.println();

		// and get result via thenAccept which accepts Lambda and executed once ResponseFuture completed
		completableFutureIntegerResponse.thenAccept(System.out::println).join();

		System.out.println();
		/**
		 * On returned result you can also use 2 more methods
		 * 
		 * sync method of CompletableFuture<Integer> get and join
		 * 
		 * get throws Checked Exception
		 * 
		 * join - throws Runtime Exception
		 */
		try {
			System.out.println(completableFutureIntegerResponse.get());
			System.out.println(completableFutureIntegerResponse.join());
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("Err: " + e);
		}

	}
}
