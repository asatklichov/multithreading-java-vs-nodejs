package concurrency.java.concurrent.api;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerConsumerWithLocksDemo {

	public static void main(String[] args) {

		Queue<Integer> queue = new LinkedList<Integer>();
		ReentrantLock lock = new ReentrantLock();
		Condition con = lock.newCondition();
		final int size = 5;

		new Producer(lock, con, queue, size).start();
		new Consumer(lock, con, queue).start();

	}

}

class Producer extends Thread {

	ReentrantLock lock;
	Condition con;
	Queue<Integer> queue;
	int size;

	public Producer(ReentrantLock lock, Condition con, Queue<Integer> queue, int size) {
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
					Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			queue.add(i);
			System.out.println("Produced : " + i);
			con.signal();
			lock.unlock();
		}
	}

}

class Consumer extends Thread {

	ReentrantLock lock;
	Condition con;
	Queue<Integer> queue;

	public Consumer(ReentrantLock lock, Condition con, Queue<Integer> queue) {
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
					Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			System.out.println("Consumed : " + queue.remove());
			con.signal();
			lock.unlock();
		}
	}
}

class ProducerConsumerWithLocksDemo2 {
	List<Integer> buffer = new ArrayList<Integer>();
	Lock lock = new ReentrantLock();
	Condition isEmpty = lock.newCondition();

	public static void main2(String[] args) throws InterruptedException {

		List<Callable<String>> producerAndConsumers = new ArrayList<Callable<String>>();

		List<Producer2> producers = new ArrayList<>();
		ProducerConsumerWithLocksDemo2.Producer2 producer = (new ProducerConsumerWithLocksDemo2()).new Producer2();
		producer.call();
		producers.add(producer);
		producerAndConsumers.addAll(producers);
//
//		Callable<String> consumer = () -> {
//			// Perform some computation
//			Thread.sleep(2000);
//			return "Return some result";
//		}; 

		ProducerConsumerWithLocksDemo2.Consumer2 consumer = (new ProducerConsumerWithLocksDemo2()).new Consumer2();
		producer.call();

		List<Callable<String>> consumers = new ArrayList<>();
		consumers.add(consumer);
		producerAndConsumers.addAll(consumers);

		ExecutorService executorService = Executors.newFixedThreadPool(8);
		try {
			List<Future<String>> futures = executorService.invokeAll(producerAndConsumers);
			futures.forEach(future -> {
				try {
					System.out.println(future.get());
				} catch (InterruptedException | ExecutionException e) {
					System.out.println("Exception: " + e.getMessage());
				}
			});
		} finally {
			executorService.shutdown();
		}
	}

	class Consumer2 implements Callable<String> {

		@Override
		public String call() throws InterruptedException {
			int count = 0;
			while (count++ < 50) {
				try {
					lock.lock();
					while (isEmpty(buffer)) {
						// wait
						isEmpty.await();
					}
					buffer.remove(buffer.size() - 1);
					// signal
					isEmpty.signalAll();
				} finally {
					lock.unlock();
				}
			}
			return "Consumed:  " + (count - 1);
		}

	}

	class Producer2 implements Callable<String> {

		@Override
		public String call() throws InterruptedException {
			int count = 0;
			while (count++ < 50) {
				try {
					lock.lock();
					while (isFull(buffer)) {
						// wait
						isEmpty.await();
					}
					buffer.add(1);
					// signal
					isEmpty.signalAll();
				} finally {
					lock.unlock();
				}
			}
			return "Produced:  " + (count - 1);
		}

	}

	private static boolean isEmpty(List<Integer> buffer) {
		return buffer.isEmpty();
	}

	private static boolean isFull(List<Integer> buffer) {
		return buffer.get(buffer.size() - 1) == 1;
	}
}
