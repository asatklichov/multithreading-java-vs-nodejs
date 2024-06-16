package concurrency.part3.async.api.completablefuture.java11.httpclient;

import java.time.Duration;
import java.time.Instant;

public class Util {

	                                                                                                            
	public static final String DOMAINS_TXT = "C:\\workspace-17\\multithreading-java-vs-nodejs\\src\\main\\java\\concurrency\\part3\\async\\api\\completablefuture\\java11\\httpclient\\domains_list100.txt";// domains.txt
	public static final String DOMAINS_TXT2 = "C:\\workspace-17\\multithreading-java-vs-nodejs\\src\\main\\java\\concurrency\\part3\\async\\api\\completablefuture\\java11\\httpclient\\domains_list500.txt";

	public static void printElapsedTime(Instant start) {
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toMillis();

		System.out.println("\nElapsed time: " + timeElapsed + " ms");
	}

	/**
	 * Just provide arg: long start = System.nanoTime();
	 * 
	 * 
	 * @param start
	 */
	public static void printElapsedTimeInNanoseconds(long start) {
		long finish = System.nanoTime();
		long timeElapsed = finish - start;
		System.out.println("\nElapsed time: " + timeElapsed + " ms");
	}

	public static void heavySum(int s) {
		long sum = s;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			sum += i;
		}
		System.out.println("Sum = " + sum);
	}

	public static long calcHeavySum(int s, int sleep) throws InterruptedException {
		long sum = s;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			sum += i;
		}
		Thread.currentThread().sleep(sleep);
		return sum;
	}
}