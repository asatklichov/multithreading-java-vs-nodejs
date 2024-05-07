package concurrency.part1.thread.core.api;

public class LongWrapperFixedRaceCondition {

	private Object lock = new Object();
	private long l;

	public LongWrapperFixedRaceCondition(long l) {
		this.l = l;
	}

	/*
	 * Still BUGGY - for VISBILITY, READ must be synchronized to able to READ LAST value
	 * 
	 * BECAUSE I have NO "HAppens BEFORE LINK"
	 * between:  getValue()[READ] and incrementValue()[WRITE]
	 * 
	 * Here still we have visibility issue (It does not guarantee it will return LAST value from WRITE operation)
	 */
	public long getValue() {
		return l; //neither synchronized nor VOLATILE
	}

	public void incrementValue() {
		synchronized (lock) {
			l = l + 1;
		}
	}
}
