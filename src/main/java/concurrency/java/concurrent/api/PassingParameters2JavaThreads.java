package concurrency.java.concurrent.api;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

/**
 * Sending Parameters in the Constructor The first way we can send a parameter
 * to a thread is simply providing it to our Runnable or Callable in their
 * constructor.
 * 
 * 
 * https://www.baeldung.com/java-thread-parameters
 *
 */
public class PassingParameters2JavaThreads implements Callable<Double> {

	int[] numbers;

	public PassingParameters2JavaThreads(int... numbers) {
		this.numbers = numbers == null ? new int[0] : numbers;
	}

	@Override
	public Double call() throws Exception {
		return IntStream.of(numbers).average().orElse(0d);
	}

	public void whenSendingParameterToCallable_thenSuccessful() throws Exception {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<Double> result = executorService.submit(new PassingParameters2JavaThreads(1, 2, 3));
		try {
			// assertEquals(2.0, result.get().doubleValue());
		} finally {
			executorService.shutdown();
		}
	}

	public void whenSendingParameterToCallable_thenSuccessful2() throws Exception {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<Double> result = executorService.submit(new PassingParameters2JavaThreads(1, 2, 3));
		try {
			// assertEquals(2.0, result.get().doubleValue());
		} finally {
			executorService.shutdown();
		}
	}

	public void whenParametersToThreadWithLamda_thenParametersPassedCorrectly() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		int[] numbers = new int[] { 4, 5, 6 };

		try {
			Future<Integer> sumResult = executorService.submit(() -> IntStream.of(numbers).sum());
			Future<Double> averageResult = executorService.submit(() -> IntStream.of(numbers).average().orElse(0d));
			// assertEquals(Integer.valueOf(15), sumResult.get());
			// assertEquals(Double.valueOf(5.0), averageResult.get());
		} finally {
			executorService.shutdown();
		}
	}

	public void whenThreadPoolExecutor_thenCorrect() throws Exception {

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
		executor.submit(() -> {
			Thread.sleep(1000);
			return null;
		});
		executor.submit(() -> {
			Thread.sleep(1000);
			return null;
		});
		executor.submit(() -> {
			Thread.sleep(1000);
			return null;
		});

		// assertEquals(2, executor.getPoolSize());
		// assertEquals(1, executor.getQueue().size());
	}

}