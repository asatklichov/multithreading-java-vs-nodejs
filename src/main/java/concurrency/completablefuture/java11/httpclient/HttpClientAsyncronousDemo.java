package concurrency.completablefuture.java11.httpclient;

import static concurrency.completablefuture.java11.httpclient.Util.DOMAINS_TXT2;
import static concurrency.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class HttpClientAsyncronousDemo {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {

		Instant start = Instant.now();
		httpClient = HttpClient.newHttpClient();
		List<CompletableFuture<String>> completableFutureStringListResponse = Files.lines(Path.of(DOMAINS_TXT2))
				.map(HttpClientAsyncronousDemo::validateLink).collect(Collectors.toList());
		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(System.out::println);
		/* disable comment to include CPU intensive calculation */
//		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(v -> {
//			long s = (long) (Math.random() *10 + 1);
//			Random generator = new Random(s);
//			heavySum(generator.nextInt());
//			System.out.println(v);
//		});

		printElapsedTime(start);
		System.out.println(
				"Run this asynx tasks in parallel with HttpClientAsyncronousInPrallelDemo, and compare result");
	}

	private static CompletableFuture<String> validateLink(String link) {
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(link)).GET().build();

		// including exception handling
		return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding())
				.thenApply(
						asynResult -> 200 == asynResult.statusCode() ? link + " access OK  " : link + " access Failed")
				// Resiliency
				.exceptionally(e -> "Error occured once accessing to " + link + ", reson is: " + e.getMessage());

	}

}