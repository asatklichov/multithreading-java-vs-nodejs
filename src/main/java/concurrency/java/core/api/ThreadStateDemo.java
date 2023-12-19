package concurrency.java.core.api;

//https://www.geeksforgeeks.org/lifecycle-and-states-of-a-thread-in-java/

//Java program to demonstrate thread states

public class ThreadStateDemo implements Runnable {
	public static ThreadStateDemo obj;
	public static Thread thread1;

	public static void main(String[] args) {
		obj = new ThreadStateDemo();
		thread1 = new Thread(obj);

		// thread1 created and is currently in the NEW
		// state.
		System.out.println("State of thread1 after creating it - " + thread1.getState());
		thread1.start();

		// thread1 moved to Runnable state
		System.out.println("State of thread1 after calling .start() method on it - " + thread1.getState());
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
		MiThread myThread = new MiThread();
		Thread thread2 = new Thread(myThread);

		// thread1 created and is currently in the NEW
		// state.
		System.out.println("State of thread2 after creating it - " + thread2.getState());
		thread2.start();

		// thread2 moved to Runnable state
		System.out.println("State of thread2 after calling .start() method on it - " + thread2.getState());

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
				"State of thread1 while it called join() method on thread2 -" + ThreadStateDemo.thread1.getState());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


