package concurrency.java.thread.core.api;


/**
 * Race condition and Visibility problems
 * 
 * What Does Synchronization Do?
 * 
 * - Protects a block of code
 * 
 * - Guarantees this code is executed by one thread at a time
 * 
 * - Prevents race conditions
 * 
 * 
 * What Does Visibility Do?
 * 
 * - CPU does not read a variable from the main memory, but from a cache
 * 
 * - Guarantees the consistency of the variables (read always correct value)
 * 
 * - Visibility: Read operation should return always the latest value set by
 * 
 * 
 * All shared variables should be accessed in a synchronized or a volatile way,
 * otherwise your will end up with race condition.
 * 
 */
public class VisibilityImprovesRaceConditionFix {

}


/**
 * In {@link RaceConditionFixedDemo} we only fixed on WRITE operation. 
 * 
 * BUT in Reality,  regarding Visibility issues, we need to FIX the READ operation as well 
 *
 */
class RaceConditionFixedDemoImprovedToFixVisibilityIssue {

	public static void main(String[] args) throws InterruptedException {

		LongWrapperFixedRaceConditionAndVisbility longWrapper = new LongWrapperFixedRaceConditionAndVisbility(0L);

		Runnable r = () -> {

			for (int i = 0; i < 1_000; i++) {
				longWrapper.incrementValue();
			}
		};

		Thread[] threads = new Thread[1_000];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(r);
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}

		System.out.println("Value = " + longWrapper.getValue());

	}
}
