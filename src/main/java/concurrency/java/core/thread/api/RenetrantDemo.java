package concurrency.java.core.thread.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Synchronized Block Reentrance
 * 
 * Once a thread has entered a synchronized block the thread is said to "hold
 * the lock" on the monitoring object the synchronized block is synchronized on.
 * If the thread calls another method which calls back to the first method with
 * the synchronized block inside, the thread holding the lock can reenter the
 * synchronized block. It is not blocked just because a thread (itself) is
 * holding the lock. Only if a differen thread is holding the lock. Look at this
 * example:
 *
 */
public class RenetrantDemo {

	List<String> elements = new ArrayList<String>();

	/**
	 * Forget for a moment that the above way of counting the elements of a list
	 * makes no sense at all. Just focus on how inside the synchronized block inside
	 * the count() method calls the count() method recursively. Thus, the thread
	 * calling count() may eventually enter the same synchronized block multiple
	 * times. This is allowed. This is possible.
	 * 
	 * Synchronized Blocks in Cluster Setups
	 * 
	 * Keep in mind that a synchronized block only blocks threads within the same
	 * Java VM from entering that code block. If you have the same Java application
	 * running on multiple Java VMs - in a cluster - then it is possible for one
	 * thread within each Java VM to enter that synchronized block at the same time.
	 * 
	 * If you need synchronization across all Java VMs in a cluster you will need to
	 * use other synchronization mechanisms than just a synchronized block.
	 */
	public int count() {
		if (elements.size() == 0) {
			return 0;
		}
		synchronized (this) {
			elements.remove(this.count() - 7); // just example
			return 1 + count();
		}
	}

}
