package concurrency.java.thread.core.api;

/**
 * The Thread Scheduler
 * 
 * <pre>
 * 
 * The thread scheduler is the part of the JVM (although most JVMs map Java threads 
 * directly to native threads on the underlying OS) that decides which thread should 
 * run at any given moment, and also takes threads out of the run state. 
 * 
 * Assuming a  single processor machine, only one thread can actually run at a time. Only one stack 
 * can ever be executing at one time. And it's the thread scheduler that decides which 
 * thread—of all that are eligible—will actually run. When we say eligible, we really 
 * mean in the runnable state.
 * 
 * 
 * Any thread in the runnable state can be chosen by the scheduler to be the one and 
 * only running thread. If a thread is not in a runnable state, then it cannot be chosen to be 
 * the currently running thread. And just so we're clear about how little is guaranteed here:
 * The order in which runnable threads are chosen to run is not guaranteed.
 * Although queue behavior is typical, it isn't guaranteed. 
 * 
 * Queue behavior means 
 * that when a thread has finished with its "turn," it moves to the end of the line of the 
 * runnable pool and waits until it eventually gets to the front of the line, where it can 
 * be chosen again. In fact, we call it a runnable pool, rather than a runnable queue, to 
 * help reinforce the fact that threads aren't all lined up in some guaranteed order.
 * Although we don't control the thread scheduler (we can't, for example, tell a 
 * specific thread to run), we can sometimes influence it. The following methods give us 
 * some tools for influencing the scheduler. Just don't ever mistake influence for control.
 * 
 * 
 * 
 * Methods from the java.lang.Thread Class    Some of the methods that can 
 * help us influence thread scheduling are as follows:
 * 
 * public static void sleep(long millis) throws InterruptedException
 * 
 * public static void yield()
 * 
 * public final void join() throws InterruptedException
 * 
 * public final void setPriority(int newPriority)
 * 
 * Note that both sleep() and join() have overloaded versions not shown here.
 * 
 * -----------------------------------------------------------
 * 
 * Methods from the java.lang.Object Class    Every class in Java inherits the 
 * following three thread-related methods:
 *     public final void wait() throws InterruptedException
 *     
 * public final void notify()
 * 
 * public final void notifyAll()
 * 
 * The wait() method has three overloaded versions (including the one listed here).
 * 
 * We'll look at the behavior of each of these methods in this chapter. First, though, 
 * we're going to look at the different states a thread can be in.
 * </pre>
 * 
 */
public class Scheduler {

}
