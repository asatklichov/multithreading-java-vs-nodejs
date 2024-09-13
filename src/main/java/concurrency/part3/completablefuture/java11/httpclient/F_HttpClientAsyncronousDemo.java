package concurrency.part3.completablefuture.java11.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class F_HttpClientAsyncronousDemo {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {
		
		System.out.println("See H_HttpClientConfigDemo to run this asynx tasks in parallel");

		httpClient = HttpClient.newHttpClient();
		List<CompletableFuture<String>> completableFutureStringListResponse = Files
				.lines(Path
						.of("C:/workspace-eclipse/Java-Features/src/features/in/java11/httpclient/domains.txt"))
				.map(F_HttpClientAsyncronousDemo::validateLink).collect(Collectors.toList());

		// later these futures executed in parallel, is faster than
		// D_HttpClientSynchronous
		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(System.out::println);
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