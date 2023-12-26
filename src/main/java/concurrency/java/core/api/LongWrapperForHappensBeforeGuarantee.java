package concurrency.java.core.api;

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
