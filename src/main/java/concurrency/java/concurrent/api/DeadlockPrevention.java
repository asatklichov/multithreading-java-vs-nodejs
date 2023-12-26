package concurrency.java.concurrent.api;

/**
 * Deadlock - https://jenkov.com/tutorials/java-concurrency/deadlock.html
 * Deadlock Prevention -
 * http://tutorials.jenkov.com/java-concurrency/deadlock-prevention.html
 * 
 * 
 * <pre>
       Lock Ordering
       Lock Timeout
       Deadlock Detection
 * </pre>
 */
public class DeadlockPrevention {

	/**
	 * <pre>
	 * Lock Timeout
	Another deadlock prevention mechanism is to put a timeout on lock attempts meaning a thread trying to obtain a 
	lock will only try for so long before giving up. If a thread does not succeed in taking all necessary locks within the 
	given timeout, it will backup, free all locks taken, wait for a random amount of time and then retry. The random amount 
	of time waited serves to give other threads trying to take the same locks a chance to take all locks, and thus let the 
	application continue running without locking.
	
	Here is an example of two threads trying to take the same two locks in different order, where the threads back up and retry:
	
	Thread 1 locks A
	Thread 2 locks B
	
	Thread 1 attempts to lock B but is blocked
	Thread 2 attempts to lock A but is blocked
	
	Thread 1's lock attempt on B times out
	Thread 1 backs up and releases A as well
	Thread 1 waits randomly (e.g. 257 millis) before retrying.
	
	Thread 2's lock attempt on A times out
	Thread 2 backs up and releases B as well
	Thread 2 waits randomly (e.g. 43 millis) before retrying.
	
	
	
	Deadlock Detection
	Deadlock detection is a heavier deadlock prevention mechanism aimed at cases in which lock ordering isn't 
	possible, and lock timeout isn't feasible.
	 * </pre>
	 */

}
