package concurrency.java.thread.core.api;

/**
 * What Happens If a Thread Can't Get the Lock?
 * 
 * <pre>
 * 
 * If a thread tries to enter a synchronized method and the lock is already taken, the 
 * thread is said to be blocked on the object's lock. Essentially, the thread goes into a 
 * kind of pool for that particular object and has to sit there until the lock is released 
 * and the thread can again become runnable/running. 
 * 
 * 
 * Just because a lock is released doesn't mean any particular thread will get it. 
 * 
 * There might be three threads waiting 
 * for a single lock, for example, and there's no guarantee that the thread that has 
 * waited the longest will get the lock first. 
 * When thinking about blocking, it's important to pay attention to which objects 
 * are being used for locking.
 * 
 * ■ Threads calling non-static synchronized methods in the same class will 
 * only block each other if they're invoked using the same instance.  That's 
 * because they each lock on this instance, and if they're called using two dif-
 * ferent instances, they get two locks, which do not interfere with each other.
 * 
 * ■ Threads calling static synchronized methods in the same class will always 
 * block each other—they all lock on the same Class instance.
 * 
 * ■ A static synchronized method and a non-static synchronized method 
 * will not block each other, ever.  The static method locks on a Class 
 * instance while the non-static method locks on the this instance—these 
 * actions do not interfere with each other at all.
 * 
 * ■ For synchronized blocks, you have to look at exactly what object has been 
 * used for locking.  (What's inside the parentheses after the word synchro-
 * nized?)  Threads that synchronize on the same object will block each other.  
 * Threads that synchronize on different objects will not.
 * 
 * </pre>
 * 
 */
public class Synchronized_Lock_notReleased {

}
