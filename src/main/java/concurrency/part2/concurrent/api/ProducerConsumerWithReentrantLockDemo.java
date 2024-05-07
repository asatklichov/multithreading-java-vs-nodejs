package concurrency.part2.concurrent.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerConsumerWithReentrantLockDemo {
}

/**
 * See {@link ProducerConsumerWithBlockingQueue } {@link ProducerConsumerWithReentrantLockAndConditionDemo}
 *
 */
class ProducerConsumerWithReentrantLockAndConditionDemo {

	public static void main(String[] args) throws InterruptedException {

		List<Integer> buffer = new ArrayList<>();

		Lock lock = new ReentrantLock();
		Condition isEmpty = lock.newCondition();
		Condition isFull = lock.newCondition();

		class Consumer implements Callable<String> {

			public String call() throws InterruptedException, TimeoutException {
				int count = 0;
				while (count++ < 50) {
					try {
						lock.lock();
						while (isEmpty(buffer)) {
							// wait isEmpty.await() - make blocked
							if (!isEmpty.await(10, TimeUnit.MILLISECONDS)) {
								throw new TimeoutException("Consumer time out");
							}
						}
						buffer.remove(buffer.size() - 1);
						// signal
						isFull.signalAll();
					} finally {
						// guarantee in case something happens it releases lock
						lock.unlock();
					}
				}
				return "Consumed " + (count - 1);
			}
		}

		class Producer implements Callable<String> {

			public String call() throws InterruptedException {
				int count = 0;
				while (count++ < 50) {
					try {
						lock.lock();
						int i = 10 / 0;
						while (isFull(buffer)) {
							// wait
							isFull.await();
						}
						buffer.add(1);
						// signal
						isEmpty.signalAll();
					} finally {
						// guarantee in case something happens it releases lock
						lock.unlock();
					}
				}
				return "Produced " + (count - 1);
			}
		}

		List<Producer> producers = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			producers.add(new Producer());
		}

		List<Consumer> consumers = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			consumers.add(new Consumer());
		}

		System.out.println("Producers and Consumers launched");

		List<Callable<String>> producersAndConsumers = new ArrayList<>();
		producersAndConsumers.addAll(producers);
		producersAndConsumers.addAll(consumers);

		/**
		 * Here we use Threads from ExecutorService
		 */
		ExecutorService executorService = Executors.newFixedThreadPool(8);
		// try with 4, total number threads available be CARE,
		// ExecutorService executorService = Executors.newFixedThreadPool(4);
		try {
			List<Future<String>> futures = executorService.invokeAll(producersAndConsumers);

			futures.forEach(future -> {
				try {
					System.out.println(future.get());
				} catch (InterruptedException | ExecutionException e) {
					System.out.println("Exception: " + e.getMessage());
				}
			});

		} finally {
			executorService.shutdown();
			System.out.println("Executor service shut down");
		}

	}

	public static boolean isEmpty(List<Integer> buffer) {
		return buffer.size() == 0;
	}

	public static boolean isFull(List<Integer> buffer) {
		return buffer.size() == 10;
	}
}

//// other way 

class ProducerConsumerWithLocksDemo {

	public static void main(String[] args) {

		Queue<Integer> queue = new LinkedList<Integer>();
		ReentrantLock lock = new ReentrantLock();
		Condition con = lock.newCondition();
		final int size = 5;

		new Produceri(lock, con, queue, size).start();
		new Consumeri(lock, con, queue).start();

	}

}

class Produceri extends Thread {

	ReentrantLock lock;
	Condition con;
	Queue<Integer> queue;
	int size;

	public Produceri(ReentrantLock lock, Condition con, Queue<Integer> queue, int size) {
		this.lock = lock;
		this.con = con;
		this.queue = queue;
		this.size = size;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			lock.lock();
			while (queue.size() == size) {
				try {
					con.await();
				} catch (InterruptedException ex) {
					Logger.getLogger(Produceri.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			queue.add(i);
			System.out.println("Produced : " + i);
			con.signal();
			lock.unlock();
		}
	}

}

class Consumeri extends Thread {

	ReentrantLock lock;
	Condition con;
	Queue<Integer> queue;

	public Consumeri(ReentrantLock lock, Condition con, Queue<Integer> queue) {
		this.lock = lock;
		this.con = con;
		this.queue = queue;
	}

	public void run() {
		for (int i = 0; i < 10; i++) {
			lock.lock();
			while (queue.size() < 1) {
				try {
					con.await();
				} catch (InterruptedException ex) {
					Logger.getLogger(Consumeri.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			System.out.println("Consumed : " + queue.remove());
			con.signal();
			lock.unlock();
		}
	}
}