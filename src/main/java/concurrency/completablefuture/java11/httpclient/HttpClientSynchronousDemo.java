package concurrency.completablefuture.java11.httpclient;

import static concurrency.completablefuture.java11.httpclient.Util.DOMAINS_TXT;
import static concurrency.completablefuture.java11.httpclient.Util.heavySum;
import static concurrency.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Random;

public class HttpClientSynchronousDemo {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {
		Instant start = Instant.now();
		httpClient = HttpClient.newHttpClient();
		Files.lines(Path.of(DOMAINS_TXT)).map(HttpClientSynchronousDemo::validateLink).forEach(System.out::println);
		// CPU intensive calc
		Files.lines(Path.of(DOMAINS_TXT)).map(HttpClientSynchronousDemo::validateLink).forEach(v -> {
			long s = (long) (Math.random() * 10 + 1);
			Random generator = new Random(s);
			heavySum(generator.nextInt());
			System.out.println(v);
		});

		printElapsedTime(start);

	}

	private static String validateLink(String link) {
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(link)).GET().build();

		int statusCode = 0;
		HttpResponse<Void> httpResponse;
		try {
			// discarded response body.
			httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
			statusCode = httpResponse.statusCode();
		} catch (Exception e) {
			System.err.println("Access fail: " + e);
		}

		return 200 == statusCode ? link + " access OK  " : link + " access Failed";
	}

}
