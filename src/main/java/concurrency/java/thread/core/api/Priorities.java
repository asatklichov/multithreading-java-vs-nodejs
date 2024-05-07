package concurrency.java.thread.core.api;

/**
 * Thread Priorities and yield( )
 * 
 * <pre>
 * 
 * To understand yield(), you must understand the concept of thread priorities. 
 * Threads always run with some priority, usually represented as a number between 1 
 * and 10 (although in some cases the range is less than 10). 
 * 
 * The scheduler in most 
 * JVMs uses preemptive, priority-based scheduling (which implies some sort 
 * of time slicing). This does not mean that all JVMs use time slicing. The JVM 
 * specification does not require a VM to implement a time-slicing scheduler, where 
 * each thread is allocated a fair amount of time and then sent back to runnable to give 
 * another thread a chance. 
 * 
 * Although many JVMs do use time slicing, some may use 
 * a scheduler that lets one thread stay running until the thread completes its run() 
 * method.
 * 
 * In most JVMs, however, the scheduler does use thread priorities in one important 
 * way: If a thread enters the runnable state, and it has a higher priority than any of 
 * the threads in the pool and a higher priority than the currently running thread, 
 * the lower-priority running thread usually will be bumped back to runnable and the 
 * highest-priority thread will be chosen to run.
 * 
 * In other words, at any given time the 
 * currently running thread usually will not have a priority that is lower than any of 
 * the threads in the pool. In most cases, the running thread will be of equal or greater 
 * priority than the highest priority threads in the pool. This is as close to a guarantee 
 * about scheduling as you'll get from the JVM specification, so you must never rely on 
 * thread priorities to guarantee the correct behavior of your program.
 * </pre>
 * 
 */
public class Priorities {
	public static void main(String[] args) {
		FooRunnable r = new FooRunnable();
		Thread t = new Thread(r);
		t.setPriority(8);
		t.start();
	}

}
