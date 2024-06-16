package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

public class O_WebSockets2 {

	public static void main(String... args) throws Exception {

		int msgCount = 5;
		CountDownLatch receiveLatch = new CountDownLatch(msgCount);

		CompletableFuture<WebSocket> wsFuture = HttpClient.newHttpClient().newWebSocketBuilder()
				.connectTimeout(Duration.ofSeconds(3)) // 3 seconds
				// public WS endpoint echo-server, to test - ws://echo.websocket.org
				/**
				 * WebSocket Echo Server We run a free very simple endpoint server with support
				 * for websockets and server-sent events (SSE) so that you can test your
				 * websocket and SSE clients easily.
				 * 
				 * https://websocket.org/tools/websocket-echo-server/
				 */

				.buildAsync(URI.create("ws://echo.websocket.org"), new EchoListener(receiveLatch));

		// and send 5 messages to echo server
		// and then get 5 messages back
		wsFuture.thenAccept(webSocket -> {
			webSocket.request(msgCount);
			for (int i = 0; i < msgCount; ++i)
				webSocket.sendText("Message " + i, true);
		});

		// blocking call - be sure server not terminate before getting messages
		// countdown counts to back to 5
		receiveLatch.await();
	}

	/**
	 * Messages coming from EchoListener
	 *
	 */
	static class EchoListener implements WebSocket.Listener {

		CountDownLatch receiveLatch;

		public EchoListener(CountDownLatch receiveLatch) {
			this.receiveLatch = receiveLatch;
		}

		@Override
		public void onOpen(WebSocket webSocket) {
			System.out.println("WebSocket opened");
		}

		@Override
		public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
			System.out.println("onText " + data);
			receiveLatch.countDown();
			return null;
		}

	}
}