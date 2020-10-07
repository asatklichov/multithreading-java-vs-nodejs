package concurrency.java.core.api;

import java.util.Collections;

/**
 * Thread may run in different order (UNPREDICTABLE), it's up to the scheduler,
 * and we don't control the scheduler!
 * 
 * 
 * There is a way, however, to start a thread but tell it not to run until some
 * other thread has finished.
 * 
 * You can do this with the join() method, which we'll look at a little later.
 * 
 * https://www.baeldung.com/java-thread-join
 * </pre>
 */
public class JoinDemo {

	/**
	 * There is a way, however, to start a thread but tell it not to run until some
	 * other thread has finished.
	 * 
	 * You can do this with the join() method, which we'll look at a little later.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// creating two threads
		ThreadJoining t1 = new ThreadJoining();
		t1.setName("Thread-A");
		ThreadJoining t2 = new ThreadJoining();
		t2.setName("Thread-B");
		ThreadJoining t3 = new ThreadJoining();
		t3.setName("Thread-C");

		t1.start();

		/*
		 * starts second thread after when first thread t1 has died.
		 */
		try {
			System.out.println("Current Thread: " + Thread.currentThread().getName());
			t1.join();
		} catch (InterruptedException ex) {
			System.err.println("Exception: " + ex.getMessage());
		}

		t2.start();
		

		// starts t3 after when thread t2 has died.
		/**
		 * In this case, the calling thread waits for roughly 1 second for the thread t3
		 * to finish. If the thread t2 does not finish in this time period, the join()
		 * method returns control to the calling method.
		 */
		try {
			System.out.println("Current Thread: " + Thread.currentThread().getName());
			// t2.join();
			t2.join(1000);
		} catch (InterruptedException ex) {
			System.err.println("Exception: " + ex.getMessage());
		}

		t3.start();
	}

}

class ThreadJoining extends Thread {
	@Override
	public void run() {
		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep(500);
				System.out.print(Thread.currentThread().getName());
			} catch (InterruptedException ex) {
				System.err.println("Exception: " + ex.getMessage());
			}
			System.out.println(", " + i);
		}
		System.out.println();
	}
}

class JoinPuzzle {

	public static void main(String[] args) {
		System.out.println("Main thread");
		MyWorkerThread t1 = new MyWorkerThread();
		t1.setName("Thread-A");
		MyWorkerThread t2 = new MyWorkerThread();
		t2.setName("Thread-B");

//		t1.run();
//		t2.run();
//		

		t1.start();
		t2.start();

//		try {
//			t1.start();
//			t1.join();
//			// System.out.println("T1 - finished, isAlive = " + t1.isAlive());
//			t2.start();
//			t2.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		// here order may not be guaranteed, because t2 is not joined into main-thread 

	}
}

class MyWorkerThread extends Thread {
	static int i = 0;

	@Override
	public void run() {
		System.out.println();
		for (int k = 0; k < 1000; k++) {
			++i;
		}
		System.out.println(Thread.currentThread().getName() + ", i = " + i);
	}
}