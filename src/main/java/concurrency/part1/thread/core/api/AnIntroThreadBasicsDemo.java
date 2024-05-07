package concurrency.part1.thread.core.api;

public class AnIntroThreadBasicsDemo {

	public static void main(String[] args) throws InterruptedException {
		/**
		 * 1-way
		 * 
		 * In this approach you can not extend your MyThread1 class with other class
		 */
		Thread t1 = new MyThread();
		t1.setName("MyThread");
		t1.start();

		/**
		 * 2-way
		 * 
		 * Better approach, implement Runnable Interface with your MyThread2 class.
		 * Motivates to separate Task and Worker concepts
		 */
		MyRunnable myRunnable = new MyRunnable();
		Thread t2 = new Thread(myRunnable, "MyRunnable");
		t2.start();

		/**
		 * 3-way via anonymous class
		 */
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				printInfo();
			}
		});
		t3.setName("MyAnThread");
		t3.start();

		Thread t = new Thread();
		t.start();

		t = new Thread() {
			@Override
			public void run() {
				System.out.println("Kindle GB");
				super.run();
			}
		};
		t.setName("KindleThread");
		t.start();

		Runnable r2 = () -> System.out.println(); 
		/**
		 * 4-way via Java 8 lambdas (functional interface - consumer)
		 */
		Runnable r = () -> {
			// printInfo();
			int x = 0;
			while (x++ < 10) { // true
				System.out.println(x + ". " + getCurrentThreadName() + " is running");
				sleep(1000);
			}
			// System.out.println(getCurrentThreadName() + " finished");
		};
		Thread t4 = new Thread(r, "Thread-By-Lambda"); // new Thread(r);
		/**
		 * If you want to keep Thread4 running after main thread terminated, make it
		 * Daemon thread
		 */
		// t4.setDaemon(true); // behavior will be different, thread will be take to
		// background

		t4.start();

		sleep(500);
		System.out.println("Stop Thread-2");
		myRunnable.terminate();// otherwise keeps running infinitively
		t4.join();
		System.out.println("--- END OF MAIN THREAD ----");
	}

	public static void printInfo() {
		System.out.println(getCurrentThreadName() + " is running ");
		System.out.println(getCurrentThreadName() + " is finished ");
	}

	public static String getCurrentThreadName() {
		// you can reference to Thread which executes Runnable/TASK
		Thread currentThread = Thread.currentThread();
		return currentThread.getName();
	}

	public static void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
	}

}

class MyThread extends Thread {

	@Override
	public void run() {
		AnIntroThreadBasicsDemo.printInfo();
	}

}

class MyRunnable implements Runnable {

	private boolean terminated;

	@Override
	public void run() {
		String name = AnIntroThreadBasicsDemo.getCurrentThreadName();
		System.out.println(name + " is running");
		while (!isTerminated()) {
			System.out.println(name + "  is sleeping");
			AnIntroThreadBasicsDemo.sleep(1000);
		}

		System.out.println(name + " FINISHED");
	}

	public synchronized boolean isTerminated() {
		return terminated;
	}

	public synchronized void terminate() {
		this.terminated = true;
	}
}
