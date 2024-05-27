package concurrency.part3.completablefuture.java11.httpclient;

import static concurrency.part3.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

class HttpClientSendSync {

	public static void main(String[] args) throws IOException, InterruptedException {

		Instant start = Instant.now();
		var httpClient = HttpClient.newHttpClient();
		var httpRequest = HttpRequest.newBuilder(URI.create("https://www.baeldung.com/java-flight-recorder-monitoring"))
				.build();
		HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
		System.out.println(httpResponse.body());

		printElapsedTime(start);
	}

}

class HttpClientSendAsync {
	public static void main(String[] args) {

		Instant start = Instant.now();
		var httpClient = HttpClient.newHttpClient();
		var httpRequest = HttpRequest.newBuilder().uri(URI.create("https://www.baeldung.com/java-flight-recorder-monitoring"))
				.build();
		httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenAccept(System.out::println).join();

		printElapsedTime(start);

	}
}

class HttpClientSendAsync2 {
	public static void main(String[] args) {

		var client = HttpClient.newHttpClient();
		var request = HttpRequest.newBuilder().uri(URI.create("https://www.baeldung.com/java-flight-recorder-monitoring")).build();
		
		var request2Way = HttpRequest.newBuilder().
				uri(URI.create("https://www.baeldung.com/java-flight-recorder-monitoring")).build();

		client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(System.out::println).join();

	}
}
