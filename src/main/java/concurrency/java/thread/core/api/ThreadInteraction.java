package concurrency.java.thread.core.api;

/**
 * Desc:
 * 
 * Thread Interaction (Objective 4.4)
 * 
 * <pre>
 * 
 * 4.4 Given a scenario, write code that makes appropriate use of wait, notify.
 * or notifyAll. The last thing we need to look at is how threads can interact
 * with one another to communicate about—among other things—their locking
 * status.
 * 
 * The Object class has three methods, wait(), notify(), and notifyAll() that
 * help threads communicate about the status of an event that the threads care
 * about. For example, if one thread is a mail-delivery thread and one thread is
 * a mail-processor thread, the mail-processor thread has to keep checking to
 * see if there's any mail to process.
 * 
 * 
 * Using the wait and notify mechanism, the mail-processor thread could check
 * for mail, and if it doesn't find any it can say, "Hey, I'm not going to waste
 * my time checking for mail every two seconds. I'm going to go hang out, and
 * when the mail deliverer puts something in the mailbox, have him notify me so
 * I can go back to runnable and do some work." In other words, using wait() and
 * notify() lets one
 * 
 * thread put itself into a "waiting room" until some other thread notifies it that there's 
 * a reason to come back out.
 * 
 * One key point to remember (and keep in mind for the exam) about wait/notify 
 * is this:
 * 
 *   wait(), notify(),  and notifyAll()  must be called from within a synchronized 
 * context! 
 * 
 * A thread can't invoke a wait or notify method on an object unless it owns 
 * that object's lock.
 * 
 * 
 * </pre>
 * 
 */
public class ThreadInteraction {

	/*
	 * Think of a computer-controlled machine that cuts pieces of fabric into
	 * different shapes and an application that allows users to specify the
	 * shape to cut. The current version of the application has one thread,
	 * which loops, first asking the user for instructions, and then directs the
	 * hardware to cut the requested shape:
	 */
	public void run() {
		while (true) {
			// Get shape from user
			// Calculate machine steps from shape
			// Send steps to hardware
		}
	}

	/*
	 * Above  design is not optimal because the user can't do anything while
	 * the machine is busy and while there are other shapes to define.
	 * 
	 * We need to improve the situation. A simple solution is to separate the
	 * processes into two different threads, one of them interacting with the
	 * user and another managing the hardware. The user thread sends the
	 * instructions to the hardware thread and then goes back to interacting
	 * with the user immediately. The hardware thread receives the instructions
	 * from the user thread and starts directing the machine immediately.
	 * 
	 * 
	 * Both threads use a common object to communicate, which holds the current
	 * design being processed. The following pseudocode shows this design:
	 */
	public void userLoop() {
		while (true) {
			// Get shape from user
			// Calculate machine steps from shape
			// Modify common object with new machine steps
		}
	}

	public void hardwareLoop() {
		while (true) {
			// Get steps from common object
			// Send steps to hardware
		}
	}

	/**
	 * The problem now is to get the hardware thread to process the machine
	 * steps as soon as they are available.
	 * 
	 * Also, the user thread should not modify them until they have all been
	 * sent to the hardware. The solution is to use wait() and notify(), and
	 * also to synchronize some of the code.
	 */

}

/**
 * The methods wait() and notify(), remember, are instance methods of Object. In
 * the same way that every object has a lock, every object can have a list of
 * threads that are waiting for a signal (a notification) from the object.
 * 
 * A thread gets on this waiting list by executing the wait() method of the
 * target object. From that moment, it doesn't execute any further instructions
 * until the notify() method of the target object is called.
 * 
 * If many threads are waiting on the same object, only one will be chosen (in
 * no guaranteed order) to proceed with its execution.
 * 
 * If there are no threads waiting, then no particular action is taken. Let's
 * take a look at some real code that shows one object waiting for another
 * object to notify it (take note, it is somewhat complex):
 * 
 */
class ThreadA {
	public static void main(String[] args) {
		ThreadB b = new ThreadB();
		b.setName("B-thread");
		b.start();
		/*
		 * the code synchronizes itself with the object b�this is because in
		 * order to call wait() on the object, ThreadA must own a lock on b. For
		 * a thread to call wait() or notify(), the thread has to be the owner
		 * of the lock for that object.
		 */
		synchronized (b) {
			try {
				System.out.println(" Waiting for " + b.getName()
						+ " calculation...");
				System.out.println("  when wait(), thread  releases its lock");
				// main thread (A) waits thread B until it finishes and notifies
				// him(A)
				/**
				 * When the wait() method is invoked on an object, the thread
				 * executing that code gives up its lock on the object
				 * immediately.
				 * 
				 * However, when notify() is called, that doesn’t mean the
				 * thread gives up its lock at that moment.
				 * 
				 * If the thread is still completing synchronized code, the lock
				 * is not released until the thread moves out of synchronized
				 * code. So just because notify() is called doesn’t mean the
				 * lock becomes available at that moment.
				 */
				// ThreadA is WAITING for Thread ThreadB to be completed
				b.wait();
			} catch (InterruptedException e) {
			}
			System.out.println("Total is: " + b.total);
		}
	}
}

class ThreadB extends Thread {
	int total;

	public void run() {
		synchronized (this) {
			System.out.println("-- thread B starts ---");
			for (int i = 0; i < 100; i++) {
				total += i;
			}
			System.out.println("-- thread B completes, you can notify now ---");
			/**
			 * When the wait() method is invoked on an object, the thread
			 * executing that code gives up its lock on the object immediately.
			 * 
			 * However, when notify() is called, that doesn’t mean the thread
			 * gives up its lock at that moment.
			 * 
			 * If the thread is still completing synchronized code, the lock is
			 * not released until the thread moves out of synchronized code. So
			 * just because notify() is called doesn’t mean the lock becomes
			 * available at that moment.
			 */
			System.out.println("  when notify(), thread  KEEPs its lock");
			notify();
		}
	}
}
