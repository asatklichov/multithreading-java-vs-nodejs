package concurrency.java.core.api;

import java.util.concurrent.atomic.AtomicBoolean;

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
class PreventThreadExecution {

}

class UnsafeStopMethod {
	/**
	 * As part of Java 11, Oracle finally removes the long time deprecated stop()
	 * and destroy() methods from the java.lang.Thread class.
	 * 
	 * The stop() method was considered unsafe. If a thread was stopped externally,
	 * all the monitors that the thread held became unlocked instantaneously. That
	 * could leave shared resources and variables in an unsafe, unstable meta-state.
	 */
	public static void main(String[] args) {
		System.out.println("stop() method is unsafe");
		// Consider the SharedClass class
		/**
		 * If an object of that class is shared by multiple threads, the swap() and the
		 * increment() operations should be atomic. In other words, all or nothing. No
		 * intermediate state should ever been exposed.
		 * 
		 * If a thread executing the swap() method is stopped by the stop() method after
		 * executing a = b; but before this.b = temp;, the state of the shared variables
		 * a and b in the sharedObject are left in a corrupted and incorrect state.
		 * Moreover, other threads can continue operating on those variables and calling
		 * the synchronized methods. And assume they are operating on correct values.
		 */
		System.out.println("destroy() method  has opposite problem");
		/**
		 * The destroy() method had the opposite problem. The method has never actually
		 * been implemented but the idea was to stop the thread without having any
		 * cleanup. In other words, if the thread was holding any monitors, they would
		 * stay locked forever, after a thread has been destroyed.
		 * 
		 * Back to the code example. If a thread executing the swap() method was
		 * destroyed, the remaining threads would not be able to call any methods of the
		 * shared object of type SharedClass, a situation that can also be described as
		 * a deadlock.
		 */

	}
}

class SharedClass {
	private int a = 1;
	private int b = 2;

	public synchronized void swap() {
		int temp = this.a;
		this.a = this.b;
		this.b = temp;
	}

	public synchronized void increment() {
		this.a++;
		this.b++;
	}
}

/**
 * 
 * See from CORE API
 * {@link concurrency.java.concurrent.api.ThreadInterruptionDemo }
 * 
 * 
 * Java Thread interrupt() method
 * 
 * <pre>
 *The interrupt() method of thread class is used to interrupt the thread. 
 *
 *If any thread is in sleeping or waiting state (i.e.
 * sleep() or wait() is invoked) then using the interrupt() method, we can
 * interrupt the thread execution by throwing InterruptedException.
 * 
 * If the thread is not in the sleeping or waiting state then calling the
 * interrupt() method performs a normal behavior and doesn't interrupt the
 * thread but sets the interrupt flag to true.
 * 
 * https://www.javatpoint.com/java-thread-interrupt-method
 * </pre>
 */

public class ThreadInterruptionDemo extends Thread {
	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println("javatpoint");
		} catch (InterruptedException e) {
			/*
			 * here we throw a new exception so thread will stop working.
			 */
			throw new RuntimeException("-> Thread interrupted..." + e);

		} finally {
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Yes, thread interrupted, " + Thread.currentThread().getState());
			} else {
				System.out.println("No, thread state is" + Thread.currentThread().getState());
			}
		}
	}

	public static void main(String args[]) {
		ThreadInterruptionDemo t1 = new ThreadInterruptionDemo();
		t1.start();
		try {
			/*
			 * after interrupting the thread, we throw a new exception so it will stop
			 * working. See above
			 */

			/**
			 * To stop threads in Java, we rely on a co-operative mechanism called
			 * Interruption. The concept is very simple. To stop a thread, all we can do is
			 * deliver it a signal, aka interrupt it, requesting that the thread stops
			 * itself at the next available opportunity. That’s all. There is no telling
			 * what the receiver thread might do with the signal: it may not even bother to
			 * check the signal; or even worse ignore it.
			 * 
			 * 
			 * The call to interrupt() causes the isInterrupted() method to return true
			 * 
			 * If the thread is blocked, or waiting, then the corresponding method will
			 * throw an InterruptedException
			 * 
			 * The methods wait()/ notify(), join() throw InterruptedException
			 */
			t1.interrupt();
		} catch (Exception e) {
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Yes, thread intrrupted, " + Thread.currentThread().getState());
			}
			System.out.println("Thread Interrupted and Exception handled " + e);
		}
	}
}

