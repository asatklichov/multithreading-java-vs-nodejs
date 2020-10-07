package concurrency.reactive;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * My custom HTTP Server
 *
 */
public class MySimpleHttpServer {

	public static void main(String[] args) throws InterruptedException, IOException {
		HttpHandler handler = h -> {
			String body = "My Simple HTTP Simple server";
			h.sendResponseHeaders(200, body.length());
			try (OutputStream os = h.getResponseBody()) {
				os.write(body.getBytes());
			}
		};

		HttpServer mySimpleHttpServer = HttpServer.create(new InetSocketAddress(8000), 0);
		mySimpleHttpServer.createContext("/serve", handler);
		mySimpleHttpServer.start();
	}

}
