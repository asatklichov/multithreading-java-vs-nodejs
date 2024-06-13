package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

public class I_HttpClientBodyPublishers {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {

		httpClient = HttpClient.newHttpClient();

		/**
		 * POST method used as PAYLOAD as being BODY PUBLISHER
		 * 
		 * also see PUT, PATCH, ...
		 * 
		 * and also method -> method(String methodName, BodyPublisher pub)
		 * 
		 * ofString(body)
		 * 
		 * ofByteArray(Path)
		 * 
		 * ofInputStream
		 * 
		 * noBody
		 * 
		 * 
		 * 
		 * BodyHandlers
		 * 
		 * - ofString -> String
		 * 
		 * - ofByteArray -> byte[]
		 * 
		 * - ofFile(Path) -> as response writes to Path you provided
		 * 
		 * - ofLines -> Stream<String>
		 * 
		 * - discarding -> Void - noBody, just used to check STATUS
		 * 
		 * 
		 * <pre>
		 * HttpRequest.newBuilder().uri(new URI("https://postman-echo.com/post"))
		 * 		.headers("Content-Type", "text/plain;charset=UTF-8")
		 * 		.POST(ofFile(Paths.get("src/test/resources/data.txt"))).build();
		 * </pre>
		 */

		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://sahet.net"))
				.POST(BodyPublishers.ofString("some body infor form")).build();
		//.POST(ofFile(Paths.get("src/test/resources/data.txt"))).build();

		System.out.println("-----------------------------");

		httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
				.thenAccept(System.out::println).join();

	}

}