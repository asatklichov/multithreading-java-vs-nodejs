package concurrency.part3.completablefuture.java11.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class D_HttpClientSynchronousDemo {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {

		httpClient = HttpClient.newHttpClient();
		Files.lines(Path.of(Util.DOMAINS_TXT2)).map(D_HttpClientSynchronousDemo::validateLink)
				.forEach(System.out::println);

	}

	private static String validateLink(String link) {
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(link)).GET().build();

		/*
		 * Returns a response body handler that discards the response body.
		 */
		int statusCode = 0;
		HttpResponse<Void> httpResponse;
		try {
			httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
			statusCode = httpResponse.statusCode();
		} catch (IOException | InterruptedException e) {
			System.err.println("Err: " + e);
		}

		return 200 == statusCode ? link + " access OK  " : link + " access Failed";
	}

}
