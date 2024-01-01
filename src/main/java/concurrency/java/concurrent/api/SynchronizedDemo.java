package concurrency.java.core.api;

public class SynchronizedDemo {

	// give example with this monitor object
	// give for .class level

	// give example with different monitor object
	// like: multiple monitor objects: private Object monitor1; monitor2,... then
	// FOLLOW RIGHT ORDER to solve DEADLOCK issue, ... ...
	// shared monitor object ...

	// shared monitor object demo
	public static void main(String[] args) {
		Object monitor1 = new Object();

		SharedMonitor obj1 = new SharedMonitor(monitor1);
		SharedMonitor obj2 = new SharedMonitor(monitor1);
		// synchronized on same monitor obj. once used on multiple threads, block each
		// other
		obj1.increment();
		obj2.increment();

		Object monitor2 = new Object();
		SharedMonitor obj3 = new SharedMonitor(monitor2);
		// not blocks others threads
		obj3.increment();
	}

}

class SharedMonitor {

	private Object monitor;
	private int count;

	public SharedMonitor(Object monitor) {
		if (monitor == null) {
			throw new IllegalArgumentException("Monitor object can not be null");
		}
		this.monitor = monitor;
	}

	public void increment() {
		synchronized (this.monitor) {
			this.count++;
			System.out.println(count);
		}
	}
}

/**
 * Reentrance  Java synchronized blocks are reentrant A thread already holds,
 * is allowed to enter synchronized block which is also synchronized
 *
 */
class Reentrant {
	private int count;

	public synchronized void incrment() {
		this.count++;
	}

	public synchronized int incrmentAndGet() {
		// so here no blocked - reentrant
		incrment();
		return this.count;
	}

}

class HappensBeforeGuarantee {
	private long count = 0;
	private Object obj = null;

	public void set1(Object o) {
		//both read/write from main memory here, it is guaranteed 
		synchronized (this) {
			this.count++;
			this.obj = o;
		}
	}

	/**
	 * JVM decides to re-order, what happens
	 */
	public void set2(Object o) {
		//so here no longer guarantee, to read/write from main-memory this count
		this.count++;
		synchronized (this) {
			this.obj = o;
		}
		//but here it guarantees, however, but still not 100%
	}

	public void set3(Object o) {
		synchronized (this) {
			this.obj = o;
		}
		
		//so here no longer guarantee, to read/write from main-memory this count
		//problem is incremented count may not be written to count 
		this.count++;
	}

	public Object get() {
		synchronized (this) {
			return this.obj;
		}
	}
}