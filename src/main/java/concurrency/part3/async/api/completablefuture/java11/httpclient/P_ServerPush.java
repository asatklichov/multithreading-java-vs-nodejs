package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse.PushPromiseHandler;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * It is really the multiplexing feature of HTTP/2 that allows us to forget
 * about resource bundling. For each resource, the server sends a special
 * request, known as a push promise to the client.
 * 
 * Push promises received, if any, are handled by the given PushPromiseHandler.
 * A null valued PushPromiseHandler rejects any push promises.
 * 
 * HTTP/2 Server Push is a new way to send resources from a server to the
 * client. Traditionally a browser requests an HTML page parses the code and
 * sends additional requests for all the referenced resources on the page (JS,
 * CSS, images, ...)
 * 
 * With HTTP/2 Server Push, a server not only sends back the HTML it also sends
 * back all the referenced resources that the browser needs to display the page.
 * 
 * In the following example the /indexWithPush endpoint not only sends back an
 * HTML page but also a picture that is referenced on the page.
 * 
 * To handle these resources, an application has to implement the
 * PushPromiseHandler interface and then pass an instance of this implementation
 * as the third argument to the send() or sendAsync() method.
 *
 */
public class P_ServerPush {

	public static void main(String[] args) {

		HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("https://localhost:8443/indexWithPush"))
				.build();

		var asyncRequests = new CopyOnWriteArrayList<CompletableFuture<Void>>();

		PushPromiseHandler<byte[]> pph = (initial, pushRequest, acceptor) -> {
			CompletableFuture<Void> cf = acceptor.apply(BodyHandlers.ofByteArray()).thenAccept(response -> {
				System.out.println("Got pushed resource: " + response.uri());
				System.out.println("Body: " + response.body());
			});
			asyncRequests.add(cf);
		};

		HttpClient httpClient = HttpClient.newHttpClient();
		httpClient.sendAsync(request, BodyHandlers.ofByteArray(), pph).thenApply(HttpResponse::body)
				.thenAccept(System.out::println).join();
	}

}
