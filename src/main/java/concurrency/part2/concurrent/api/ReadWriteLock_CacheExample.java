package concurrency.part2.concurrent.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * see old way of it {@link SynchronizedHashMapUsingUnsafeCollection}
 * 
 * and alternative {@link SynchronizedHashMapUsingSafeCollection}
 * 
 * Below solution fixes race condition and all threads are busy with adding
 * resources
 */
public class ReadWriteLock_CacheExample {

	/**
	 * example, key can be a
	 * 
	 * primary key from database
	 */
	private Map<Long, String> cache = new HashMap<>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock readLock = lock.readLock();
	private Lock writeLock = lock.writeLock();

	/**
	 * For both write methods, we need to surround the critical section with the
	 * write lock — only one thread can get access to it:
	 */
	public String put(Long key, String value) {
		writeLock.lock();
		try {
			return cache.put(key, value);
		} finally {
			writeLock.unlock();
		}
	}

	public String get(Long key) {
		readLock.lock();
		try {
			return cache.get(key);
		} finally {
			readLock.unlock();
		}
	}

	public static void main(String[] args) {

		ReadWriteLock_CacheExample cache = new ReadWriteLock_CacheExample();

		class Producer implements Callable<String> {

			private Random rand = new Random();

			public String call() throws Exception {
				while (true) {
					long key = rand.nextInt(1_000);
					// make a check at WHILE if you want to finish adding
					cache.put(key, Long.toString(key));
					if (cache.get(key) == null) {
						System.out.println("Key " + key + " has not been put in the map");
					}
				}
			}
		}

		ExecutorService executorService = Executors.newFixedThreadPool(4);

		System.out.println("Adding value...");

		try {
			for (int i = 0; i < 4; i++) {
				executorService.submit(new Producer());
			}
		} finally {
			executorService.shutdown();
		}
	}
}



//other example
class SynchronizedHashMapWithReadWriteLock {

	Map<String, String> syncHashMap = new HashMap<>();
	ReadWriteLock lock = new ReentrantReadWriteLock(); 
	// ...
	Lock writeLock = lock.writeLock(); 
	Lock readLock = lock.readLock();

	public void put(String key, String value) {
		try {
			writeLock.lock();
			syncHashMap.put(key, value);
		} finally {
			writeLock.unlock();
		}
	}

	public String remove(String key) {
		try {
			writeLock.lock();
			return syncHashMap.remove(key);
		} finally {
			writeLock.unlock();
		}
	}
	// ...


	// ...
	public String get(String key) {
		try {
			readLock.lock();
			return syncHashMap.get(key);
		} finally {
			readLock.unlock();
		}
	}

	public boolean containsKey(String key) {
		try {
			readLock.lock();
			return syncHashMap.containsKey(key);
		} finally {
			readLock.unlock();
		}
	}
}
