package concurrency.java.core.thread.api;

public class LongWrapperRaceCondition {

	private long l;

	public LongWrapperRaceCondition(long l) {
		this.l = l;
	}

	public long getValue() {
		return l;
	}

	/**
	 * Causes Race Condition.
	 * 
	 */
	public void incrementValue() {
		/**
		 * 1. Value l is read and copied locally 2. Then write operation followed
		 * 
		 * So, it is a Read/Write operation for different threads.
		 * 
		 * Value updated by other thread is not read by another thread, because other
		 * thread can not see it. Value is not updated yet in Main memory of the
		 * hardware
		 */
		l = l + 1;
	}
}
