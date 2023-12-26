package concurrency.java.core.api;

class ThreadSignalingDemo {
	public static void main(String[] args) {
		System.out.println("wait(), notify() and notifyAll()");
		/**
		 * Java has a built-in wait mechanism that enable threads to become inactive
		 * while waiting for signals from other threads. The class java.lang.Object
		 * defines three methods, wait(), notify(), and notifyAll(), to facilitate this
		 */

		// see MyWaitNotify
		System.out.println("\nMissed Signals ");
		System.out.println(
				"The methods notify() and notifyAll() do not save the method\r\n calls to them in case no threads are waiting when they are called.");
		System.out.println(
				"This may or may not be a problem, but in some cases this may result in the waiting thread waiting forever, never waking up, because the signal to wake up was missed.");
		/**
		 * The methods notify() and notifyAll() do not save the method calls to them in
		 * case no threads are waiting when they are called. The notify signal is then
		 * just lost. Therefore, if a thread calls notify() before the thread to signal
		 * has called wait(), the signal will be missed by the waiting thread. This may
		 * or may not be a problem, but in some cases this may result in the waiting
		 * thread waiting forever, never waking up, because the signal to wake up was
		 * missed.
		 */
		System.out.println("To avoid losing signals they should be stored inside the signal class.");
		// see MyWaitNotify2

		System.out.println("\nSpurious Wakeups");
		/**
		 * For inexplicable reasons it is possible for threads to wake up even if
		 * notify() and notifyAll() has not been called. This is known as spurious
		 * wakeups. Wakeups without any reason.
		 */
		// see MyNotify3

	}
}

class MonitorObject {
}

class MyWaitNotify {

	/**
	 * A thread that calls wait() on any object becomes inactive until another
	 * thread calls notify() on that object.
	 * 
	 * In order to call either wait() or notify the calling thread must first obtain
	 * the lock on that object.
	 * 
	 * In other words, the calling thread must call wait() or notify() from inside a
	 * synchronized block. Here is a modified version of MySignal called
	 * MyWaitNotify that uses wait() and notify().
	 */
	MonitorObject myMonitorObject = new MonitorObject();

	public void doWait() {
		synchronized (myMonitorObject) {
			try {
				myMonitorObject.wait();
			} catch (InterruptedException e) {
				// ...
			}
		}
	}

	public void doNotify() {
		synchronized (myMonitorObject) {
			myMonitorObject.notify();
		}
	}
}

class MyWaitNotify2 {

	MonitorObject myMonitorObject = new MonitorObject();
	boolean wasSignalled = false;

	public void doWait() {
		synchronized (myMonitorObject) {
			if (!wasSignalled) {
				try {
					myMonitorObject.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// clear signal and continue running.
			wasSignalled = false;
		}
	}

	public void doNotify() {
		synchronized (myMonitorObject) {
			wasSignalled = true;
			myMonitorObject.notify();
		}
	}
}

class MyWaitNotify3 {

	MonitorObject myMonitorObject = new MonitorObject();
	boolean wasSignalled = false;

	public void doWait() {
		synchronized (myMonitorObject) {
			while (!wasSignalled) {
				try {
					/**
					 * Notice how the wait() call is now nested inside a while loop instead of an
					 * if-statement. If the waiting thread wakes up without having received a
					 * signal, the wasSignalled member will still be false, and the while loop will
					 * execute once more, causing the awakened thread to go back to waiting.
					 */
					myMonitorObject.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// clear signal and continue running.
			wasSignalled = false;
		}
	}

	public void doNotify() {
		synchronized (myMonitorObject) {
			wasSignalled = true;
			myMonitorObject.notify();
		}
	}
}

/**
 * http://tutorials.jenkov.com/java-concurrency/thread-signaling.html
 * 
 * A thread that calls wait() on any object becomes inactive until another
 * thread calls notify() on that object. In order to call either wait() or
 * notify the calling thread must first obtain the lock on that object. In other
 * words, the calling thread must call wait() or notify() from inside a
 * synchronized block.
 *
 */
class MySignal {

	protected boolean hasDataToProcess = false;

	public synchronized boolean hasDataToProcess() {
		return this.hasDataToProcess;
	}

	public synchronized void setHasDataToProcess(boolean hasData) {
		this.hasDataToProcess = hasData;
	}

}