package concurrency.part3.completablefuture.java11.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

/**
 * Downloading a file is very straightforward. Send a GET request and then handle
 * the bytes in the response according to your use case. In this example, we
 * utilize a BodyHandler that automatically saves the bytes from the response
 * into a file on the local filesystem.
 * 
 * 
 * Notice that the Java 11 client does not handle compression transparently
 *
 */
class FileDownload {

	public static void main(String[] args) throws IOException, InterruptedException {
		var url = "https://www.7-zip.org/a/7z1806-x64.exe";

		var client = HttpClient.newBuilder().build();
		var request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();

		Path localFile = Paths.get("7z.exe");
		HttpResponse<Path> response = client.send(request, BodyHandlers.ofFile(localFile));
		System.out.println("Downloaded, see the project folder");

	}

}

/**
 * Upload with multipart If the server endpoint expects binary data in the
 * request body, an application could send a POST request with
 * BodyPublishers.ofFile. This publisher reads a file from the filesystem and
 * sends the bytes in the body to the server.
 * 
 * However, in this case, we need to send some additional data in the POST
 * request body and use a multipart form post with the Content-Type
 * multipart/form-data. The request body is specially formatted as a series of
 * parts, separated with boundaries. Unfortunately, the Java 11 HTTP client does
 * not provide any convenient support for this kind of body, but we can build it
 * from scratch.
 * 
 * The following method takes a Map of key/value pairs and a boundary and then
 * builds the multipart body.
 *
 */
class FileUpload {

	public static BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) throws IOException {
		var byteArrays = new ArrayList<byte[]>();
		byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=")
				.getBytes(StandardCharsets.UTF_8);
		for (Map.Entry<Object, Object> entry : data.entrySet()) {
			byteArrays.add(separator);

			if (entry.getValue() instanceof Path) {
				var path = (Path) entry.getValue();
				String mimeType = Files.probeContentType(path);
				byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName() + "\"\r\nContent-Type: "
						+ mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
				byteArrays.add(Files.readAllBytes(path));
				byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
			} else {
				byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n")
						.getBytes(StandardCharsets.UTF_8));
			}
		}
		byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
		return BodyPublishers.ofByteArrays(byteArrays);

	}

}
