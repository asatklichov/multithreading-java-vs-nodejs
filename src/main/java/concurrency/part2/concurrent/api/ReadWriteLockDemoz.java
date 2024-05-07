package concurrency.part2.concurrent.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * * A java.util.concurrent.locks.ReadWriteLock is an advanced thread lock
 * mechanism. It allows multiple threads to read a certain resource, but only
 * one to write it, at a time.
 * 
 * Notice how the ReadWriteLock actually internally keeps two Lock instances.
 * One guarding read access, and one guarding write access.
 * 
 * see {@link MyReadWriteLock} as custome impl.
 *
 */
class ReadWriteLockDemo {
	public static void main(String[] args) {
		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

		readWriteLock.readLock().lock();

		// multiple readers can enter this section
		// if not locked for writing, and not writers waiting
		// to lock for writing.

		readWriteLock.readLock().unlock();

		readWriteLock.writeLock().lock();

		// only one writer can enter this section,
		// and only if no threads are currently reading.

		readWriteLock.writeLock().unlock();
	}

}

/**
 * 
 * ReentrantReadWriteLock ReentrantReadWriteLock class implements the
 * ReadWriteLock interface.
 * 
 * ReadWriteLock interface that maintains a pair of locks, one for read-only
 * operations and one for the write operation. The read lock may be
 * simultaneously held by multiple threads as long as there is no write.
 * 
 * <pre>
 * Let’s see the rules for acquiring the ReadLock or WriteLock by a thread:
	 Read Lock – If no thread acquired the write lock or requested for it, multiple threads can acquire the read lock.
	 Write Lock – If no threads are reading or writing, only one thread can acquire the write lock.
 * </pre>
 */

class ReadWriteLockDemo2 {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		ReadWriteLock lock = new ReentrantReadWriteLock();

		executor.submit(() -> {
			/**
			 * Write Lock – If no threads are reading or writing, only one thread can
			 * acquire the write lock.
			 * 
			 */
			lock.writeLock().lock();
			try {
				// sleep(1);
				map.put("foo", "bar");
			} finally {
				lock.writeLock().unlock();
			}
		});

		Runnable readTask = () -> {
			/**
			 * Read Lock – If no thread acquired the write lock or requested for it,
			 * multiple threads can acquire the read lock.
			 * 
			 */
			lock.readLock().lock();
			try {
				System.out.println(map.get("foo"));
				// sleep(1);
			} finally {
				lock.readLock().unlock();
			}
		};

		executor.submit(readTask);
		executor.submit(readTask);

		// stop(executor);
	}
}

/**
 * 
 * see also {@link LockzFairnessandStarvation}
 * 
 * A java.util.concurrent.locks.ReadWriteLock is an advanced thread lock
 * mechanism. It allows multiple threads to read a certain resource, but only
 * one to write it, at a time.
 * 
 * A read / write lock is more sophisticated lock than the Lock implementations
 * shown in the text Locks in Java.
 * 
 * https://jenkov.com/tutorials/java-concurrency/read-write-locks.html
 * 
 * The ReadWriteLock has two lock methods and two unlock methods. One lock and
 * unlock method for read access and one lock and unlock for write access.
 *
 */
class MyReadWriteLock {

	private int readers = 0;
	private int writers = 0;
	private int writeRequests = 0;

	public synchronized void lockRead() throws InterruptedException {
		while (writers > 0 || writeRequests > 0) {
			wait();
		}
		readers++;
	}

	public synchronized void unlockRead() {
		readers--;
		notifyAll();
	}

	public synchronized void lockWrite() throws InterruptedException {
		writeRequests++;

		while (readers > 0 || writers > 0) {
			wait();
		}
		writeRequests--;
		writers++;
	}

	public synchronized void unlockWrite() throws InterruptedException {
		writers--;
		notifyAll();
	}
}

/**
 * 
 * See {@link ReadWriteLock_CacheExample} for new approach using ReadWriteLock *
 * to get MAX throughput.
 * 
 * In below approach, where some keys are not put to map due to race condition
 *
 */
class SynchronizedHashMapUsingUnsafeCollection {

	private Map<Long, String> cache = new HashMap<>();

	public void put(Long key, String value) {
		cache.put(key, value);
	}

	public String get(Long key) {
		return cache.get(key);
	}

	public static void main(String[] args) {

		SynchronizedHashMapUsingUnsafeCollection cache = new SynchronizedHashMapUsingUnsafeCollection();

		class Producer implements Callable<String> {

			private Random rand = new Random();

			public String call() throws Exception {
				while (true) {
					long key = rand.nextInt(1_000);
					// unsafe collection - Map used that is why
					// race condition problem happened
					cache.put(key, Long.toString(key));
					if (cache.get(key) == null) {
						System.out.println("Key " + key + " has not been put in the map");
					}
				}
			}
		}

		ExecutorService executorService = Executors.newFixedThreadPool(4);

		try {
			for (int i = 0; i < 4; i++) {
				executorService.submit(new Producer());
			}
		} finally {
			executorService.shutdown();
		}
	}

}

/**
 * 
 * See {@link ReadWriteLock_CacheExample} for new approach using ReadWriteLock *
 * to get MAX throughput.
 * 
 * Using Collections.synchronizedMap and fix the problem, BUT gives MIN
 * throughput
 */
class SynchronizedHashMapUsingSafeCollection {

}
