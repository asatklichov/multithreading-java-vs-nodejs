package concurrency.java.concurrent.api;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Java Lock interface, java.util.concurrent.locks.Lock, represents a
 * concurrent lock which can be used to guard against race conditions inside
 * critical sections. Thus, the Java Lock interface provides a more flexible
 * alternative to a Java synchronized block
 * 
 * see {@link Synchronizers} see {@link ExchangerDemo}
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

public class LockImplicitAndExplicitWay {

}

class IntrinsicLockDemo {
	private final Object lock = new Object();

	/**
	 * Synchronized pattern is used to run one thread on a guarded code where other
	 * several threads will wait for the key to be released.
	 * 
	 * What happens if a thread is blocked inside block (suppose a bug in guarded
	 * code, etc.)? Then thread will not release the key, and other threads will
	 * keep waiting forever unless you reboot the JVM. Avoid this!
	 * 
	 * * Notice the synchronized(this) block in the inc() method. This block makes
	 * sure that only one thread can execute the return ++count at a time. The code
	 * in the synchronized block could have been more advanced, but the simple
	 * ++count suffices to get the point across.
	 * 
	 * https://jenkov.com/tutorials/java-concurrency/locks.html
	 *
	 * 
	 */
	public String init() {
		synchronized (lock) {
			// todo
			return "";
		}
	}
}

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
 * As an improved solution to above Intrinsic lock problem, JDK provides a
 * solution via Reentrant lock object
 *
 */
class LockDemo {
	private final Lock lock = new ReentrantLock();
	private int count = 0;

	public void init() {
		/**
		 * We need to make sure that we are wrapping the lock() and the unlock() calls
		 * in the try-finally block to avoid the deadlock situations.
		 * 
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
		 * 
		 */
		try {
			lock.lock();
			// todo
			count++;
		} catch (Exception e) {

		} finally {
			lock.unlock();
		}
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
 *  * This is called reentrance. The thread can reenter any block of code for which
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
 * </pre>
 */
class LocksInJavaReentrant {

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
 * lockInterruptibly() - used to interrupt the thread when it’s waiting for the
 * lock. Simply it locks the Lock unless the thread calling the method has been
 * interrupted
 * 
 * 
 *
 */
class LocksCanBeInterruptible {
	private final Lock lock = new ReentrantLock();

	public void doIt() throws InterruptedException {
		try {
			// lock.lock();
			// instead of lock() use below
			lock.lockInterruptibly(); // releases waiting thread in case exception happens
			// todo
		} finally {
			lock.unlock();
		}
	}
}

class TimedLockAcquisition {

	private final Lock lock = new ReentrantLock();

	/**
	 * In this case, the thread calling tryLock() will wait for one second and will
	 * give up waiting if the lock isn’t available.
	 * 
	 * So, instead of block thread will not enter the guarded block of code and can
	 * do something else
	 * 
	 */
	void performTryLock() throws InterruptedException {
		// ...
		boolean isLockAcquired = lock.tryLock(1, TimeUnit.SECONDS);

		if (isLockAcquired) {
			try {
				// Critical section here
			} finally {
				lock.unlock();
			}
		}
	}
}

/**
 * Lock Fairness An unfair lock does not guarantee the order in which threads
 * waiting to lock the lock will be given access to lock it, so Thread
 * Starvation happens. The ReentrantLock behaviour is unfair by default, but can
 * be tuned to operate in fair mode via its constructor. Note that
 * tryLock() with no parameters does not respect the fairness mode, so you must
 * use the tryLock(long timeout, TimeUnit unit)
 * 
 * First to enter the waiting line is the first to enter the block (guarded
 * zone) of code
 *
 * 
 */
class FairLockDemo {

	// by setting TRUE
	private final Lock fairLock = new ReentrantLock(true);

	void perform() {

		try {
			fairLock.lock();
		} finally {
			fairLock.unlock();
		}
	}
}