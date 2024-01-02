package concurrency.completablefuture.java11.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

class MultipleRequests {
	public static void main(String[] args) {

		Collection<HttpRequest> paths = null; // Read it ...

		var client = HttpClient.newHttpClient();

		List<HttpRequest> requests = paths.stream()
				.map(path -> "https://localhost:8443" + path)
				.map(URI::create)
				.map(uri -> HttpRequest.newBuilder(uri).build()).collect(Collectors.toList());

		CompletableFuture<?>[] responses = requests.stream()
				.map(request -> client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body)
						.exceptionally(e -> "Error: " + e.getMessage()).thenAccept(System.out::println))
				.toArray(CompletableFuture<?>[]::new);

	}
}

/**
 * Sending Multiple Requests Using HTTP/2 Running the scenario above but using
 * HTTP/2 (by setting version(Version.HTTP_2) on the created client instance, we
 * can see that a similar latency is achieved but with only one TCP connection
 * being used as shown in the below screenshot, hence, using fewer resources.
 * This is achieved through multiplexing � a key feature that enables multiple
 * requests to be sent concurrently over the same connection, in the form of
 * multiple streams of frames. Each request / response is decomposed into
 * frames, which are sent over a stream. The client is then responsible for
 * assembling the frames into the final response.
 *
 */
class MultipleRequestsHttp11 {

	public static void main(String[] args) throws IOException, InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(6);
		HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
		HttpRequest mainRequest = HttpRequest.newBuilder()
				.uri(URI.create("https://http2.akamai.com/demo/h2_demo_frame.html")).build();
		HttpResponse<String> mainResponse = httpClient.send(mainRequest, BodyHandlers.ofString());
		List<Future<?>> futures = new ArrayList<>();
		String responseBody = null; // TBD
		// For each image resource in the main HTML, send a request on a separate thread
		responseBody.lines().filter(line -> line.trim().startsWith("<img height"))
				.map(line -> line.substring(line.indexOf("src='") + 5, line.indexOf("'/>"))).forEach(image -> {
					Future imgFuture = executor.submit(() -> {
						HttpRequest imgRequest = HttpRequest.newBuilder()
								.uri(URI.create("https://http2.akamai.com" + image)).build();
						try {
							HttpResponse<String> imageResponse = httpClient.send(imgRequest, BodyHandlers.ofString());
							// logger.info("Loaded " + image + ", status code: " +
							// imageResponse.statusCode());
						} catch (IOException | InterruptedException ex) {
							// logger.error("Error during image request for " + image, ex);
						}
					});
					futures.add(imgFuture);
				});
		// Wait for all submitted image loads to be completed
		futures.forEach(f -> {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException ex) {
				// logger.error("Error waiting for image load", ex);
			}
		});
	}

}

/**
 * Sending Multiple Requests Using HTTP/1.1 When loading a web page in a browser
 * using HTTP/1.1, several requests are sent behind the scenes. A request is
 * first sent to retrieve the main HTML of the page, and then several requests
 * are typically needed to retrieve the resources referenced by the HTML, e.g.
 * CSS files, images, and so on. To do this, several TCP connections are created
 * to support the parallel requests, due to a limitation in the protocol where
 * only one request/response can occur on a given connection. However, the
 * number of connections is usually limited (most tests on page loads seem to
 * create six connections). This means that many requests will wait until
 * previous requests are complete before they can be sent. The following example
 * reproduces this scenario by loading a page that links to hundreds of images
 * (taken from an online demo on HTTP/2).
 * 
 * A request is first sent to retrieve the HTML main resource. Then, we parse
 * the result, and for each image in the document, a request is submitted in
 * parallel using an executor with a limited number of threads:
 *
 */
class MultipleRequestsWithHttp2 {

	public static void main(String[] args) throws IOException, InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(6);
		HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
		HttpRequest mainRequest = HttpRequest.newBuilder()
				.uri(URI.create("https://http2.akamai.com/demo/h2_demo_frame.html")).build();
		HttpResponse<String> mainResponse = httpClient.send(mainRequest, BodyHandlers.ofString());
		List<Future<?>> futures = new ArrayList<>();
		String responseBody = null; // TBD
		// For each image resource in the main HTML, send a request on a separate thread
		responseBody.lines().filter(line -> line.trim().startsWith("<img height"))
				.map(line -> line.substring(line.indexOf("src='") + 5, line.indexOf("'/>"))).forEach(image -> {
					Future imgFuture = executor.submit(() -> {
						HttpRequest imgRequest = HttpRequest.newBuilder()
								.uri(URI.create("https://http2.akamai.com" + image)).build();
						try {
							HttpResponse<String> imageResponse = httpClient.send(imgRequest, BodyHandlers.ofString());
							// logger.info("Loaded " + image + ", status code: " +
							// imageResponse.statusCode());
						} catch (IOException | InterruptedException ex) {
							// logger.error("Error during image request for " + image, ex);
						}
					});
					futures.add(imgFuture);
				});
		// Wait for all submitted image loads to be completed
		futures.forEach(f -> {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException ex) {
				// logger.error("Error waiting for image load", ex);
			}
		});
	}

}
