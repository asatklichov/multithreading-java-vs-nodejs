package concurrency.java.concurrent.api;

import java.util.concurrent.atomic.AtomicInteger;

class Counter {
	private int c = 0;

	public void increment() {
		c++;
	}

	public void decrement() {
		c--;
	}

	public int value() {
		return c;
	}

}

/**
 * One way to make Counter safe from thread interference is to make its methods
 * synchronized, as in SynchronizedCounter:
 * 
 * @return
 */
class SynchronizedCounter {
	private int c = 0;

	public synchronized void increment() {
		c++;
	}

	public synchronized void decrement() {
		c--;
	}

	public synchronized int value() {
		return c;
	}

}

/**
 * For this simple class, synchronization is an acceptable solution. But for a
 * more complicated class, we might want to avoid the liveness impact of
 * unnecessary synchronization. Replacing the int field with an AtomicInteger
 * allows us to prevent thread interference without resorting to
 * synchronization, as in AtomicCounter:
 */

class AtomicCounter {
	private AtomicInteger c = new AtomicInteger(0);

	public void increment() {
		c.incrementAndGet();
	}

	public void decrement() {
		c.decrementAndGet();
	}

	public int value() {
		return c.get();
	}

}