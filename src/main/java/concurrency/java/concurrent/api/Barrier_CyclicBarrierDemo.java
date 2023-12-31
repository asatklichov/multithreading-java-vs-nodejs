package concurrency.java.concurrent.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * https://www.baeldung.com/java-cyclicbarrier-countdownlatch
 * 
 * The java.util.concurrent.CyclicBarrier class is a synchronization mechanism
 * that can synchronize threads progressing through some algorithm. In other
 * words, it is a barrier that all threads must wait at, until all threads reach
 * it, before any of the threads can continue. Here is a diagram illustrating
 * that:
 *
 */
public class Barrier_CyclicBarrierDemo {

	// https://jenkov.com/tutorials/java-util-concurrent/cyclicbarrier.html
	public static void main(String[] args) {
		/**
		 * 
		 * The java.util.concurrent.CyclicBarrier class is a synchronization mechanism
		 * that can synchronize threads progressing through some algorithm. In other
		 * words, it is a barrier that all threads must wait at, until all threads reach
		 * it, before any of the threads can continue. Here is a diagram illustrating
		 * that:
		 * 
		 * 
		 * When you create a CyclicBarrier you specify how many threads are to wait at
		 * it, before releasing them. Here is how you create a CyclicBarrier:
		 */
		//CyclicBarrier barrier = new CyclicBarrier(2);

		Runnable barrier1Action = new Runnable() {
			public void run() {
				System.out.println("BarrierAction 1 executed ");
			}
		};
		Runnable barrier2Action = new Runnable() {
			public void run() {
				System.out.println("BarrierAction 2 executed ");
			}
		};

		CyclicBarrier barrier1 = new CyclicBarrier(2, barrier1Action);
		CyclicBarrier barrier2 = new CyclicBarrier(2, barrier2Action);

		CyclicBarrierRunnable barrierRunnable1 = new CyclicBarrierRunnable(barrier1, barrier2);

		CyclicBarrierRunnable barrierRunnable2 = new CyclicBarrierRunnable(barrier1, barrier2);

		new Thread(barrierRunnable1).start();
		new Thread(barrierRunnable2).start();
	}

}

class CyclicBarrierRunnable implements Runnable {

	CyclicBarrier barrier1 = null;
	CyclicBarrier barrier2 = null;

	public CyclicBarrierRunnable(CyclicBarrier barrier1, CyclicBarrier barrier2) {

		this.barrier1 = barrier1;
		this.barrier2 = barrier2;
	}

	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + " waiting at barrier 1");
			this.barrier1.await();

			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName() + " waiting at barrier 2");
			this.barrier2.await();

			System.out.println(Thread.currentThread().getName() + " done!");

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}

/**
 * Group of people want to go to cinema and gathered in one place
 *
 */
class BarrierInAction {

	public static void main(String[] args) {

		class Friend implements Callable<String> {

			private CyclicBarrier barrier;

			public Friend(CyclicBarrier barrier) {
				this.barrier = barrier;
			}

			public String call() throws Exception {

				try {
					Random random = new Random();
					Thread.sleep((random.nextInt(20) * 100 + 100));
					System.out.println("I just arrived, waiting for the others...");

					barrier.await();

					System.out.println("Let's go to the cinema!");
					return "ok";
				} catch (InterruptedException e) {
					System.out.println("Interrupted");
				}
				return "nok";
			}
		}

		ExecutorService executorService = Executors.newFixedThreadPool(2);

		CyclicBarrier barrier = new CyclicBarrier(4, () -> System.out.println("Barrier openning"));
		List<Future<String>> futures = new ArrayList<>();

		try {
			for (int i = 0; i < 4; i++) {
				Friend friend = new Friend(barrier);
				futures.add(executorService.submit(friend));
			}

			futures.forEach(future -> {
				try {
					future.get(200, TimeUnit.MILLISECONDS);
				} catch (InterruptedException | ExecutionException e) {
					System.out.println(e.getMessage());
				} catch (TimeoutException e) {
					System.out.println("Timed out");
					// interrup running tasks and the waiting ones
					future.cancel(true);
				}
			});

		} finally {
			executorService.shutdown();
		}
	}
}

class Worker implements Callable<List<Integer>> {
	private CyclicBarrier barrier;
	private List<Integer> inputList;

	public Worker(CyclicBarrier barrier, List<Integer> inputList) {
		this.barrier = barrier;
		this.inputList = inputList;
	}

	public List<Integer> call() {
		List<Integer> result = findPrimes(inputList);
		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// Error handling
		}
		return result;
	}

	/**
	 * 
	 * @param inputList2
	 * @return
	 */
	private List<Integer> findPrimes(List<Integer> inputList) {
		List<Integer> primeNumbersList = new ArrayList<>();
		for (Integer integer : inputList) {
			List<Integer> primeNumbersBruteForce = primeNumbersBruteForce(integer);			
			Collections.copy(primeNumbersList, primeNumbersBruteForce);
		}
		return primeNumbersList;
	}
	
	public static List<Integer> primeNumbersBruteForce(int n) {
	    List<Integer> primeNumbers = new ArrayList<>();
	    for (int i = 2; i <= n; i++) {
	        if (isPrimeBruteForce(i)) {
	            primeNumbers.add(i);
	        }
	    }
	    return primeNumbers;
	}
	public static boolean isPrimeBruteForce(int number) {
	    for (int i = 2; i < number; i++) {
	        if (number % i == 0) {
	            return false;
	        }
	    }
	    return true;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CyclicBarrier barrier = new CyclicBarrier(4);
		ExecutorService service = Executors.newFixedThreadPool(4);
		List<Integer> inputList = List.of(3, 4, 53, 4);
		Worker worker1 = new Worker(barrier, inputList);
		// More workers
		Future<List<Integer>> future1 = service.submit(worker1);
		// More submissions
		List<Integer> finalResult = new ArrayList<>(future1.get());
		finalResult.addAll(future1.get());
		// More results
	}
}