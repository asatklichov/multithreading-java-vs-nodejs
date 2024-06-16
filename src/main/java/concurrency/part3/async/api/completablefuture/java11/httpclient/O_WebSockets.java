package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * 
 * WebSocket
 * 
 * Full-duplex communication
 * 
 * Message-based protocol
 * 
 * Text and binary
 * 
 * 
 * WebSocket.Listener: onOpen, onClose, onError, onText, onBinary, onPing, onPong
 * 
 *  <code>
 *  
 * CompletableFuture<WebSocket> wsFuture = 
 * 
 *  HttpClient.newHttpClient().newWebSocketBuilder()
 *  .buildAsync(URI.create("ws://server-url"),
 *  webSocketListener);
 *  
 * </code>
 * 
 * Reactive Streams (akka-streams, RX): Back pressure
 * 
 * Backpressure can commonly occur in this scenario when one server is sending requests to another faster than it can process them
 * 
 * 
 * 
 * 
 * For Spring see: https://www.baeldung.com/websockets-api-java-spring-client
 * 
 * 
 * https://golb.hplar.ch/2019/01/java-11-http-client.html
 * 
 * 
 * First, we need to implement a WebSocket Listener. This is an interface
 * composed of several methods, but all methods are implemented by default
 * methods. Therefore you only have to implement the methods your application
 * needs for its work. In this demo, we listen for the open and close event, and
 * in onText print any text message, the server sends it to us.
 *
 *
 * Every WebSocket connection starts with a HTTP request. Similar to this we
 * have to first create a HttpClient and then call
 * newWebSocketBuilder().buildAsync() to build a WebSocket instance
 * asynchronously. You can only build a WebSocket connection asynchronously.
 * There is no blocking method available.
 * 
 * This example blocks the calling thread with join() until it gets the
 * WebSocket client. Not something you should do in a real application if you
 * are writing your application in a non-blocking fashion.
 */
public class O_WebSockets {

	public static void main(String[] args) throws InterruptedException {
		Listener wsListener = new Listener() {
			@Override
			public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {

				System.out.println("onText: " + data);

				return Listener.super.onText(webSocket, data, last);
			}

			@Override
			public void onOpen(WebSocket webSocket) {
				System.out.println("onOpen");
				Listener.super.onOpen(webSocket);
			}

			@Override
			public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
				System.out.println("onClose: " + statusCode + " " + reason);
				return Listener.super.onClose(webSocket, statusCode, reason);
			}
		};

		var client = HttpClient.newHttpClient();

		WebSocket webSocket = client.newWebSocketBuilder()
				.buildAsync(URI.create("wss://localhost:8443/wsEndpoint"), wsListener).join();
		webSocket.sendText("hello from the client", true);

		TimeUnit.SECONDS.sleep(30);
		webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "ok");
	}

}
