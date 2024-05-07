package concurrency.part2.concurrent.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

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

class ReentrantLockDemo {

	public static final int PLUS = 0;
	public static final int MINUS = 1;
	int type = -1;

	public int value;
	private int result;

	Lock lock = new ReentrantLock();

	public ReentrantLockDemo(int type, int value) {
		this.type = type;
		this.value = value;
	}

	public void add(double value) {
		try {
			lock.lock();
			this.result += value;
		} finally {
			lock.unlock();
		}
	}

	public void subtract(double value) {
		try {
			lock.lock();
			this.result -= value;
		} finally {
			lock.unlock();
		}
	}

	public void exec(ReentrantLockDemo... calculations) {
		try {
			lock.lock();

			for (ReentrantLockDemo calculation : calculations) {
				switch (calculation.type) {
				case ReentrantLockDemo.PLUS:
					add(calculation.value);
					break;
				case ReentrantLockDemo.MINUS:
					subtract(calculation.value);
					break;
				}
			}
		} finally {
			lock.unlock();
		}
	}
}

class ReentrantLockTryLock {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		ReentrantLock lock = new ReentrantLock();

		/**
		 * First a Lock is created. Then it's lock() method is called. Now the Lock
		 * instance is locked. Any other thread calling lock() will be blocked until the
		 * thread that locked the lock calls unlock(). Finally unlock() is called, and
		 * the Lock is now unlocked so other threads can lock it.
		 * 
		 * 
		 * Obviously all threads must share the same Lock instance. If each thread
		 * creates its own Lock instance, then they will be locking on different locks,
		 * and thus not be blocking each other from access. I will show you later in
		 * this Java Lock tutorial an example of how a shared Lock instance looks.
		 */
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
			/**
			 * Acquires the lock only if it is not held by another thread at the timeof
			 * invocation.
			 */
			boolean locked = lock.tryLock();
			System.out.println("Lock acquired: " + locked);
		});

		// stop(executor)
	}

}



/**
 * StampedLock is introduced in Java 8. It also supports both read and write
 * locks.
 * 
 * However, lock acquisition methods return a stamp that is used to release a
 * lock or to check if the lock is still valid:
 * 
 */
class StampedLockDemo {
	Map<String, String> map = new HashMap<>();
	private StampedLock lock = new StampedLock();

	public void put(String key, String value) {
		long stamp = lock.writeLock();
		try {
			map.put(key, value);
		} finally {
			lock.unlockWrite(stamp);
		}
	}

	public String get(String key) throws InterruptedException {
		long stamp = lock.readLock();
		try {
			return map.get(key);
		} finally {
			lock.unlockRead(stamp);
		}
	}

	/*
	 * Another feature provided by StampedLock is optimistic locking. Most of the
	 * time, read operations don’t need to wait for write operation completion, and
	 * as a result of this, the full-fledged read lock isn’t required.
	 * 
	 * Instead, we can upgrade to read lock:
	 */
	public String readWithOptimisticLock(String key) {
		long stamp = lock.tryOptimisticRead();
		String value = map.get(key);

		if (!lock.validate(stamp)) {
			stamp = lock.readLock();
			try {
				return map.get(key);
			} finally {
				lock.unlock(stamp);
			}
		}
		return value;
	}
}

//StampedLock instead of ReadWriteLock: https://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/
/**
 *
 * 
 * StampedLock Provide read/write locks, but is NOT reentrant. Uses stamp (a
 * long value), to release a lock or to check if the lock is still valid.
 * readLock(): exclusive blocking, writeLock(): non-exclusive blocking
 * tryOptimisticRead(): returns a non-zero stamp only if the lock is not
 * currently held in write mode, use validate(stamp) (repeatablly) to test if
 * any write lock has break it tryConvertToWriteLock(): try to convert a read
 * lock to write lock
 * 
 */

class StampedLockDemo2 {
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

/**
 * Working With Conditions The Condition class provides the ability for a thread
 * to wait for some condition to occur while executing the critical section.
 * 
 * This can occur when a thread acquires the access to the critical section but
 * doesn’t have the necessary condition to perform its operation. For example, a
 * reader thread can get access to the lock of a shared queue that still doesn’t
 * have any data to consume.
 * 
 * Traditionally Java provides wait(), notify() and notifyAll() methods for
 * thread intercommunication.
 * 
 * Conditions have similar mechanisms, but we can also specify multiple
 * conditions
 *
 */
class ReentrantLockWithCondition {

	Stack<String> stack = new Stack<>();
	int CAPACITY = 5;

	ReentrantLock lock = new ReentrantLock();
	Condition stackEmptyCondition = lock.newCondition();
	Condition stackFullCondition = lock.newCondition();

	public void pushToStack(String item) throws InterruptedException {
		try {
			lock.lock();
			while (stack.size() == CAPACITY) {
				stackFullCondition.await();
			}
			stack.push(item);
			stackEmptyCondition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public String popFromStack() throws InterruptedException {
		try {
			lock.lock();
			while (stack.size() == 0) {
				stackEmptyCondition.await();
			}
			return stack.pop();
		} finally {
			stackFullCondition.signalAll();
			lock.unlock();
		}
	}
}

class Safelockz {
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