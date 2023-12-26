package concurrency.java.core.api;

/**
 * Deadlock occurs when one thread waits second one to release the lock
 * 
 * How to avoid deadlock
 * 
 * We can follow certain steps to avoid deadlock:
 * 
 * - If possible, avoid nested locks or unnecessary locks - Use Thread Join with
 * maximum-time that thread will take
 * 
 * - Or use same Lock order
 * 
 *
 */
class Deadlock {
	public static Object lock1 = new Object();
	public static Object lock2 = new Object();

	public static void main(String args[]) {
		Thread threadA = new Thread(() -> {
			synchronized (lock1) {
				System.out.println("Thread-A: Holding lock 1...");
				sleep(2000);
				System.out.println("Thread-A: Waiting for lock 2...");
				synchronized (lock2) {
					System.out.println("Thread-A: Holding lock 1 & 2...");
				}
			}
		});
		Thread threadB = new Thread(() -> {
			synchronized (lock2) {
				System.out.println("Thread-B: Holding lock 2...");
				sleep(2000);
				System.out.println("Thread-B: Waiting for lock 1...");

				synchronized (lock1) {
					System.out.println("Thread-B: Holding lock 1 & 2...");
				}
			}
		});
		threadA.start();
		threadB.start();
	}

	public static void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
	}
}

class DeadlockFixed {
	public static Object lock1 = new Object();
	public static Object lock2 = new Object();

	public static void main(String args[]) {
		Thread threadA = new Thread(() -> {
			synchronized (lock1) {
				System.out.println("Thread-A: Holding lock 1...");
				sleep(2000);
				System.out.println("Thread-A: Waiting for lock 2...");
				synchronized (lock2) {
					System.out.println("Thread-A: Holding lock 1 & 2...");
				}
			}
		});
		Thread threadB = new Thread(() -> {
			synchronized (lock1) {
				System.out.println("Thread-B: Holding lock 2...");
				sleep(2000);
				System.out.println("Thread-B: Waiting for lock 1...");

				synchronized (lock2) {
					System.out.println("Thread-B: Holding lock 1 & 2...");
				}
			}
		});
		threadA.start();
		threadB.start();
	}

	public static void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
	}
}

class Deadlock2 {

	public static void main(String[] args) throws InterruptedException {
		Regard regard = new Regard();
		Runnable r1 = () -> regard.hello();
		Runnable r2 = () -> regard.hi();

		Thread t1 = new Thread(r1);
		t1.start();

		Thread t2 = new Thread(r2);
		t2.start();

		t1.join();
		t2.join();
	}
}

class Regard {

	private Object monitor1 = new Object();
	private Object monitor2 = new Object();

	public void hello() {
		synchronized (monitor1) {
			System.out.println(Thread.currentThread().getName() + " hello");
			hi();
		}
	}

	public void hi() {
		synchronized (monitor2) { //
			System.out.println(Thread.currentThread().getName() + " hi");
			handShake();
		}
	}

	public void handShake() {
		synchronized (monitor1) { //
			System.out.println(Thread.currentThread().getName() + " uses cafee machine");
		}
	}

}

class Deadlock2Fixed {

	public static void main(String[] args) throws InterruptedException {
		Regard2 regard = new Regard2();
		Runnable r1 = () -> regard.hello();
		Runnable r2 = () -> regard.hi();

		Thread t1 = new Thread(r1);
		t1.start();

		Thread t2 = new Thread(r2);
		t2.start();

		t1.join();
		t2.join();
	}
}

class Regard2 {

	private Object monitor1 = new Object();
	private Object monitor2 = new Object();

	public void hello() {
		synchronized (monitor1) {
			System.out.println(Thread.currentThread().getName() + " hello");
			hi();
		}
	}

	public void hi() {
		synchronized (monitor2) { //
			System.out.println(Thread.currentThread().getName() + " hi");
			handShake();
		}
	}

	public void handShake() {
		synchronized (monitor2) { //
			System.out.println(Thread.currentThread().getName() + " uses cafee machine");
		}
	}

}

class DeadLock3 {

	/**
	 * monitor objects
	 */
	private Object lock1 = new Object();
	private Object lock2 = new Object();

	public void one() {
		synchronized (lock1) {
			System.out.println("[" + Thread.currentThread().getName() + "] in method one()");
			two();
		}
	}

	public void two() {
		synchronized (lock2) {
			System.out.println("[" + Thread.currentThread().getName() + "] in method two()");
			three();
		}
	}

	public void three() {
		synchronized (lock1) {
			System.out.println("[" + Thread.currentThread().getName() + "] in method three()");
			// not calls other methods
		}
	}

	public static void main(String[] args) throws InterruptedException {

		DeadLock3 obj = new DeadLock3();

		Runnable r1 = () -> obj.one();
		Runnable r2 = () -> obj.two();

		Thread t1 = new Thread(r1);
		t1.start();

		Thread t2 = new Thread(r2);
		t2.start();

		t1.join();
		t2.join();
	}
}

class DeadLock3Fixed1 {

	/**
	 * monitor objects
	 */
	private Object lock1 = new Object();
	private Object lock2 = new Object();

	public void one() {
		synchronized (lock1) {
			System.out.println("[" + Thread.currentThread().getName() + "] in method one()");
			two();
		}
	}

	public void two() {
		synchronized (lock2) {
			System.out.println("[" + Thread.currentThread().getName() + "] in method two()");
			three();
		}
	}

	public void three() {
		synchronized (lock2) {
			System.out.println("[" + Thread.currentThread().getName() + "] in method three()");
			// not calls other methods
		}
	}

	public static void main(String[] args) throws InterruptedException {

		DeadLock3Fixed1 obj = new DeadLock3Fixed1();

		Runnable r1 = () -> obj.one();
		Runnable r2 = () -> obj.two();

		Thread t1 = new Thread(r1);
		t1.start();

		Thread t2 = new Thread(r2);
		t2.start();

		t1.join();
		t2.join();
	}
}

class DeadLock3Fixed2 {

	/**
	 * monitor objects
	 */
	private Object lock1 = new Object();
	private Object lock2 = new Object();

	public void one() {
		synchronized (lock1) {
			System.out.println("Thread-1: Holding lock 1...");
			sleep(2000);
			System.out.println("Thread-1: Waiting for lock 2...");
			synchronized (lock2) {
				System.out.println("Thread-1: Holding lock 1 & 2...");
			}
		}
	}

	public void two() {
		synchronized (lock1) {
			System.out.println("[" + Thread.currentThread().getName() + "] in method two()");
			System.out.println("Thread-2: Holding lock 2...");
			sleep(2000);
			System.out.println("Thread-2: Waiting for lock 1...");

			synchronized (lock2) {
				System.out.println("Thread-2: Holding lock 1 & 2...");
			}
		}
	}

	public static void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) throws InterruptedException {

		DeadLock3Fixed2 obj = new DeadLock3Fixed2();

		Runnable r1 = () -> obj.one();
		Runnable r2 = () -> obj.two();

		Thread t1 = new Thread(r1);
		t1.start();

		Thread t2 = new Thread(r2);
		t2.start();

		t1.join();
		t2.join();
	}

}
