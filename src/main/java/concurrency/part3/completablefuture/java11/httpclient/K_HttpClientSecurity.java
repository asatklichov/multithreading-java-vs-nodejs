package concurrency.part3.completablefuture.java11.httpclient;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

public class K_HttpClientSecurity {

	public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {

		/**
		 * Security
		 * 
		 * 
		 * A) Provides a default set of root Certification Authority (CA) certificates
		 * in the JDK. Default certiciates {@ link G_RootCertificate} {@ link
		 * _PerformanceImprovementsAndMoreJEPs#RootCertificates}
		 * 
		 * TRY: > cd C:\apps\Java\jdk-11\lib\security > keytool -list -keystore cacerts >  changeit
		 * 
		 * 
		 * B) Self signed certificates, which are not added in above list
		 * 
		 * use keytool (or OpenSSL) to generate
		 * 
		 * 
		 * C) Mutual authentication
		 * 
		 * 
		 */

		/*
		 * -Djavax.net.ssl.trustStore shows Provides a default set of root Certification
		 * Authority (CA) certificates in the JDK  [verisign, CS ...]
		 * 
		 * -Djavax.net.ssl.keyStore keeps client certs (private keys)
		 */
		SSLContext sslCtx = SSLContext.getDefault();

		/*
		 * TLSv1.2 or TLSv1.3
		 * 
		 */
		SSLParameters sslParameters = new SSLParameters(new String[] { "TLSv1.3" },
				new String[] { "TLS_AES_128_GCM_SHA256" });
		HttpClient httpClient = HttpClient.newBuilder().sslContext(sslCtx).sslParameters(sslParameters).build();

	}

}

class HttpClientBasicAuth {

	public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {

		/**
		 * Basic Authentication is a simple way to protect resources on the server. If a
		 * client accesses such resources without any authentication, the server sends
		 * back a status code of 401. The client then re-sends the request with an
		 * authentication header attached to it.
		 * 
		 * 
		 * Security - See Basic Authentication (base64), e.g see servlet one, or Spring
		 * one ...
		 * 
		 * 1. Use Authenticator
		 * 
		 * 2. Proxy - also used for security purposes as well
		 * 
		 * To set a proxy for the request, the builder method proxy is used to provide a
		 * ProxySelector. If the proxy host and port are fixed, the proxy selector can
		 * be hardcoded in the selector:
		 * 
		 * https://golb.hplar.ch/2019/01/java-11-http-client.html
		 * 
		 * <code>
		 * var client = HttpClient.newBuilder()
		          .authenticator(new BasicAuthenticator("user", "password"))
		          .build();
		          
		    or 
		    
		    Authenticator.setDefault(new BasicAuthenticator("user", "password"));
			var client = HttpClient.newBuilder()
		            .authenticator(Authenticator.getDefault())
		            .build();
		 * </code>
		 * 
		 * 
		 */
		Authenticator authenticator = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("admin", "secret".toCharArray());
			}

		};

		// ProxySelector proxySelector = ProxySelector.getDefault();
		ProxySelector proxySelector = ProxySelector.of(new InetSocketAddress("company.proxyserver.com", 8080));
		HttpClient httpClient = HttpClient.newBuilder()
				.authenticator(authenticator)
				.proxy(proxySelector).build();

	}

}


/**
 * 
 * * Root Certificates
 * 
 * <pre>
 * Provides a default set of root Certification Authority (CA) certificates in the JDK.
 * 
 * 
 *JEP 319 will provide a default set of root Certification
 * Authority making OpenJDK builds more appealing to developers. It also aims to
 * reduce the difference between the OpenJDK and Oracle JDK builds. Critical
 * security components such as TLS will now work by default in OpenJDK builds.
 * 
 *
 * The cacerts keystore, which was initially empty so far, is intended to
 * contain a set of root certificates that can be used to establish trust in the
 * certificate chains used by various security protocols.
 * 
 * As a result, critical security components such as TLS didn�t work by default
 * under OpenJDK builds.
 * 
 * With Java 10, Oracle has open-sourced the root certificates in Oracle�s Java
 * SE Root CA program in order to make OpenJDK builds more attractive to
 * developers and to reduce the differences between those builds and Oracle JDK
 * builds.
 * </pre>
 * 
 * The cacerts keystore will be populated with a set of root certificates issued
 * by the CAs of Oracle's Java SE Root CA Program.
 * 
 * TRY: > cd C:\apps\Java\jdk-11\lib\security > keytool -list -keystore cacerts >  changeit
 * 
 * 
 */
class RootCertificateJ10 {

}