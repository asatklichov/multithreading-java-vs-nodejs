package concurrency.part1.thread.core.api;

import java.util.Timer;
import java.util.TimerTask;

public class VisibilityDemo {

}

//From Java Puzzlers book
class LockMess extends Thread {

	private volatile boolean quittingTime = false;

	public void run() {
		while (!quittingTime) { // issue is quittingTime never set to true
			working();
			System.out.println("Still working...");
		}

		// once the work completed
		System.out.println("Coffee is good !");
	}

	private void working() {
		try {
			// Nicest work is - SLEEPing ;) ;)
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
	}

	//// quit() synchronized with same KEY as keepWorking()
	synchronized void quit() throws InterruptedException {
		quittingTime = true;
		System.out.println("Calling join() ...  ");
		// LOCK MESS happens here - RUN in DEBUG mode to SIMULATE (no breakpoint usage)
		join();// THREAD is BLOCKED here, actually KEY is LOST (normally join() keeps KEY, BUT
				// here LOST) .. (wait() release it... )
		// join() [not synchronized] --> join(0) [synchronized] method --> calls WAIT()
		System.out.println("Thread completed its work");

		/**
		 * BUT, problem here is, Thread is BLOCKED on JOIN call, and RELEASES the KEY to
		 * let other thread execute keepWorking() method.
		 * 
		 * 
		 * Open join() to investigate deeply (uses wait/notify pattern). join() calls
		 * join(0) which is SYNCHRONIZED, and since Java is RE-ENTRANT, JAVA can call
		 * this join(0) method with current KEY. And join(0) calls wait() which releases
		 * the KEY.
		 * 
		 * So, above JOIN method release the KEY to let other thread to continue run.
		 * 
		 * 
		 * wait() - release key
		 * 
		 * I guess that the reason to this is that join() method does releases the
		 * lock/s obtained on the thread(if any) as it can be seen from the code below
		 * that join() method internally uses the call to the wait() method. So in case
		 * you use a.join() and also synchronize on a then you lose the lock. But in
		 * case you use a.join() and synchronize on aa( any string) then you have the
		 * lock on aa and the thread goes to wait.
		 * 
		 * 
		 * According to Kathy Sierra and Bert Bates "Thread doesn't give up the lock on
		 * invocation of join “ -
		 * https://coderanch.com/t/242419/certification/invocation-join-release-locks-objects
		 * But here main thread first acquires a lock on object 'a' and then invokes
		 * a.join which pushes the out of the running state. The main thread cannot be
		 * scheduled until the Thread A completes. This causes the other thread to start
		 * executing so it's run method begins and this thread tries to acquire the lock
		 * on object 'a' and successfully gets it (which is evident by the output). This
		 * can happen only if the call to a.join in main thread released the lock on
		 * object 'a' which is contrary to what the author says.
		 * 
		 * Actually, if you use a different object to lock on, say a String, you would
		 * see that the join() method will not release the lock, and the program will
		 * wait forever. But I am curious as to why if you lock the thread object
		 * itself, it appears that the join() method is releasing the lock.
		 * 
		 * 
		 */
	}

	synchronized void keepWorking() {
		quittingTime = false;
		System.out.println("Keep working ... ");
	}

	public static void main(String... args) throws InterruptedException {

		final LockMess worker = new LockMess();
		worker.start();

		/**
		 * Special object, like a Runnable but runs with auto timer.
		 */
		Timer t = new Timer(true); // Daemon thread
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				worker.keepWorking();
			}
		}, 500);// TimerTask is as Runnable but executes after 500 ms. to set quittingTime =
				// false;

		// as you see: keepWorking() is not called, BUT worker.quit() called, where
		// LOCK-MESS happens
		// if you use less than 400 not 500 - code is OK

		// keepWorking() synchronized with same KEY as quit()

		Thread.sleep(400);
		// set quittingTime = true; to stop thread - but never stops ;)
		// quit() synchronized with same KEY as keepWorking()
		worker.quit();

	}
}

class LockMessFixed extends Thread {

	// helps to fix LOCK mess done by join()
	// so join(0)->wait() can not use this - no REENTRANT allowed here
	private Object lock = new Object();
	private volatile boolean quittingTime = false;

	public void run() {
		while (!quittingTime) {
			working();
			System.out.println("Still working...");
		}

		System.out.println("Coffee is good !");
	}

	private void working() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
	}

	synchronized void quit() throws InterruptedException {
		/**
		 * LOCK MESS FIX, via change a KEY using special lock
		 * 
		 * So, here we HAVE DOUBLE SYNCHRONIZATION, and lock object is hidden and not
		 * visible to outside (join(0)-->wait())
		 * 
		 * 
		 * Actually, if you use a different object to lock on, you would see that the
		 * join() method will not release the lock, and the program will wait forever.
		 * But if not uses then it appears that the join() method is releasing the lock.
		 * 
		 */
		synchronized (lock) { // this lock object is hidden and not visible to outside
			quittingTime = true;
			System.out.println("Calling join");
			join();// wait thread completes its work
			System.out.println("Back from join");
		}
	}

	synchronized void keepWorking() {
		synchronized (lock) {
			quittingTime = false;
			System.out.println("Keep working");
		}
	}

	public static void main(String... args) throws InterruptedException {

		final LockMessFixed worker = new LockMessFixed();
		worker.start();

		Timer t = new Timer(true); // Daemon thread
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				worker.keepWorking();
			}

		}, 500);

		Thread.sleep(400);
		worker.quit();
	}
}

//does invocation of join release the locks on objects the current holds?
//https://coderanch.com/t/242419/certification/invocation-join-release-locks-objects
class A extends Thread {
	static A a;

	public void run() {
		try {
			synchronized (a) {
				System.out.println(Thread.currentThread().getName() + " acquired a lock on A");
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] ar) throws Exception {
		a = new A();
		a.start();
		synchronized (a) {
			System.out.println(Thread.currentThread().getName() + " acquired a lock on a");
			a.join();
			System.out.println(Thread.holdsLock(a));
		}
	}
}