package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

//https://docs.oracle.com/javase/tutorial/networking/index.html
public class AnInto_URLConnetion {

	public static void main(String[] args) {

		// https://docs.oracle.com/javase/tutorial/networking/overview/networking.html
		// https://docs.oracle.com/javase/tutorial/networking/urls/urlInfo.html

		System.out.println("\nParsing URLs");
		/**
		 * <pre>
		 * Parsing a URL
		The URL class provides several methods that let you query URL objects. You can get the protocol, authority, host name, port number, path, query, 
		filename, and reference from a URL using these accessor methods:
		
		
		getProtocol
		Returns the protocol identifier component of the URL.
		getAuthority
		Returns the authority component of the URL.
		getHost
		Returns the host name component of the URL.
		getPort
		Returns the port number component of the URL. The getPort method returns an integer that is the port number. If the port is not set, getPort returns -1.
		getPath
		Returns the path component of this URL.
		getQuery
		Returns the query component of this URL.
		getFile
		Returns the filename component of the URL. The getFile method returns the same as getPath, plus the concatenation of the value of getQuery, if any.
		getRef
		Returns the reference component of the URL.
		 * </pre>
		 */

		System.out.println("\n Reading from URL");
		/**
		 * When you run the program, you should see, scrolling by in your command
		 * window, the HTML commands and textual content from the HTML file located at
		 * http://www.oracle.com/. Alternatively, the program might hang or you might
		 * see an exception stack trace. If either of the latter two events occurs, you
		 * may have to set the proxy host so that the program can find the Oracle
		 * server.
		 */

		System.out.println("\nConnecting to a URL");
		/**
		 * After you've successfully created a URL object, you can call the URL object's
		 * openConnection method to get a URLConnection object, or one of its protocol
		 * specific subclasses, for example java.net.HttpURLConnection
		 * 
		 * <pre>
		 * try {
		 * 	URL myURL = new URL("http://example.com/");
		 * 	URLConnection myURLConnection = myURL.openConnection();
		 * 	myURLConnection.connect();
		 * } catch (MalformedURLException e) {
		 * 	// new URL() failed
		 * 	// ...
		 * } catch (IOException e) {
		 * 	// openConnection() failed
		 * 	// ...
		 * }
		 * </pre>
		 */

		System.out.println("\nReading from and Writing to a URLConnection");
		// https://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html

	}

}

class ParseURL {
	public static void main(String[] args) throws Exception {

		URL aURL = new URL("http://example.com:80/docs/books/tutorial" + "/index.html?name=networking#DOWNLOADING");

		System.out.println("protocol = " + aURL.getProtocol());
		System.out.println("authority = " + aURL.getAuthority());
		System.out.println("host = " + aURL.getHost());
		System.out.println("port = " + aURL.getPort());
		System.out.println("path = " + aURL.getPath());
		System.out.println("query = " + aURL.getQuery());
		System.out.println("filename = " + aURL.getFile());
		System.out.println("ref = " + aURL.getRef());
	}
}

class URLReader {
	public static void main(String[] args) throws Exception {

		URL oracle = new URL("http://sahet.net/");
		BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

		String inputLine;
		while ((inputLine = in.readLine()) != null)
			if (inputLine.contains("Azat") || inputLine.contains("Misgin")
					|| !(inputLine.contains("href") || inputLine.contains("<p")))
				System.out.println(inputLine);
		in.close();
	}
}

class URLConnectionDemo {
	public static void main(String[] args) {
		URL url = null;
		try {
			url = new URL("http://sahet.net");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		URLConnection urlConnection = null;
		try {
			urlConnection = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream input = null;
		try {
			input = urlConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int data = 0;
		try {
			data = input.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (data != -1) {
			System.out.print((char) data);
			try {
				data = input.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("/nHTTP GET and POST");
		/**
		 * By default the URLConnection sends an HTTP GET request to the webserver. If
		 * you want to send an HTTP POST request instead, call the
		 * URLConnection.setDoOutput(true) method, like this:
		 */
		try {
			url = new URL("https://seznam.cz");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			urlConnection = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		urlConnection.setDoOutput(true);// POST method
		/*
		 * Using this OutputStream you can write any data you want in the body of the
		 * HTTP request. Remember to URL encode it (search Google for an explanation of
		 * URL encoding).
		 */

	}

}

class URLConnectionReader {
	public static void main(String[] args) throws Exception {
		URL oracle = new URL("https://www.oracle.com/");
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			System.out.println(inputLine);
		in.close();
	}
}

class URLConnectionReaderAndWriter {
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println(
					"Usage:  java Reverse " + "http://<location of your servlet/script>" + " string_to_reverse");
			System.exit(1);
		}

		String stringToReverse = URLEncoder.encode(args[1], "UTF-8");

		URL url = new URL(args[0]);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);

		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		out.write("string=" + stringToReverse);
		out.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String decodedString;
		while ((decodedString = in.readLine()) != null) {
			System.out.println(decodedString);
		}
		in.close();
	}
}
