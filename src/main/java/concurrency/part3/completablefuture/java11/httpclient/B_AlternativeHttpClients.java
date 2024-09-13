package concurrency.part3.completablefuture.java11.httpclient;

import java.io.IOException;

/**
 * As an alternative to HttpURLConnection
 * 
 * 1. use Apache HttpComponents, https://hc.apache.org/httpcomponents-client-ga/
 * 
 * is used long years until Java 11 came with new HttpClient
 * 
 * 2. Square's OkHttp
 * 
 * https://square.github.io/okhttp/
 * 
 * 
 * 3. JAX-RS REST client or Spring RestTemplate
 * 
 * (not only HttpClient but also deals with REST principles and does e.g. JSON
 * conversion )
 *
 */
public class B_AlternativeHttpClients {

	public static void main(String[] args) throws IOException {

		System.out.println("Examples");
		/**
		 * 1. https://mkyong.com/java/apache-httpclient-examples/
		 * 
		 * https://hc.apache.org/httpcomponents-client-ga/
		 * 
		 * 
		 * 2. https://square.github.io/okhttp/
		 * 
		 * 
		 * 3. https://www.baeldung.com/jersey-jax-rs-client
		 * 
		 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
		 * 
		 * https://spring.io/guides/gs/consuming-rest/
		 * 
		 */

	}

}
