package concurrency.part1.thread.core.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

//https://jenkov.com/tutorials/java-concurrency/deadlock-prevention.html
public class DeadlockPrevention {
	// 1. Lock Ordering
	// 2. Lock Timeout
	// 3. Deadlock Detection
	/*
	 * Deadlock detection is a heavier deadlock prevention mechanism aimed at cases
	 * in which lock ordering isn't possible, and lock timeout isn't feasible.
	 */

}
//https://www.javatpoint.com/how-to-avoid-thread-deadlock-in-java#:~:text=Use%20timeouts%3A%20When%20acquiring%20locks,lock%20to%20protect%20multiple%20resources.

//1. Avoid nested synchronization blocks
class DeadlockExample {
	static Object lock1 = new Object(); // Create object for lock1
	static Object lock2 = new Object(); // Create object for lock2

	public static void main(String[] args) {
// Create the first thread  
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				synchronized (lock1) {
					System.out.println("Thread 1: Holding lock 1...");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Thread 1: Waiting for lock 2...");
					synchronized (lock2) {
						System.out.println("Thread 1: Holding lock 1 & 2...");
					}
				}
			}
		});
		// Create a second thread
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				synchronized (lock2) {
					System.out.println("Thread 2: Holding lock 2...");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Thread 2: Waiting for lock 1...");
					synchronized (lock1) {
						System.out.println("Thread 2: Holding lock 1 & 2...");
					}
				}
			}
		});
		// Start both threads
		t1.start();
		t2.start();
	}
}

//2 Acquire locks in a consistent order:
class DeadlockAvoidanceExample {
	static Object lock1 = new Object(); // Create the first lock
	static Object lock2 = new Object(); // Create the second lock

	public static void main(String[] args) {
// Create the first thread  
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				synchronized (lock1) { // Acquire lock1
					System.out.println("Thread 1: Holding lock 1...");
					try {
						Thread.sleep(10); // Pause for 10 milliseconds
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Thread 1: Waiting for lock 2...");
					synchronized (lock2) { // Acquire lock2
						System.out.println("Thread 1: Holding lock 1 & 2...");
					}
				}
			}
		});
		// Create the second thread
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				synchronized (lock1) { // Acquire lock1
					System.out.println("Thread 2: Holding lock 1...");
					try {
						Thread.sleep(10); // Pause for 10 milliseconds
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Thread 2: Waiting for lock 2...");
					synchronized (lock2) { // Acquire lock2
						System.out.println("Thread 2: Holding lock 1 & 2...");
					}
				}
			}
		});

		t1.start(); // Start the first thread
		t2.start(); // Start the second thread
	}
}

//3. Avoid waiting for a lock indefinitely:
/*
 * A thread shouldn't keep waiting indefinitely for a lock. Instead, set a
 * timeout for the lock so the thread can move on if the lock is not acquired
 * promptly.
 * 
 * To avoid waiting for a lock indefinitely, you can use the tryLock() method of
 * the Java.util.concurrent.locks.ReentrantLock class. The tryLock() method
 * tries to acquire the lock but returns immediately if it cannot be acquired.
 * It allows you to prevent your thread from waiting indefinitely for a lock.
 */
class DeadlockAvoidanceExample3 {
	static ReentrantLock lock1 = new ReentrantLock();
	static ReentrantLock lock2 = new ReentrantLock();

	public static void main(String[] args) {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				boolean lock1Acquired = lock1.tryLock(); // Attempt to acquire lock1
				if (!lock1Acquired) { // If unable to acquire lock1, return
					System.out.println("Thread 1: Unable to acquire lock 1");
					return;
				}
				System.out.println("Thread 1: Holding lock 1...");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread 1: Waiting for lock 2...");
				boolean lock2Acquired = lock2.tryLock(); // Attempt to acquire lock2
				if (!lock2Acquired) { // If unable to acquire lock2, release lock1 and return
					System.out.println("Thread 1: Unable to acquire lock 2");
					lock1.unlock();
					return;
				}
				System.out.println("Thread 1: Holding lock 1 & 2...");
				lock2.unlock(); // Release lock2
				lock1.unlock(); // Release lock1
			}
		});
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				boolean lock2Acquired = lock2.tryLock(); // Attempt to acquire lock2
				if (!lock2Acquired) { // If unable to acquire lock2, return
					System.out.println("Thread 2: Unable to acquire lock 2");
					return;
				}
				System.out.println("Thread 2: Holding lock 2...");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread 2: Waiting for lock 1...");
				boolean lock1Acquired = lock1.tryLock(); // Attempt to acquire lock1
				if (!lock1Acquired) { // If unable to acquire lock1, release lock2 and return
					System.out.println("Thread 2: Unable to acquire lock 1");
					lock2.unlock();
					return;
				}
				System.out.println("Thread 2: Holding lock 1 & 2...");
				lock1.unlock(); // Release lock1
				lock2.unlock(); // Release lock2
			}
		});
		t1.start();
		t2.start();
	}
}

/*
 * 4. Use Reentrant Locks: Reentrant locks can control access to resources in a
 * deadlock-free manner. They allow a single thread to lock the resource
 * multiple times, which can help avoid deadlocks.
 * 
 * Reentrant locks are a particular kind of lock that permit several locks to be
 * made with the same thread. It means that a thread can acquire the lock,
 * release it, and then acquire it again without causing a deadlock. It can be
 * useful in avoiding deadlocks in complex, multi-threaded applications.
 */

class DeadlockAvoidanceExample4 {
	// Creating two ReentrantLock objects lock1 and lock2
	static ReentrantLock lock1 = new ReentrantLock();
	static ReentrantLock lock2 = new ReentrantLock();

