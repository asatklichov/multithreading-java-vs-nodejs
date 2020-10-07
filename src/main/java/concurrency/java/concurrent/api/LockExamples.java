package concurrency.java.concurrent.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

/**
 * Java provides an improved set of Lock implementations, whose behavior is
 * slightly more sophisticated than the intrinsic locks discussed above.
 * 
 * With intrinsic locks, the lock acquisition model is rather rigid: one thread
 * acquires the lock, then executes a method or code block, and finally releases
 * the lock, so other threads can acquire it and access the method.
 * 
 * There's no underlying mechanism that checks the queued threads and gives
 * priority access to the longest waiting threads.
 * 
 * ReentrantLock instances allow us to do exactly that, hence preventing queued
 * threads from suffering some types of resource starvation:
 * 
 * 
 * The ReentrantLock constructor takes an optional fairness boolean parameter.
 * When set to true, and multiple threads are trying to acquire a lock, the JVM
 * will give priority to the longest waiting thread and grant access to the
 * lock.
 * 
 * https://www.baeldung.com/java-concurrent-locks
 *
 */
class ReentrantLockCounter {

	private int counter;
	private final ReentrantLock reLock = new ReentrantLock(true);

	public void incrementCounter() {
		reLock.lock();
		try {
			counter += 1;
		} finally {
			reLock.unlock();
		}
	}

	// TBD

}

class RenetrantLock {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		ReentrantLock lock = new ReentrantLock();

		executor.submit(() -> {
			lock.lock();
			try {
				// sleep(1);
			} finally {
				lock.unlock();
			}
		});

		executor.submit(() -> {
			System.out.println("Locked: " + lock.isLocked());
			System.out.println("Held by me: " + lock.isHeldByCurrentThread());
			boolean locked = lock.tryLock();
			System.out.println("Lock acquired: " + locked);
		});

		// stop(executor)
	}

}

class ReadWriteLockDemo {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		ReadWriteLock lock = new ReentrantReadWriteLock();

		executor.submit(() -> {
			lock.writeLock().lock();
			try {
				// sleep(1);
				map.put("foo", "bar");
			} finally {
				lock.writeLock().unlock();
			}
		});

		Runnable readTask = () -> {
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

//StampedLock instead of ReadWriteLock: https://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/ 
class StampedLockDemo {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
			long stamp = lock.writeLock();
			try {
				// sleep(1);
				map.put("foo", "bar");
			} finally {
				lock.unlockWrite(stamp);
			}
		});

		Runnable readTask = () -> {
			long stamp = lock.readLock();
			try {
				System.out.println(map.get("foo"));
				// sleep(1);
			} finally {
				lock.unlockRead(stamp);
			}
		};

		executor.submit(readTask);
		executor.submit(readTask);

		// stop(executor);
	}
}

//The next example demonstrates optimistic locking:
class OptimisticLockDemo {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
			long stamp = lock.tryOptimisticRead();
			try {
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
				// sleep(1);
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
				// sleep(2);
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
			} finally {
				lock.unlock(stamp);
			}
		});

		executor.submit(() -> {
			long stamp = lock.writeLock();
			try {
				System.out.println("Write Lock acquired");
				// sleep(2);
			} finally {
				lock.unlock(stamp);
				System.out.println("Write done");
			}
		});

		// stop(executor);
	}
}

class SemaphoresDemo {
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(10);

		Semaphore semaphore = new Semaphore(5);

		Runnable longRunningTask = () -> {
			boolean permit = false;
			try {
				permit = semaphore.tryAcquire(1, TimeUnit.SECONDS);
				if (permit) {
					System.out.println("Semaphore acquired");
					// sleep(5);
				} else {
					System.out.println("Could not acquire semaphore");
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			} finally {
				if (permit) {
					semaphore.release();
				}
			}
		};

		IntStream.range(0, 10).forEach(i -> executor.submit(longRunningTask));

		// stop(executor);
	}
}

class Safelock {
	static class Friend {
		private final String name;
		private final Lock lock = new ReentrantLock();

		public Friend(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public boolean impendingBow(Friend bower) {
			Boolean myLock = false;
			Boolean yourLock = false;
			try {
				myLock = lock.tryLock();
				yourLock = bower.lock.tryLock();
			} finally {
				if (!(myLock && yourLock)) {
					if (myLock) {
						lock.unlock();
					}
					if (yourLock) {
						bower.lock.unlock();
					}
				}
			}
			return myLock && yourLock;
		}

		public void bow(Friend bower) {
			if (impendingBow(bower)) {
				try {
					System.out.format("%s: %s has" + " bowed to me!%n", this.name, bower.getName());
					bower.bowBack(this);
				} finally {
					lock.unlock();
					bower.lock.unlock();
				}
			} else {
				System.out.format(
						"%s: %s started" + " to bow to me, but saw that" + " I was already bowing to" + " him.%n",
						this.name, bower.getName());
			}
		}

		public void bowBack(Friend bower) {
			System.out.format("%s: %s has" + " bowed back to me!%n", this.name, bower.getName());
		}
	}

	static class BowLoop implements Runnable {
		private Friend bower;
		private Friend bowee;

		public BowLoop(Friend bower, Friend bowee) {
			this.bower = bower;
			this.bowee = bowee;
		}

		public void run() {
			Random random = new Random();
			for (;;) {
				try {
					Thread.sleep(random.nextInt(10));
				} catch (InterruptedException e) {
				}
				bowee.bow(bower);
			}
		}
	}

	public static void main(String[] args) {
		final Friend alphonse = new Friend("Alphonse");
		final Friend gaston = new Friend("Gaston");
		new Thread(new BowLoop(alphonse, gaston)).start();
		new Thread(new BowLoop(gaston, alphonse)).start();
	}
}