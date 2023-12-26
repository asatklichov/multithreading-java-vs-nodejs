package concurrency.java.core.api;

public class LongWrapperFixedRaceCondition {

	private Object lock = new Object();
	private long l;

	public LongWrapperFixedRaceCondition(long l) {
		this.l = l;
	}

	/*
	 * Here still we have visibility issue (It does not guarantee it will return LAST value from WRITE operation)
	 */
	public long getValue() {
		return l;
	}

	public void incrementValue() {
		synchronized (lock) {
			l = l + 1;
		}
	}
}
