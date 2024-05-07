package concurrency.part1.thread.core.api;

/**
 * Thread Deadlock
 * 
 * 
 * <pre>
 * 
 * Perhaps the scariest thing that can happen to a Java program is deadlock.
 * Deadlock occurs when two threads are blocked, with each waiting for the
 * other's lock. Neither can run until the other gives up its lock, so they'll
 * sit there forever.
 * 
 * This can happen, for example, when thread A hits synchronized code, acquires
 * a lock B, and then enters another method (still within the synchronized code
 * it has the lock on) that's also synchronized. But thread A can't get the lock
 * to enter this synchronized code—block C—because another thread D has the lock
 * already. So thread A goes off to the waiting-for-the-C-lock pool, hoping that
 * thread D will hurry up and release the lock (by completing the synchronized
 * method).
 * 
 * But thread A will wait a very long time indeed, because while thread D picked
 * up lock C, it then entered a method synchronized on lock B. Obviously, thread
 * D can't get the lock B because thread A has it. And thread A won't release it
 * until thread D releases lock C. But thread D won't release lock C until after
 * it can get lock B and continue. And there they sit. The following example
 * demonstrates deadlock:
 * 
 * </pre>
 * 
 */
public class DeadlockRisk {
	private static class Resource {
		public int value;
	}

	private Resource resourceA = new Resource();
	private Resource resourceB = new Resource();

	/**
	 * 
	 * <pre>
	 * Assume that read() is started by one thread and write() is started by
	 * another. If there are two different threads that may read and write
	 * independently, there is a risk of deadlock at line 8 or 16.
	 * 
	 * The reader thread will have resourceA, the writer thread will have
	 * resourceB, and both will get stuck waiting for the other.
	 * 
	 * Code like this almost never results in deadlock because the CPU has to
	 * switch from the reader thread to the writer thread at a particular point
	 * in the code, and the chances of deadlock occurring are very small.
	 * 
	 * The application may work fine 99.9 percent of the time. The preceding
	 * simple example is easy to fix; just swap the order of locking for either
	 * the reader or the writer at lines 16 and 17 (or lines 8 and 9). More
	 * complex deadlock situations can take a long time to figure out.
	 * 
	 * Regardless of how little chance there is for your code to deadlock, the
	 * bottom line is, if you deadlock, you're dead. There are design approaches
	 * that can help avoid deadlock, including strategies for always acquiring
	 * locks in a predetermined order.
	 * 
	 * But that's for you to study and is beyond the scope of this book. We're
	 * just trying to get you through the exam. If you learn everything in this
	 * chapter, though, you'll still know more about threads than most
	 * experienced Java programmers.
	 * </pre>
	 */
	public int read() {
		synchronized (resourceA) { // May deadlock here
			synchronized (resourceB) {
				return resourceB.value + resourceA.value;
			}
		}
	}

	public void write(int a, int b) {
		synchronized (resourceB) { // May deadlock here
			synchronized (resourceA) {
				resourceA.value = a;
				resourceB.value = b;
			}
		}
	}
}