	public static void main(String[] args) {
		// Creating and starting Thread t1
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				lock1.lock();
				System.out.println("Thread 1: Holding lock 1...");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread 1: Waiting for lock 2...");
				lock2.lock();
				System.out.println("Thread 1: Holding lock 1 & 2...");
				lock2.unlock();
				lock1.unlock();
			}
		});
		// Creating and starting Thread t2
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				lock2.lock();
				System.out.println("Thread 2: Holding lock 2...");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread 2: Waiting for lock 1...");
				lock1.lock();
				System.out.println("Thread 2: Holding lock 1 & 2...");
				lock1.unlock();
				lock2.unlock();
			}
		});

		// Starting both threads
		t1.start();
		t2.start();
	}
}

//5. Use Executor Services
/*
 * Java provides a built-in executor service that can be used to manage thread
 * execution. You can use Its service to manage the execution of tasks and
 * ensure they are executed deadlock-free.
 * 
 * Executor Services are a way to manage multiple threads in a Java program. It
 * allows you to execute a task in the background by submitting it to an
 * executor. The executor then decides which thread should be used to execute
 * the task. It helps to simplify the management of multiple threads, as well as
 * to improve performance by reusing existing threads.
 */

class DeadlockAvoidanceExample5 {
	public static void main(String[] args) {
// create a thread pool with two threads  
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		// define tasks to be executed by threads
		Runnable task1 = new Runnable() {
			public void run() {
				System.out.println("Thread 1: Running task 1...");
			}
		};
		Runnable task2 = new Runnable() {
			public void run() {
				System.out.println("Thread 2: Running task 2...");
			}
		};
		// submit tasks to the executor service for execution
		executorService.submit(task1);
		executorService.submit(task2);
		// shutdown the executor service when all tasks are completed
		executorService.shutdown();
	}
}
/*
 * 6. Monitor your code: Regularly monitor your code for deadlocks. If a
 * deadlock occurs, you should quickly identify and resolve it to avoid
 * performance issues and delays in your application.
 * 
 * Monitoring your code is the process of monitoring the behaviour of your
 * program and identifying potential deadlocks. It can be done by logging the
 * state of your threads and using tools such as profilers to monitor the
 * behaviour of your program.
 * 
 * In the example, we LOG the state of each thread to help identify any
 * potential deadlocks. It can help you understand your program's behaviour and
 * identify potential issues before they become critical. Additionally, you can
 * use profilers to monitor the behaviour of your program and identify any
 * performance issues that may be contributing to a deadlock. By monitoring your
 * code and using tools to help identify potential deadlocks, you can ensure
 * that your Java programs run smoothly and avoid deadlocks.
 */

class DeadlockAvoidanceExample6 {
	public static void main(String[] args) {
// Create two locks as objects  
		Object lock1 = new Object();
		Object lock2 = new Object();
		// Create thread t1
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				// Acquire lock1
				synchronized (lock1) {
					System.out.println("Thread 1: Holding lock 1...");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Thread 1: Waiting for lock 2...");
					// Acquire lock2
					synchronized (lock2) {
						System.out.println("Thread 1: Holding lock 1 & 2...");
					}
				}
			}
		});
		// Create thread t2
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				// Acquire lock2
				synchronized (lock2) {
					System.out.println("Thread 2: Holding lock 2...");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Thread 2: Waiting for lock 1...");
					// Acquire lock1
					synchronized (lock1) {
						System.out.println("Thread 2: Holding lock 1 & 2...");
					}
				}
			}
		});
		// Start both threads
		t1.start();
		t2.start();
	}
}

/*
 * 7. Avoid circular dependencies:
 * 
 * If multiple threads depend on each other, they can get stuck in a circular
 * deadlock. To avoid It, you should avoid circular dependencies and make sure
 * that the dependencies between threads are well-defined.
 * 
 * Circular dependencies can occur when two or more objects depend on each other
 * in a way that creates a cycle. It can cause deadlocks as each object waits
 * for the other to release a required resource. To avoid circular dependencies,
 * it is important to design your program to prevent these dependencies from
 * forming.
 */
//It program demonstrates a simple example of avoiding deadlock in Java using synchronized blocks and locking objects  
//Two threads are created that synchronize on two different objects in the opposite order, thereby avoiding any potential deadlock  
class DeadlockAvoidanceExample7 {
	/**
	 * In The example, we avoid circular dependencies by ensuring that each thread
	 * acquires locks in a different order. It prevents a cycle from forming, as
	 * each thread can obtain the required locks without waiting for the other to
	 * release them. By avoiding circular dependencies, you can ensure your Java
	 * programs run smoothly and avoid deadlocks.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//Create two objects to be used as locks  
		final Object lock1 = new Object();
		final Object lock2 = new Object();
// Create the first thread that synchronizes on lock1, then lock2  
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				synchronized (lock1) {
					System.out.println("Thread 1: Holding lock 1...");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Thread 1: Waiting for lock 2...");
					synchronized (lock2) {
						System.out.println("Thread 1: Holding lock 1 & 2...");
					}
				}
			}
		});
// Create a second thread that synchronizes on lock2, then lock1  
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				synchronized (lock2) {
					System.out.println("Thread 2: Holding lock 2...");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Thread 2: Waiting for lock 1...");
					synchronized (lock1) {
						System.out.println("Thread 2: Holding lock 1 & 2...");
					}
				}
			}
		});
// Start both threads  
		t1.start();
		t2.start();
	}
}