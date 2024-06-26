package concurrency.part1.thread.core.api;

/**
 * Same with {@link LongWrapperFixedRaceConditionAndVisbility}
 * 
 * to using different terminology to explain
 *
 */
public class LongWrapperForHappensBeforeGuarantee {

	private Object lock = new Object();
	private long l;

	public LongWrapperForHappensBeforeGuarantee(long l) {
		this.l = l;
	}

	public long getValue() {
		synchronized (lock) {
			return l;
		}
	}

	public void incrementValue() {
		synchronized (lock) {
			l = l + 1;
		}
	}
}
