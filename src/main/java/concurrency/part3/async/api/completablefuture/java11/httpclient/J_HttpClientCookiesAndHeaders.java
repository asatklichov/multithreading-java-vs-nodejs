package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.io.IOException;
//import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

/**
 * he main class in the java.net package for handling cookies is CookieHandler.
 * There are other helper classes and interfaces such as CookieManager,
 * CookiePolicy, CookieStore, and HttpCookie.
 * 
 * https://www.baeldung.com/cookies-java
 */
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
		 * CookieStore: Default in-memory implementation
		 * 
		 * 
		 * 
		 * Headers
		 * 
		 * e.g. host-name is mandatory, we pass it automatically, ...
		 * 
		 * 
		 * For JSON consumption, USE jACKSON, OR jETTISON, OR org.eclipse.YASSON,
		 * org.glassfish.javax.json
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
		 * FORM data: There is no built-in support to send a POST request with
		 * x-www-form-urlencoded, but it's not that complicated to implement it.
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
		 * COMPRESSIOn As mentioned at the beginning, the Java 11 HTTP client does not
		 * handle compressed responses, nor does it send the Accept-Encoding request
		 * header to request compressed responses by default. If we know that the server
		 * can send back compressed resources, we can request them by adding the
		 * Accept-Encoding header. <code>
		 * var client = HttpClient.newHttpClient();
			var request = HttpRequest.newBuilder()
		            .GET()
		            .header("Accept-Encoding", "gzip")
		            .uri(URI.create("https://localhost:8443/indexWithoutPush"))
		            .build();
		 * </code>
		 * 
		 */

		/**
		 * Defining a CookieHandler With new API and builder, it’s straightforward to
		 * set a CookieHandler for our connection.
		 * 
		 * We can use builder method cookieHandler(CookieHandler cookieHandler) to
		 * define client-specific CookieHandler.
		 * 
		 * Let’s define CookieManager (a concrete implementation of CookieHandler that
		 * separates the storage of cookies from the policy surrounding accepting and
		 * rejecting cookies) that doesn’t allow to accept cookies at all:
		 * 
		 *
		 * cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_NONE)) that doesn’t
		 * allow to accept cookies at all
		 */
		// null - in memory PersistentStore will be used, accept all cookies
 
		CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
		 
	 

		httpClient = HttpClient.newBuilder().cookieHandler(cookieManager).build();

		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://seznam.cz")) // http://jsonvat.com/
				.header("Accept", "text/html").header("content-language", "en-us")
				// .header("Content-Type", "application/json")
				.headers("User-Agent", "Java", "Cache-Control", "no-transform", "Cache-Control", "no-store")
				// to replace header
				.setHeader("Accept", "application/json").POST(BodyPublishers.ofString("some body infor form")).build();

		HttpResponse<Void> res = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());

		System.out.println(res);
		System.out.println(res.body()); // null
		System.out.println(res.statusCode());
		System.out.println(res.headers());
		CookieStore cookieStore = ((CookieManager) httpClient.cookieHandler().get()).getCookieStore();
		System.out.println(cookieStore.getCookies());
		/*
		 * CookieManager allows cookies to be stored, we can access them by checking
		 * CookieHandler from our HttpClient:
		 */
		System.out.println(cookieManager.getCookieStore().getCookies());

		System.out.println("another example for cookie");
		// httpClient =
		// HttpClient.newBuilder().cookieHandler(CookieHandler.getDefault()).build();
		httpRequest = HttpRequest.newBuilder(URI.create("https://google.com")).build();
		HttpResponse<Void> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());

		System.out.println();
		System.out.println(cookieManager.getCookieStore().getURIs());
		System.out.println();
		System.out.println(cookieManager.getCookieStore().getCookies());

	}

	public static void main2(String[] args) throws IOException, InterruptedException {
		String year = "1997"; // args[0];
		String day = "5"; // args[1];
		String session = System.getenv("AOCSESSION");
		System.out.println(session);

		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create("https://adventofcode.com/" + year + "/day/" + day + "/input")).GET().build();
		CookieManager cookieManager = new CookieManager();
		HttpCookie cookie = new HttpCookie("session", session);
		cookieManager.getCookieStore().add(URI.create("https://adventofcode.com"), cookie);
		HttpClient client = HttpClient.newBuilder().cookieHandler(cookieManager).build();

		String body = client.send(req, BodyHandlers.ofString()).body();
		System.out.println(body);
	}

}

/**
 * if we would like our CookieStore implementation to behave like the hard disk
 * and retain the cookies across JVM restarts, we must customize it’s storage
 * and retrieval mechanism.
 */
class PersistentCookieStore implements CookieStore, Runnable {
	private CookieStore store;

	public PersistentCookieStore() {
		store = new CookieManager().getCookieStore();
		// deserialize cookies into store
		Runtime.getRuntime().addShutdownHook(new Thread(this));
	}

	@Override
	public void run() {
		// serialize cookies to persistent storage
	}

	@Override
	public void add(URI uri, HttpCookie cookie) {
		store.add(uri, cookie);

	}

	@Override
	public List<HttpCookie> get(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HttpCookie> getCookies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<URI> getURIs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(URI uri, HttpCookie cookie) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll() {
		// TODO Auto-generated method stub
		return false;
	}

}