package concurrency.java.core.api;

/**
 * Preventing Thread Execution
 * 
 * <pre>
 * 
 * A thread that's been stopped usually means a thread that's moved to the dead state. 
 * But Objective 4.2 is also looking for your ability to recognize when a thread will get 
 * kicked out of running but not be sent back to either runnable or dead.
 * 
 * We are  concerned with the following:
 * 
 * ■ Sleeping
 * ■ Waiting
 * ■ Blocked because it needs an object's lock
 * 
 * --------------------------------------------------
 * Sleeping
 * 	
 * The sleep() method is a static method of class Thread. You use it in your code 
 * to "slow a thread down" by forcing it to go into a sleep mode before coming back to 
 * runnable (where it still has to beg to be the currently running thread). When a thread 
 * sleeps, it drifts off somewhere and doesn't return to runnable until it wakes up.
 * 
 * You do this by invoking the static Thread.sleep() method, giving it a time in 
 * milliseconds as follows:
 * 
 * try {
 *   Thread.sleep(5*60*1000);  // Sleep for 5 minutes
 * } catch (InterruptedException ex) { }
 * 
 * Notice that the sleep() method can throw a checked InterruptedException 
 * 
 * (you'll usually know if that is a possibility, since another thread has to explicitly do 
 * the interrupting), so you must acknowledge the exception with a handle or declare. 
 * Typically, you wrap calls to sleep() in a try/catch, as in the preceding code.
 * 
 * Still, using sleep() 
 * is the best way to help all threads get a chance to run! Or at least to guarantee that 
 * one thread doesn't get in and stay until it's done. When a thread encounters a sleep 
 * call, it must go to sleep for at least the specified number of milliseconds (unless 
 * it is interrupted before its wake-up time, in which case it immediately throws the 
 * InterruptedException).
 * 
 * Remember that sleep() is a static method, so don't be fooled into thinking that 
 * one thread can put another thread to sleep. You can put sleep() code anywhere, 
 * since all code is being run by some thread. When the executing code (meaning the 
 * currently running thread's code) hits a sleep() call, it puts the currently running 
 * thread to sleep.
 * 
 * --------------------------------------------------
 * 
 * 
 * --------------------------------------------------
 * 
 * 
 * --------------------------------------------------
 * 
 * </pre>
 * 
 */
public class ZPrevent_ThreadExecution {

}
