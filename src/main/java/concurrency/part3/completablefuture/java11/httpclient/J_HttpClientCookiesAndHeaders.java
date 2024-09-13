package concurrency.part3.completablefuture.java11.httpclient;

import java.io.IOException;
//import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

public class J_HttpClientCookiesAndHeaders {

	private static HttpClient httpClient;

	public static void main(String[] args) throws IOException, InterruptedException {
		/**
		 * Cookies - to manage state, token, etc..
		 * 
		 * cookies are persistent
		 * 
		 * HttpClient.Builder::cookieHandler
		 * 
		 * CookieStore:  Default in-memory  implementation
		 * 
		 * 
		 * 
		 * Headers
		 * 
		 * e.g. host-name is mandatory, we pass it automatically, ...
		 * 
		 * 
		 * For JSON consumption, USE jACKSON, OR jETTISON, OR org.eclipse.YASSON, org.glassfish.javax.json
		 * 
		 * <code>
		 * Jsonb jsonb = JsonbBuilder.create();
		    var client = HttpClient.newHttpClient();
		
		    User user = new User(2, "Mr. Client");
		    var request = HttpRequest.newBuilder()
		                    .POST(BodyPublishers.ofString(jsonb.toJson(user)))
		                    .uri(URI.create("https://localhost:8443/saveUser"))
		                    .header("Content-Type", "application/json")
		                    .build();

    	HttpResponse<Void> response = client.send(request, BodyHandlers.discarding());
		 * </code>
		 * 
		 * 
		 * FORM data: There is no built-in support to send a POST request with x-www-form-urlencoded,
		 * but it's not that complicated to implement it. 
		 * 
		 * <code>
		 * var client = HttpClient.newHttpClient();

		    Map<Object, Object> data = new HashMap<>();
		    data.put("id", 1);
		    data.put("name", "a name");
		    data.put("ts", System.currentTimeMillis());
		
		    var request = HttpRequest.newBuilder()
		                    .POST(ofFormData(data))
		                    .uri(URI.create("https://localhost:8443/formdata"))
		                    .header("Content-Type", "application/x-www-form-urlencoded")
		                    .build();
		
		    return client.sendAsync(request, BodyHandlers.ofString())
		                    .thenApply(HttpResponse::body)
		                    .exceptionally(e -> "Error: " + e.getMessage())
		                    .thenAccept(System.out::println);
		 * </code>
		 * 
		 * 
		 * 
		 * COMPRESSIOn
		 * As mentioned at the beginning, the Java 11 HTTP client does not handle compressed responses, nor does it send the Accept-Encoding request header to request compressed responses by default.
		 * If we know that the server can send back compressed resources, we can request them by adding the Accept-Encoding header.
		 * <code>
		 * var client = HttpClient.newHttpClient();
    		var request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept-Encoding", "gzip")
                    .uri(URI.create("https://localhost:8443/indexWithoutPush"))
                    .build();
		 * </code>
		 * 
		 */

		// null - in memory PersistentStore will be used, accept all cookies
		CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		httpClient = HttpClient.newBuilder().cookieHandler(cookieManager).build();

		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://jsonvat.com/"))
				.header("Accept", "text/html").header("content-language", "en-us")
				//.header("Content-Type", "application/json")
				.headers("User-Agent", "Java", "Cache-Control", "no-transform", "Cache-Control", "no-store")
				// to replace header
				.setHeader("Accept", "application/json")
				.POST(BodyPublishers.ofString("some body infor form"))
				.build();

		httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
		System.out.println(cookieManager.getCookieStore().getCookies());

		System.out.println("another example for cookie");
		//httpClient = HttpClient.newBuilder().cookieHandler(CookieHandler.getDefault()).build();
		httpRequest = HttpRequest.newBuilder(URI.create("https://google.com")).build();
		httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());

		System.out.println(cookieManager.getCookieStore().getURIs());
		System.out.println(cookieManager.getCookieStore().getCookies());

	}

}