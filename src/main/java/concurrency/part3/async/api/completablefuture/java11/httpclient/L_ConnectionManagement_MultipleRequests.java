package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

class oldWayOfSendingMultipleRequest {

	/**
	 * Multiple requests to a REST server using multiple threads. So, multiple HTTP
	 * requests to the same server via changing the parameter.
	 * 
	 * With HTTP2 no new connection is created every time. See below
	 * 
	 * 
	 * @param args
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		String[] params = { "param1", "param2", "param3", "paramSomeBigNumber" }; // as many

		for (int i = 0; i < params.length; i++) {

			String targetURL = "http://some.server.addr?a=" + params[i];

			HttpURLConnection connection = null;

			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			String urlParameters = null; // todo
			wr.writeBytes(urlParameters);
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			// Do some stuff with this specific http response

			///////////// JAVA 11 way //////////
			/**
			 * With Java 11's HttpClient, this is actually very simple to achieve; all you
			 * need is the following snippet:
			 */
			var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
			for (var param : params) {
				targetURL = "http://some.server.addr?a=" + param;
				var request = HttpRequest.newBuilder().GET().uri(new URI(targetURL)).build();
				client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).whenComplete((response, exception) -> {
					// Handle response/exception here
				});
			}

		}
	}

}

/**
 * The Java 11 HttpClient has an internal connection pool. By default, it is
 * unlimited in size.
 * 
 * 
 * So let’s enable Jetty’s or Tomcat's debug logging by creating a
 * jetty-logging.properties in our classpath:
 * 
 * org.eclipse.jetty.util.log.class=org.eclipse.jetty.util.log.StrErrLog
 * org.eclipse.jetty.LEVEL=DEBUG jetty.logs=logs
 * 
 * When a new connection is created, Jetty logs a “New HTTP Connection” message:
 * 
 * let’s validate that the HttpClient really does make use of an internal
 * connection pool. If there’s a connection pool in use, we’ll only see a single
 * “New HTTP Connection” message.
 * 
 * The JDK 11 ConnectionPool checks the jdk.httpclient.connectionPoolSize system
 * property when initializing and defaults to 0 (unlimited).
 * 
 * 
 * See: Java Networking properties
 * https://docs.oracle.com/en/java/javase/17/core/java-networking.html#GUID-E6C82625-7C02-4AB3-B15D-0DF8A249CD73
 * 
 * 
 * https://www.baeldung.com/java-httpclient-connection-management
 */
class ConnectionManagement {
	public static void main(String[] args) {
		// suppose you have a END-Server
		int serverPort = 1019; // server.port()
		HttpRequest getRequest = HttpRequest.newBuilder().uri(create("http://localhost:" + serverPort + "/first"))
				.build();

	}

	private static URI create(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}

class MultipleRequests {
	public static void main(String[] args) {

		Collection<HttpRequest> paths = null; // Read it ...

		var client = HttpClient.newHttpClient();

		List<HttpRequest> requests = paths.stream().map(path -> "https://localhost:8443" + path).map(URI::create)
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
