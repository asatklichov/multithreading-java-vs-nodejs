package concurrency.java.core.api;

public class LongWrapperFixedRaceConditionAndVisbility {

	private Object lock = new Object();
	private long l;

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
		//correctly increment
		synchronized (lock) {
			l = l + 1;
		}
	}
}
