package concurrency.java.thread.core.api;

/**
 * A thread state. A thread can be in one of the following states:
 * 
 * NEW A thread that has not yet started is in this state.
 * 
 * RUNNABLE A thread executing in the Java virtual machine is in this state.
 * 
 * BLOCKED A thread that is blocked waiting for a monitor lock is in this state.
 * 
 * WAITING A thread that is waiting indefinitely for another thread to perform a
 * particular action is in this state.
 * 
 * TIMED_WAITING A thread that is waiting for another thread to perform an
 * action for up to a specified waiting time is in this state.
 * 
 * TERMINATED A thread that has exited is in this state. A thread can be in only
 * one state at a given point in time. These states are virtual machine states
 * which do not reflect any operating system thread states.
 * 
 * 
 */
class ThreadSTATES {

}

//https://www.geeksforgeeks.org/lifecycle-and-states-of-a-thread-in-java/

//Java program to demonstrate thread states

public class AThreadStateDemo implements Runnable {
	public static AThreadStateDemo job;
	public static Thread thread1;

	public static void main(String[] args) {
		job = new AThreadStateDemo();
		thread1 = new Thread(job); //thread1 uses AThreadStateDemo as a TASK

		// thread1 created and is currently in the NEW  state.
		System.out.println("State of thread1 after creating it - " + thread1.getState());
		thread1.start();

		// thread1 moved to Runnable state
		System.out.println("State of thread1 after calling .start() method on it - " + thread1.getState());
		System.out.println();
		try {
			// main thread joins after thread1
			// waiting for thread1 to die/terminate
			thread1.join();

			System.out.println("State of thread1 when it has finished it's execution - " + thread1.getState());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Implements java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		MiThread myJob = new MiThread();
		Thread thread2 = new Thread(myJob);

		// thread1 created and is currently in the NEW
		// state.
		System.out.println("State of thread2 after creating it - " + thread2.getState());
		thread2.start();

		// thread2 moved to Runnable state
		System.out.println("State of thread2 after calling .start() method on it - " + thread2.getState());
		System.out.println();

		// moving thread1 to timed waiting state
		try {
			// moving thread1 to timed waiting state
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("State of thread2 after calling .sleep() method on it - " + thread2.getState());

		try {
			// thread 1 joins after thread2
			// waiting for thread2 to die
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("State of thread2 when it has finished it's execution - " + thread2.getState());
	}
}

class MiThread implements Runnable {
	public void run() {
		// moving thread2 to timed waiting state
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(
				"State of thread1 while it called join() method on thread2 -" + AThreadStateDemo.thread1.getState());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
