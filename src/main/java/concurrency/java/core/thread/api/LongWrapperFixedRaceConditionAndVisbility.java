package concurrency.java.core.thread.api;

/**
 * Same with {@link LongWrapperForHappensBeforeGuarantee}
 * 
 * to using different terminology to explain
 *
 */
public class LongWrapperFixedRaceConditionAndVisbility {

	private Object lock = new Object();
	private long l;

	//// private Object lock = new Object();
	// private volatile long l; //can't help alone, see:
	//// incrementValueNotOnlyVolatileHere

	public LongWrapperFixedRaceConditionAndVisbility(long l) {
		this.l = l;
	}

	/**
	 * We improve the solution, so No Visibility issue will happen
	 */
	public long getValue() {
		synchronized (lock) {
			return l;
		}
	}

	public void incrementValue() {
		// correctly increment
		synchronized (lock) {
			l = l + 1;
		}
	}

	public void incrementValueNotOnlyVolatileHelpsHere() {
		// synchronized (lock) {
		l = l + 1; // Here we have READ+WRITE again
		// this operation should be ATOMIC
		// only synchronization can help not to interrupt between READ-and-WRITE
		// }
	}
}