/**
 * after interrupting the thread, we handle the exception, so it will break out
 * from the sleeping state but will not stop working
 *
 */
class ThreadNotStopRunningDemo extends Thread {
	public void run() {
		try {
			// Here current threads goes to sleeping state
			// Another thread gets the chance to execute
			Thread.sleep(500);
			System.out.println("javatpoint");
		} catch (InterruptedException e) {
			/**
			 * Here we catch the Exception, so THREAD will not be stopped
			 */
			System.out.println("Exception handled " + e);
		}
		System.out.println("thread is running...");
		if (Thread.currentThread().isInterrupted()) {
			System.out.println("Yes, thread interrupted, " + Thread.currentThread().getState());
		} else {
			System.out.println("No, thread state is" + Thread.currentThread().getState());
		}
		;
	}

	public static void main(String args[]) {
		ThreadNotStopRunningDemo t1 = new ThreadNotStopRunningDemo();
		ThreadNotStopRunningDemo t2 = new ThreadNotStopRunningDemo();
		// call run() method
		t1.start();
		// interrupt the thread
		t1.interrupt();

	}
}

class StoppableThread implements Runnable {

	private volatile boolean isCancelled = false;

	public void run() {
		while (!isCancelled) {
			System.out.println("Thread is running with all its might!");

			try {
				Thread.sleep(1000);
				// continue executing
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// cleanup and exit
		System.out.println("Thread stopped");
	}

	public void cancel() {
		isCancelled = true;
	}

	public static void main(String args[]) throws InterruptedException {
		StoppableThread job = new StoppableThread();
		Thread thread = new Thread(job);
		thread.start();
		Thread.sleep(2000);
		job.cancel();

	}
}

//https://docs.oracle.com/javase/tutorial/essential/concurrency/index.html

class SunSimpleSample {

	// Display a message, preceded by
	// the name of the current thread
	static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}

	private static class MessageLoop implements Runnable {
		public void run() {
			String importantInfo[] = { "Mares eat oats", "Does eat oats", "Little lambs eat ivy",
					"A kid will eat ivy too" };
			try {
				for (int i = 0; i < importantInfo.length; i++) {
					// Pause for 4 seconds
					Thread.sleep(4000);
					// Print a message
					threadMessage(importantInfo[i]);
				}
			} catch (InterruptedException e) {
				threadMessage("I wasn't done!");
			}
		}
	}

	public static void main(String args[]) throws InterruptedException {

		// Delay, in milliseconds before
		// we interrupt MessageLoop
		// thread (default one hour).
		long patience = 1000 * 60 * 60;

		// If command line argument
		// present, gives patience
		// in seconds.
		if (args.length > 0) {
			try {
				patience = Long.parseLong(args[0]) * 1000;
			} catch (NumberFormatException e) {
				System.err.println("Argument must be an integer.");
				System.exit(1);
			}
		}

		threadMessage("Starting MessageLoop thread");
		long startTime = System.currentTimeMillis();
		Thread t = new Thread(new MessageLoop());
		t.start();

		threadMessage("Waiting for MessageLoop thread to finish");
		// loop until MessageLoop
		// thread exits
		while (t.isAlive()) {
			threadMessage("Still waiting...");
			// Wait maximum of 1 second
			// for MessageLoop thread
			// to finish.
			t.join(1000);
			if (((System.currentTimeMillis() - startTime) > patience) && t.isAlive()) {
				threadMessage("Tired of waiting!");
				t.interrupt();
				// Shouldn't be long now
				// -- wait indefinitely
				t.join();
			}
		}
		threadMessage("Finally!");
	}
} 