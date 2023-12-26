package concurrency.java.core.api;

public class StopAThread {
	// https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html
	public static void main(String[] args) {
		MyZRunnable myRunnable = new MyZRunnable();

		Thread thread = new Thread(myRunnable);

		thread.start();

		try {
			Thread.sleep(5L * 1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		myRunnable.doStop();
	}

}

/**
 * <pre>
 * Stop a Thread
 * 
	Stopping a Java Thread requires some preparation of your thread implementation code. 
	The Java Thread class contains a stop() method, but it is deprecated. The original stop() 
	method would not provide any guarantees about in what state the thread was stopped. That means, 
	that all Java objects the thread had access to during execution would be left in an unknown state. 
	If other threads in your application also has access to the same objects, your application 
	could fail unexpectedly and unpredictably.
 * </pre>
 *
 */
class MyZRunnable implements Runnable {

	private boolean doStop = false;

	public synchronized void doStop() {
		this.doStop = true;
	}

	private synchronized boolean keepRunning() {
		return this.doStop == false;
	}

	@Override
	public void run() {
		while (keepRunning()) {
			// keep doing what this thread should do.
			System.out.println("Running ... ");

			try {
				Thread.sleep(2L * 1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}