package concurrency.java.concurrent.api;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * see {@link FairLockDemo}
 * 
 * see also {@link MyReadWriteLock} * see {@link LocksInJava}
 * 
 * 
 * If a thread is not granted CPU time because other threads grab it all, it is
 * called "starvation". The thread is "starved to death" because other threads
 * are allowed the CPU time instead of it. The solution to starvation is called
 * "fairness" - that all threads are fairly granted a chance to execute.
 * 
 * <pre>
 *  
 * Causes of Starvation in Java The following three common causes can lead to
 * starvation of threads in Java:
 * 
 * Threads with high priority swallow all CPU time from threads with lower
 * priority.
 * 
 * Threads are blocked indefinately waiting to enter a synchronized block,
 * because other threads are constantly allowed access before it.
 * 
 * Threads waiting on an object (called wait() on it) remain waiting
 * indefinitely because other threads are constantly awakened instead of it.
 * 
 * 
 * see https://jenkov.com/tutorials/java-concurrency/starvation-and-fairness.html
 * </pre>
 */
public class LockzFairnessandStarvation { 
}

class Synchronizer {
	MyLock lock = new MyLock();

	public void doSynchronized() throws InterruptedException {
		this.lock.lock();
		// critical section, do a lot of work which takes a long time
		this.lock.unlock();
	}

}

/**
 * If you look at the Synchronizer class above and look into this Lock
 * implementation you will notice that threads are now blocked trying to access
 * the lock() method, if more than one thread calls lock() simultanously.
 * Second, if the lock is locked, the threads are blocked in the wait() call
 * inside the while(isLocked) loop in the lock() method. Remember that a thread
 * calling wait() releases the synchronization lock on the Lock instance, so
 * threads waiting to enter lock() can now do so. The result is that multiple
 * threads can end up having called wait() inside lock().
 * 
 * As stated earlier synchronized blocks makes no guarantees about what thread
 * is being granted access if more than one thread is waiting to enter. Nor does
 * wait() make any guarantees about what thread is awakened when notify() is
 * called. So, the current version of the Lock class makes no different
 * guarantees with respect to fairness than synchronized version of
 * doSynchronized(). But we can change that.
 * 
 * The current version of the Lock class calls its own wait() method. If instead
 * each thread calls wait() on a separate object, so that only one thread has
 * called wait() on each object, the Lock class can decide which of these
 * objects to call notify() on, thereby effectively selecting exactly what
 * thread to awaken.
 *
 */
class MyLock {
	private boolean isLocked = false;
	private Thread lockingThread = null;

	public synchronized void lock() throws InterruptedException {
		while (isLocked) {
			wait();
		}
		isLocked = true;
		lockingThread = Thread.currentThread();
	}

	public synchronized void unlock() {
		if (this.lockingThread != Thread.currentThread()) {
			throw new IllegalMonitorStateException("Calling thread has not locked this lock");
		}
		isLocked = false;
		lockingThread = null;
		notify();
	}
}

/**
 * A Fair Lock Below is shown the previous Lock class turned into a fair lock
 * called FairLock.
 * 
 * You will notice that the implementation has changed a bit with respect to
 * synchronization and wait() / notify() compared to the Lock class shown
 * earlier.
 *
 * 
 * Lock Fairness An unfair lock does not guarantee the order in which threads
 * waiting to lock the lock will be given access to lock it. That means, that a
 * waiting thread could risk waiting forever, if other threads keep trying to
 * lock the lock, and are given priority over the waiting thread. This situation
 * can lead to starvation. I cover starvation and fairness in more detail in my
 * Starvation and Fairness Tutorial.
 *
 * Ref:
 * https://jenkov.com/tutorials/java-concurrency/starvation-and-fairness.html
 */
class FairLock {
	private boolean isLocked = false;
	private Thread lockingThread = null;
	private List<QueueObject> waitingThreads = new ArrayList<QueueObject>();

	public void lock() throws InterruptedException {
		QueueObject queueObject = new QueueObject();
		boolean isLockedForThisThread = true;
		synchronized (this) {
			waitingThreads.add(queueObject);
		}

		while (isLockedForThisThread) {
			synchronized (this) {
				isLockedForThisThread = isLocked || waitingThreads.get(0) != queueObject;
				if (!isLockedForThisThread) {
					isLocked = true;
					waitingThreads.remove(queueObject);
					lockingThread = Thread.currentThread();
					return;
				}
			}
			try {
				queueObject.doWait();
			} catch (InterruptedException e) {
				synchronized (this) {
					waitingThreads.remove(queueObject);
				}
				throw e;
			}
		}
	}

	public synchronized void unlock() {
		if (this.lockingThread != Thread.currentThread()) {
			throw new IllegalMonitorStateException("Calling thread has not locked this lock");
		}
		isLocked = false;
		lockingThread = null;
		if (waitingThreads.size() > 0) {
			waitingThreads.get(0).doNotify();
		}
	}
}

class QueueObject {

	private boolean isNotified = false;

	public synchronized void doWait() throws InterruptedException {
		while (!isNotified) {
			this.wait();
		}
		this.isNotified = false;
	}

	public synchronized void doNotify() {
		this.isNotified = true;
		this.notify();
	}

	public boolean equals(Object o) {
		return this == o;
	}
}
