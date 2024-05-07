package concurrency.java.core.thread.api;

/**
 * In Java, daemon threads are low-priority threads that run in the background
 * to perform tasks such as garbage collection or provide services to user
 * threads. The life of a daemon thread depends on the mercy of user threads,
 * meaning that when all user threads finish their execution, the Java Virtual
 * Machine (JVM) automatically terminates the daemon thread.
 * 
 * To put it simply, daemon threads serve user threads by handling background
 * tasks and have no role other than supporting the main execution.
 *
 *
 * Example of Daemon Thread in Java
 * 
 * Some examples of daemon threads in Java include garbage collection (gc) and
 * finalizer. These threads work silently in the background, performing tasks
 * that support the main execution without interfering with the user’s
 * operations.
 * 
 * 
 * Properties of Java Daemon Thread
 * 
 * No Preventing JVM Exit: Daemon threads cannot prevent the JVM from exiting
 * when all user threads finish their execution. If all user threads complete
 * their tasks, the JVM terminates itself, regardless of whether any daemon
 * threads are running. Automatic Termination: If the JVM detects a running
 * daemon thread, it terminates the thread and subsequently shuts it down. The
 * JVM does not check if the daemon thread is actively running; it terminates it
 * regardless. Low Priority: Daemon threads have the lowest priority among all
 * threads in Java.
 *
 * 
 * Default Nature of Daemon Thread By default, the main thread is always a
 * non-daemon thread. However, for all other threads, their daemon nature is
 * inherited from their parent thread. If the parent thread is a daemon, the
 * child thread is also a daemon, and if the parent thread is a non-daemon, the
 * child thread is also a non-daemon.
 * 
 * Note: Whenever the last non-daemon thread terminates, all the daemon threads
 * will be terminated automatically.
 * 
 * Methods of Daemon Thread 1. void setDaemon(boolean status):
 */
public class DaemonThreadDemo extends Thread {

	public DaemonThreadDemo(String name) {
		super(name);
	}

	public void run() {
		if (Thread.currentThread().isDaemon()) {
			System.out.println(getName() + " is Daemon thread");
		}

		else {
			System.out.println(getName() + " is User thread");
		}
	}

	public static void main(String[] args) {

		DaemonThreadDemo t1 = new DaemonThreadDemo("t1");
		DaemonThreadDemo t2 = new DaemonThreadDemo("t2");
		DaemonThreadDemo t3 = new DaemonThreadDemo("t3");

		t1.setDaemon(true);
		t1.start();
		t2.start();

		t3.setDaemon(true);
		t3.start();
	}
}