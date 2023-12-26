package concurrency.java.concurrent.api;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The Java Lock interface, java.util.concurrent.locks.Lock, represents a
 * concurrent lock which can be used to guard against race conditions inside
 * critical sections. Thus, the Java Lock interface provides a more flexible
 * alternative to a Java synchronized block
 * 
 * see {@link AnatomyOfASynchronizer} see {@link ExchangerDemo}
 * 
 * <pre>
 * Main Differences Between a Lock and a Synchronized Block
 * 
The main differences between a Lock and a synchronized block are:

A synchronized block makes no guarantees about the sequence in which threads waiting to entering it are granted access.
You cannot pass any parameters to the entry of a synchronized block. Thus, having a timeout trying to get access to a synchronized block is not possible.
The synchronized block must be fully contained within a single method. A Lock can have it's calls to lock() and unlock() in separate methods.
 * </pre>
 */
class LocksInJava {
	/**
	 * https://jenkov.com/tutorials/java-concurrency/locks.html
	 */
	public static void main(String[] args) {

	}
}

/**
 * Notice the synchronized(this) block in the inc() method. This block makes
 * sure that only one thread can execute the return ++count at a time. The code
 * in the synchronized block could have been more advanced, but the simple
 * ++count suffices to get the point across.
 *
 */
class CounterOld {

	private int count = 0;

	public int inc() {
		synchronized (this) {
			return ++count;
		}
	}
}

class CounterNew {

	private MyLock lock = new MyLock();
	private int count = 0;

	Lock lock2 = new ReentrantLock();

	/**
	 * The lock() method locks the Lock instance so that all threads calling lock()
	 * are blocked until unlock() is executed.
	 * 
	 * * Notice how both outer() and inner() are declared synchronized, which in
	 * Java is equivalent to a synchronized(this) block. If a thread calls outer()
	 * there is no problem calling inner() from inside outer(), since both methods
	 * (or blocks) are synchronized on the same monitor object ("this"). If a thread
	 * already holds the lock on a monitor object, it has access to all blocks
	 * synchronized on the same monitor object.
	 * 
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public int inc() throws InterruptedException {
		lock.lock();
		int newCount = ++count;
		lock.unlock(); // he exception would interrupt the program flow, and the call to lock.unlock()
						// would never be executed.
		return newCount;
	}

	/**
	 * Fail-safe Lock and Unlock
	 * 
	 * 
	 * If you look at the example in the previous section, imagine what happens if
	 * an exception is thrown between the call to lock.lock() and lock.unlock() .
	 * The exception would interrupt the program flow, and the call to lock.unlock()
	 * would never be executed. The Lock would thus remain locked forever.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public int incFailSafe() {
		int newCount = 0;
		try {
			lock2.lock();
			newCount = ++count;
			// critical section
		} finally {
			lock2.unlock();
		}
		return newCount;
	}

}

class LockImpl {

	private boolean isLocked = false;

	public synchronized void lock() throws InterruptedException {
		while (isLocked) {
			wait();
		}
		isLocked = true;
	}

	public synchronized void unlock() {
		isLocked = false;
		notify();
	}
}

/**
 * Lock Reentrance
 * 
 * 
 * Synchronized blocks in Java are reentrant. This means, that if a Java thread
 * enters a synchronized block of code, and thereby take the lock on the monitor
 * object the block is synchronized on, the thread can enter other Java code
 * blocks synchronized on the same monitor object
 * 
 * 
 * A lock is a thread synchronization mechanism like synchronized blocks except
 * locks can be more sophisticated than Java's synchronized blocks.
 * 
 * <pre>
 *Locks (and
 * other more advanced synchronization mechanisms) are created using
 * synchronized blocks, so it is not like we can get totally rid of the
 * synchronized keyword.
 * 
 * From Java 5 the package java.util.concurrent.locks contains several lock
 * implementations, so you may not have to implement your own locks.
 * 
 * </pre>
 */
public class LocksInJavaReentrant {

	/**
	 * Read / Write Lock Java Implementation First let's summarize the conditions
	 * for getting read and write access to the resource:
	 * 
	 * Read Access If no threads are writing, and no threads have requested write
	 * access. Write Access If no threads are reading or writing.
	 */
	public static void main(String[] args) {

	}

}

/**
 * 
 * 
 * This is called reentrance. The thread can reenter any block of code for which
 * it already holds the lock.
 * 
 * 
 * 
 * Lock Reentrance Synchronized blocks in Java are reentrant.
 * 
 * This means, that if a Java thread enters a synchronized block of code, and
 * thereby take the lock on the monitor object the block is synchronized on, the
 * thread can enter other Java code blocks synchronized on the same monitor
 * object. Here is an example:
 * 
 * 
 */
class Calculator {

	public static class Calculation {
		public static final int UNSPECIFIED = -1;
		public static final int ADDITION = 0;
		public static final int SUBTRACTION = 1;
		int type = UNSPECIFIED;

		public double value;

		public Calculation(int type, double value) {
			this.type = type;
			this.value = value;
		}
	}

	private double result = 0.0D;
	Lock lock = new ReentrantLock();

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

	public void calculate(Calculation... calculations) {
		try {
			lock.lock();

			for (Calculation calculation : calculations) {
				switch (calculation.type) {
				case Calculation.ADDITION:
					add(calculation.value);
					break;
				case Calculation.SUBTRACTION:
					subtract(calculation.value);
					break;
				}
			}
		} finally {
			lock.unlock();
		}
	}
}

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
 * see also {@link StarvationAndFairness}
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