package concurrency.part1.thread.core.api;

/**
 * Assume the following method is properly synchronized and called from a thread
 * A on an object B:
 * 
 * 
 * wait(2000);
 * 
 * <pre>
 * 
 * After calling this method, when will the thread A become a candidate to get another turn at 
 * the CPU?
 * A. After object B is notified, or after two seconds
 * B. After the lock on B is released, or after two seconds
 * C. Two seconds after object B is notified
 * D. Two seconds after lock B is released
 * 
 * 
 * Answer:
 *     ✓    A is correct. Either of the two events will make the thread a candidate for running again. 
 *       B is incorrect because a waiting thread will not return to runnable when the lock is 
 * released, unless a notification occurs. C is incorrect because the thread will become a 
 * candidate immediately after notification. D is also incorrect because a thread will not come 
 * out of a waiting pool just because a lock has been released. (Objective 4.4)
 * </pre>
 * 
 */
class Thread_Interactions_Ex1 {

}

/**
 * Which are true? (Choose all that apply.)
 * 
 * <pre>
 * 
 * A. The notifyAll() method must be called from a synchronized context
 * 
 * B. To call wait(), an object must own the lock on the thread
 * 
 * C. The notify() method is defined in class java.lang.Thread
 * 
 * D. When a thread is waiting as a result of wait(), it releases its lock
 * 
 * E. The notify() method causes a thread to immediately release its lock
 * 
 * F.  The difference between notify() and notifyAll() is that notifyAll() notifies all 
 * waiting threads, regardless of the object they're waiting on
 * 
 * 
 * Answer:
 *      ✓    A is correct because notifyAll() (and wait() and notify()) must be called from within 
 * a synchronized context. D is a correct statement.
 *        B is incorrect because to call wait(), the thread must own the lock on the object that 
 * wait() is being invoked on, not the other way around. C is wrong because notify() is 
 * defined in java.lang.Object. E is wrong because notify() will not cause a thread to 
 * release its locks. The thread can only release its locks by exiting the synchronized code. F is 
 * wrong because notifyAll() notifies all the threads waiting on a particular locked object, 
 * not all threads waiting on any object. (Objective 4.4)
 * </pre>
 * 
 */
class Thread_Interactions_Ex2 {

}

// What is the result of trying to compile and run this program?
/**
 * What is the result of trying to compile and run this program?
 * 
 * <pre>
 * 
 *    A.  It fails to compile because the IllegalMonitorStateException of wait() is not dealt 
 * with in line 7 
 * 
 *    B.  1 2 3 
 *    
 *    C.  1 3
 *    
 *    D.  1 2
 *    
 *    E. At runtime, it throws an IllegalMonitorStateException when trying to wait
 *    
 *    F. It will fail to compile because it has to be synchronized on the this object
 *    
 *    
 *    Answer:
 *   ✓    D is correct. 1 and 2 will be printed, but there will be no return from the wait call because 
 * no other thread will notify the main thread, so 3 will never be printed. It's frozen at line 7.
 *       A is incorrect; IllegalMonitorStateException is an unchecked exception. B and C 
 * are incorrect; 3 will never be printed, since this program will wait forever. E is incorrect 
 * because IllegalMonitorStateException will never be thrown because the wait() 
 * is done on args within a block of code synchronized on args. F is incorrect because any 
 * object can be used to synchronize on and this and static don't mix.  (Objective 4.4)
 * </pre>
 * 
 */
class WaitTestz {
	public static void main(String[] args) {
		System.out.print("1 ");
		synchronized (args) {
			System.out.print("2 ");
			try {
				/**
				 * Note that: if the code does not acquire a lock on "args"
				 * before calling args.wait(), so it throws an
				 * IllegalMonitorStateException.
				 */
				args.wait();
			} catch (InterruptedException e) {
			}
		}
		System.out.print("3 ");
	}

}

// other
class TestX {
	public static void main(String[] args) {
		final Foo f = new Foo();
		Thread t = new Thread(new Runnable() {
			public void run() {
				f.doStuff();
			}
		});
		Thread g = new Thread() {
			public void run() {
				f.doStuff();
			}
		};
		t.start();
		g.start();
	}
}

class Foo {
	int x = 5;

	public void doStuff() {
		if (x < 10) {
			// nothing to do
			try {
				wait();
			} catch (InterruptedException ex) {
			}
		} else {
			System.out.println("x is " + x++);
			if (x >= 10) {
				notify();
			}
		}
	}
}
