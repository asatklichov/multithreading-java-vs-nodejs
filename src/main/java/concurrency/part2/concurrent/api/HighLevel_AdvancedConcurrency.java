package concurrency.part2.concurrent.api;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HighLevel_AdvancedConcurrency {

	public static void main(String[] args) {
		// https://docs.oracle.com/javase/tutorial/essential/concurrency/highlevel.html
		/**
		 * <pre>
		 * High Level Concurrency Objects
		So far, this lesson has focused on the low-level APIs that have been part of the Java platform from the very beginning. These APIs are adequate for very basic 
		tasks, but higher-level building blocks are needed for more advanced tasks. This is especially true for massively concurrent applications that fully exploit today's 
		multiprocessor and multi-core systems.
		
		In this section we'll look at some of the high-level concurrency features introduced with version 5.0 of the Java platform. Most of these features are implemented in 
		the new java.util.concurrent packages. There are also new concurrent data structures in the Java Collections Framework.
		
		Lock objects support locking idioms that simplify many concurrent applications. 
		- https://docs.oracle.com/javase/tutorial/essential/concurrency/newlocks.html
		
		
		Executors define a high-level API for launching and managing threads. Executor implementations provided by java.util.concurrent provide thread pool management suitable 
		for large-scale applications.
		- https://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html
		
		Concurrent collections make it easier to manage large collections of data, and can greatly reduce the need for synchronization.
		- https://docs.oracle.com/javase/tutorial/essential/concurrency/collections.html
		
		Atomic variables have features that minimize synchronization and help avoid memory consistency errors.
		- https://docs.oracle.com/javase/tutorial/essential/concurrency/atomicvars.html
		
		ThreadLocalRandom (in JDK 7) provides efficient generation of pseudo-random numbers from multiple threads.
		- https://docs.oracle.com/javase/tutorial/essential/concurrency/threadlocalrandom.html
		 * </pre>
		 */
		System.out.println("Lock Objects - e.g. Safelock below");
		System.out.println(
				" As with implicit locks[intrinsic locks - synchronization], only one thread can own a Lock object at a time. \n"
						+ "Lock objects also support a wait/notify mechanism, through their associated Condition objects");

		/**
		 * Synchronized code relies on a simple kind of reentrant lock. This kind of
		 * lock is easy to use, but has many limitations. More sophisticated locking
		 * idioms are supported by the java.util.concurrent.locks package. We won't
		 * examine this package in detail, but instead will focus on its most basic
		 * interface, Lock.
		 * 
		 * Lock objects work very much like the implicit locks used by synchronized
		 * code. As with implicit locks, only one thread can own a Lock object at a
		 * time. Lock objects also support a wait/notify mechanism, through their
		 * associated Condition objects.
		 * 
		 * The biggest advantage of Lock objects over implicit locks is their ability to
		 * back out of an attempt to acquire a lock. The tryLock method backs out if the
		 * lock is not available immediately or before a timeout expires (if specified).
		 * The lockInterruptibly method backs out if another thread sends an interrupt
		 * before the lock is acquired.
		 */

		System.out.println("/nExecutors ");
		/**
		 * In all of the previous examples, there's a close connection between the task
		 * being done by a new thread, as defined by its Runnable object, and the thread
		 * itself, as defined by a Thread object. This works well for small
		 * applications, but in large-scale applications, it makes sense to separate
		 * thread management and creation from the rest of the application. Objects that
		 * encapsulate these functions are known as executors. The following subsections
		 * describe executors in detail.
		 * 
		 * Executor Interfaces define the three executor object types. Thread Pools are
		 * the most common kind of executor implementation. Fork/Join is a framework
		 * (new in JDK 7) for taking advantage of multiple processors.
		 * 
		 * https://docs.oracle.com/javase/tutorial/essential/concurrency/exinter.html
		 * 
		 * https://docs.oracle.com/javase/tutorial/essential/concurrency/pools.html
		 * 
		 * https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html
		 * 
		 */

		System.out.println("/nConcurrent Collections ");
		/**
		 * https://docs.oracle.com/javase/tutorial/essential/concurrency/collections.html
		 * 
		 * 
		 * Concurrent Collections The java.util.concurrent package includes a number of
		 * additions to the Java Collections Framework. These are most easily
		 * categorized by the collection interfaces provided:
		 * 
		 * BlockingQueue defines a first-in-first-out data structure that blocks or
		 * times out when you attempt to add to a full queue, or retrieve from an empty
		 * queue. ConcurrentMap is a subinterface of java.util.Map that defines useful
		 * atomic operations. These operations remove or replace a key-value pair only
		 * if the key is present, or add a key-value pair only if the key is absent.
		 * Making these operations atomic helps avoid synchronization. The standard
		 * general-purpose implementation of ConcurrentMap is ConcurrentHashMap, which
		 * is a concurrent analog of HashMap. ConcurrentNavigableMap is a subinterface
		 * of ConcurrentMap that supports approximate matches. The standard
		 * general-purpose implementation of ConcurrentNavigableMap is
		 * ConcurrentSkipListMap, which is a concurrent analog of TreeMap.
		 */
		System.out.println("/n Atomic Variables - based on CASIng concept");
		/**
		 * https://docs.oracle.com/javase/tutorial/essential/concurrency/atomicvars.html
		 */
	}

}

//Let's use Lock objects to solve the deadlock problem we saw in Liveness.
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