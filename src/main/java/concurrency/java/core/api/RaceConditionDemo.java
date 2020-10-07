package concurrency.java.core.api;

/**
 * Race condition and Visibility problems
 */
public class RaceConditionDemo {

	public static void main(String[] args) {
		/**
		 * two threads has not share Runnable, that is why each has count= 1000000
		 */
		MyRunnablz run1 = new MyRunnablz();
		MyRunnablz run2 = new MyRunnablz();

		Thread t1 = new Thread(run1);
		Thread t2 = new Thread(run2);

		t1.start();
		t2.start();

		/**
		 * Here two threads SHARE Runnable, then count gets below 1000000. ... That is
		 * why result is UNPREDICTABLE ...
		 * 
		 * Reason happens during READ/WRITE, they override each others COUNT and so on,
		 * One thread is updating while second is reading
		 * 
		 * FIX ?
		 */

		/**
		 * Thread-0: 1892119
		 * 
		 * Thread-1: 1892119
		 */
		  //MyRunnablz run = new MyRunnablz();

		/**
		 * Unpredictable
		 * 
		 * Thread-1: 1979915
		 * 
		 * Thread-0: 2000000
		 */
		//MyRunnablz2 run = new MyRunnablz2();

//		Thread t1 = new Thread(run);
//		Thread t2 = new Thread(run);
//
//		t1.start();
//		// t1.join();
//		t2.start();

	}
}

class MyRunnablz implements Runnable {

	// can cause race condition / write visibility issue
	private int count = 0;

	@Override
	public void run() {
		for (int i = 0; i < 1000000; i++) {
			this.count++;
		}
		System.out.println(Thread.currentThread().getName() + ": " + count);
	}
}

class MyRunnablz2 implements Runnable {

	// can cause race condition / write visibility issue
	private int count = 0;

	@Override
	public void run() {
		 synchronized (this) {
			for (int i = 0; i < 1000000; i++) {
				this.count++;
			}
			System.out.println(Thread.currentThread().getName() + ": " + count);
		 }
	}
}
