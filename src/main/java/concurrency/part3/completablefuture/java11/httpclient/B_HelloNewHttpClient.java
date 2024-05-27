package concurrency.part3.completablefuture.java11.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Desc: HttpClient, some examples are her influenced from the HttpClient course
 * of Sander Mak from pluralsight.
 * 
 * 
 * HttpClient standardized The new HttpClient has been standardized. It is
 * located in the java.net.http package. It is designed to improve the overall
 * performance of sending requests by a client and receiving responses from the
 * server. It also natively supports WebSockets.
 * 
 * <pre>
	 *  - Available since Java 11
	 *  
	 *  - modular, java.net.http
	 *  
	 *  - Replaces HttpURLConnection API
	 *  
	 *  - The new API supports both HTTP/1.1 and HTTP/2, WebSockets
	 *  
	 *  - Sync and Async methods
	 *  
	 *  - In case you do modular-application then use "java.net.http" in your module-info.java
 * 
 * </pre>
 * 
 * /** client with default settings <code>
 * The default settings include:
		prefer HTTP/2
		no connection timeout - Don't confuse with request timeout!
		redirection policy of NEVER
		no cookie handler
		no authenticator
		default thread pool executor
		default proxy selector
		default SSL context
 * </code>
 * 
 * 
 * To configure yourself, use Otherwise use
 * HttpClient.newBuilder().version(Version.HTTP_1_1) ..
 */
class Hello {
	public static void main(String[] args) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://sahet.net")).build();

		ofString(client, httpRequest);
		ofLines(client, httpRequest);
		ofFile(client, httpRequest);
		discarding(client, httpRequest);

	}

	/**
	 * Returns a BodyHandler<String> that returns a BodySubscriber<String> obtained
	 * from BodySubscribers.ofString(Charset).
	 * 
	 * @param client
	 * @param httpRequest
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void ofString(HttpClient client, HttpRequest httpRequest) throws IOException, InterruptedException {
		HttpResponse<String> response = client.send(httpRequest, BodyHandlers.ofString());
		System.out.println(response);
		String body = response.body();
		System.out.println(body);
		int status = response.statusCode();
		System.out.println(status);
		HttpHeaders headers = response.headers();
		System.out.println(headers);
	}

	/**
	 * Returns a response body handler that discards the response body.
	 * 
	 * @param client
	 * @param httpRequest
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void discarding(HttpClient client, HttpRequest httpRequest)
			throws IOException, InterruptedException {
		HttpResponse<Void> voidObj = client.send(httpRequest, HttpResponse.BodyHandlers.discarding());
		/**
		 *
		 * <pre>
		(GET http://sahet.net) 200
		null
		200
		 * </pre>
		 */
		System.out.println(voidObj);
		System.out.println(voidObj.body()); // null
		System.out.println(voidObj.statusCode());
	}

	/**
	 * Returns a BodyHandler<Stream<String>> that returns a
	 * BodySubscriber<Stream<String>> obtainedfrom
	 * BodySubscribers.ofLines(charset).The charset used to decode the response body
	 * bytes isobtained from the HTTP response headers as specified by
	 * ofString(),and lines are delimited in the manner of
	 * BufferedReader.readLine().
	 * 
	 * @param client
	 * @param httpRequest
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void ofLines(HttpClient client, HttpRequest httpRequest) throws IOException, InterruptedException {
		HttpResponse<Stream<String>> response = client.send(httpRequest, BodyHandlers.ofLines());
		Stream<String> lines = response.body();
		lines.forEach(x -> System.out.println(x));
		System.out.println(response);
		System.out.println(response.statusCode());
		System.out.println(response.headers());
	}

	/**
	 * Returns a BodyHandler<Path> that returns a BodySubscriber<Path>.
	 * 
	 * Equivalent to: ofFile(file, CREATE, WRITE)
	 * 
	 * 
	 * @param client
	 * @param httpRequest
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void ofFile(HttpClient client, HttpRequest httpRequest) throws IOException, InterruptedException {
		HttpResponse<Path> response = client.send(httpRequest, BodyHandlers.ofFile(Path.of("sahet-body.txt")));
		Path path = response.body();
		System.out.println("See the output file-path: " + path.getFileName());
		// lines.forEach(x -> System.out.println(x));
		System.out.println(response);
		System.out.println(response.statusCode());
		System.out.println(response.headers());
	}

}