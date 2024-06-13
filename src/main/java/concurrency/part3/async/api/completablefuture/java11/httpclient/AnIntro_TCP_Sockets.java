package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//https://docs.oracle.com/javase/tutorial/networking/sockets/index.html
public class AnIntro_TCP_Sockets {
	public static void main(String[] args) {
		System.out.println(
				"URLs and URLConnections provide a relatively high-level mechanism for accessing resources on the Internet. \n"
						+ "Sometimes your programs require lower-level network communication, for example, when you want to write a client-server application.");

		System.out.println("\nWhat is TCP");
		System.out.println(
				"When two applications want to communicate to each other reliably, they establish a connection and send data back and forth over that connection.\n"
						+ "TCP (Transmission Control Protocol) is a connection-based protocol that provides a reliable flow of data between two computers.");

		System.out.println("What is a socket?");
		System.out.println(
				"A socket is one endpoint of a two-way communication link between two programs running on the network. A socket is bound to a port number so that the TCP layer can identify \n"
						+ "the application that data is destined to be sent to.");
		System.out.println(
				"An endpoint is a combination of an IP address and a port number. Every TCP connection can be uniquely identified by its two endpoints. ");

		System.out.println("\nReading from and Writing to a Socket");
		// https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
	}

}

//https://datatracker.ietf.org/doc/html/rfc862
class EchoServer {
	public static void main(String[] args) throws IOException {

//		if (args.length != 1) {
//			System.err.println("Usage: java EchoServer <port number>");
//			System.exit(1);
//		}

		int portNumber = 6060; // Integer.parseInt(args[0]);

		try (ServerSocket serverSocket = new ServerSocket(portNumber); // Integer.parseInt(args[0])
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				out.println("SERVER: " + inputLine);
			}
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}

class EchoClient {
	public static void main(String[] args) throws IOException {

		/*
		 * if (args.length != 2) {
		 * System.err.println("Usage: java EchoClient <host name> <port number>");
		 * System.exit(1); }
		 */

		String hostName = "localhost"; // args[0];
		int portNumber = 6060; // Integer.parseInt(args[1]);

		try (Socket echoSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
			String userInput;
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				System.out.println("echo: " + in.readLine());
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}
}