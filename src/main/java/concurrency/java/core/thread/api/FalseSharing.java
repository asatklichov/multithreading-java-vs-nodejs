package concurrency.java.core.thread.api;

public class FalseSharing {

	public static int NUM_THREADS_MAX = 4;
	public final static long ITERATIONS = 50_000_000L;

	private static VolatileLongPadded[] paddedLongs;
	private static VolatileLongUnPadded[] unPaddedLongs;

	/**
	 * When we create several instance of this class, most probably they will be
	 * recorded on same cache line on the CPU.
	 * 
	 * And this causes the FALSE SHARING problem
	 * 
	 */
	public final static class VolatileLongUnPadded {
		public volatile long value = 0L;
	}

	/**
	 * To fix FALSE sharing
	 * 
	 * As same as above, BUT to be sure, not to come across FALSE SHARING, we just
	 * use some PADDINGs (before and after).
	 * 
	 * So 13 long variables. This help, it will not SHARE same line of CACHE in CPU. 
	 * 
	 * This idea is similar to HASH algorithm.
	 * Try to make different hash KEYs, so buckets will be containing less values.
	 */
	public final static class VolatileLongPadded {
		public long q1, q2, q3, q4, q5, q6;
		public volatile long value = 0L;
		public long q11, q12, q13, q14, q15, q16;

	}

	static {
		paddedLongs = new VolatileLongPadded[NUM_THREADS_MAX];
		for (int i = 0; i < paddedLongs.length; i++) {
			paddedLongs[i] = new VolatileLongPadded();
		}
		unPaddedLongs = new VolatileLongUnPadded[NUM_THREADS_MAX];
		for (int i = 0; i < unPaddedLongs.length; i++) {
			unPaddedLongs[i] = new VolatileLongUnPadded();
		}
	}

	public static void main(final String[] args) throws Exception {
		System.out.println("See the PERFORMANCE once # of threads increases");
		runBenchmark();
	}

	private static void runBenchmark() throws InterruptedException {

		long begin, end;

		for (int n = 1; n <= NUM_THREADS_MAX; n++) {

			Thread[] threads = new Thread[n];

			for (int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(createPaddedRunnable(j));
			}

			begin = System.currentTimeMillis();
			for (Thread t : threads) {
				t.start();
			}
			// main thread wait until all threads finish their work
			for (Thread t : threads) {
				t.join();
			}
			end = System.currentTimeMillis();
			System.out.printf("   Padded # threads %d - T = %dms\n", n, end - begin);

			for (int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(createUnpaddedRunnable(j));
			}

			begin = System.currentTimeMillis();
			for (Thread t : threads) {
				t.start();
			}
			for (Thread t : threads) {
				t.join();
			}
			end = System.currentTimeMillis();
			System.out.printf(" UnPadded # threads %d - T = %dms\n\n", n, end - begin);
		}
	}

	private static Runnable createUnpaddedRunnable(final int k) {
		return () -> {
			long i = ITERATIONS + 1;
			while (0 != --i) {
				//possibly shares same cache line 
				//modified value
				unPaddedLongs[k].value = i;
			}
		};
	}

	private static Runnable createPaddedRunnable(final int k) {
		Runnable paddedTouch = () -> {
			long i = ITERATIONS + 1;
			while (0 != --i) {
				// protected against FALSE sharing
				paddedLongs[k].value = i;
			}
		};
		return paddedTouch;
	}
}

class FalseSharing2 {
	public static void main(String[] args) {
		// https://jenkov.com/tutorials/java-concurrency/false-sharing.html
		System.out.println("\n Preventing False Sharing With the @Contented Annotation");
		/**
		 * From Java 8 and 9, Java has the @Contended annotation which can pad fields
		 * inside classes with empty bytes (after the field - when stored in RAM), so
		 * that the fields inside an object of that class are not stored within the same
		 * CPU cache line. Below is the Counter class from the earlier example with
		 * the @Contended annotation added to one of the fields. Adding this annotation
		 * made the execution time drop down to around the same time as when the two
		 * thread used two different Counter instances. Here is the modified Counter
		 * class:
		 * 
		 * <pre>
		 * public class Counter1 {
		 * 
		 * 	@jdk.internal.vm.annotation.Contended
		 * 	public volatile long count1 = 0;
		 * 	public volatile long count2 = 0;
		 * }
		 * </pre>
		 */

	}

}