package concurrency.java.concurrent.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Hence, the Java platform provides strong support for this scenario through
 * different synchronization wrappers implemented within the Collections class.
 * 
 * These wrappers make it easy to create synchronized views of the supplied
 * collections by means of several static factory methods.
 * 
 * https://www.baeldung.com/java-synchronized-collections
 *
 */
public class SynchronizedJavaCollections {

	public static void main(String[] args) throws InterruptedException {
		Collection<Integer> syncCollection = Collections.synchronizedCollection(new ArrayList<>());
		Runnable listOperations = () -> {
			syncCollection.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));
		};

		Thread thread1 = new Thread(listOperations);
		Thread thread2 = new Thread(listOperations);
		thread1.start();
		thread2.start();
		thread1.join();
		thread2.join();

		System.out.println(syncCollection.size());// 12

		/*
		 * synchronizedList() method looks nearly identical to its higher-level
		 * counterpart, synchronizedCollection().
		 */
		// List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
		List<String> syncList = Collections.synchronizedList(Arrays.asList("a", "b", "c"));
		List<String> uppercasedCollection = new ArrayList<>();

		listOperations = () -> {
			synchronized (syncList) {
				syncList.forEach((e) -> {
					uppercasedCollection.add(e.toUpperCase());
				});
			}
		};

	}
}
