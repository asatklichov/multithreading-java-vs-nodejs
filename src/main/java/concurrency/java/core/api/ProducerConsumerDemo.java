package concurrency.java.core.api;

import concurrency.java.concurrent.api.ProducerConsumerWithReentrantLockDemo;

/**
 * See {@link ProducerConsumerWithReentrantLockDemo}
 *
 */
public class ProducerConsumerDemo {

}

/**
 * Buffer causes Race Condition if shared among the multiple threads. Because
 * many threads can do read/write at the same time.
 * 
 * Making method produce() and consume() synchronized can not help to much in
 * case we write it like e.g. buffer[count++] = 1;
 */
class UnsafeProducerConsumer {

	private static int[] buffer;
	private static int count;

	static class Producer {

		public void produce() {
			while (isFull(buffer)) { // tbd
			}

			buffer[count++] = 1;
		}
	}

	static class Consumer {

		public void consume() {
			while (isEmpty(buffer)) { // tbd
			}
			buffer[--count] = 0;
		}
	}

	private static boolean isEmpty(int[] buffer) {
		return count == 0;
	}

	private static boolean isFull(int[] buffer) {
		return buffer[buffer.length - 1] == 1;
	}

	public static void main(String[] args) throws InterruptedException {
		buffer = new int[10];
		count = 0;

		Producer producer = new Producer();
		Consumer consumer = new Consumer();

		Runnable producerTask = () -> {
			for (int i = 0; i < 15; i++) {
				producer.produce();
			}
			System.out.println("All produced");
		};

		Runnable consumerTask = () -> {
			for (int i = 0; i < 15; i++) {
				consumer.consume();

			}
			System.out.println("All consumed");
		};

		Thread producerWorker = new Thread(producerTask);
		Thread consumerWorker = new Thread(consumerTask);

		producerWorker.start();
		consumerWorker.start();

		/*
		 * This join makes sure that main-thread joins after application threads, and
		 * last line will be executed once threads finishes its execution
		 */
		consumerWorker.join();
		producerWorker.join();

		System.out.println("Remained data in buffer: " + count);

		/*
		 * All consumed
		 * 
		 * All produced
		 * 
		 * Remained data in buffer: 3
		 */
	}
}

//1-solution. What happens if we synchronize above produce() and consume() methods? 
/**
 * 
 * It will not fix the problem, because consumer() and produce() methods use
 * different LOCK object (each uses its instance as a monitor object). This code
 * change will work, if Consumer and Producer uses SAME lock object
 *
 * 
 * Produces LOCK issues
 * 
 */
class ProducerConsumerFirstTry {
	private static int[] buffer;
	private static int count;

	static class Producer {

		public synchronized void produce() {
			while (isFull(buffer)) { // tbd
			}

			buffer[count++] = 1;
		}
	}

	static class Consumer {

		public synchronized void consume() {
			while (isEmpty(buffer)) { // tbd
			}
			buffer[--count] = 0;
		}
	}

	private static boolean isEmpty(int[] buffer) {
		return count == 0;
	}

	private static boolean isFull(int[] buffer) {
		return buffer[buffer.length - 1] == 1;
	}

	public static void main(String[] args) throws InterruptedException {
		buffer = new int[10];
		count = 0;

		Producer producer = new Producer();
		Consumer consumer = new Consumer();

		Runnable producerTask = () -> {
			for (int i = 0; i < 15; i++) {
				producer.produce();
			}
			System.out.println("All produced");
		};

		Runnable consumerTask = () -> {
			for (int i = 0; i < 15; i++) {
				consumer.consume();

			}
			System.out.println("All consumed");
		};

		Thread producerWorker = new Thread(producerTask);
		Thread consumerWorker = new Thread(consumerTask);

		producerWorker.start();
		consumerWorker.start();

		/*
		 * This join makes sure that main-thread joins after application threads, and
		 * last line will be executed once threads finishes its execution
		 */
		consumerWorker.join();
		producerWorker.join();

		System.out.println("Remained data in buffer: " + count);
		/*
		 * All consumed
		 * 
		 * All produced
		 * 
		 * Remained data in buffer: 2
		 */
	}
}

/**
 * Using common lock object
 *
 */
class ProducerConsumerSecondTry {

	/**
	 * Producer can not publish if buffer is full and consumer can not consume if it
	 * is empty. So, always keep buffer under control to prevent race condition.
	 */
	private static int[] buffer;
	private static int count;

	/**
	 * This LOCK object will be common to all threads, and helps us to solve the
	 * RACE condition once used by multiple threads
	 */
	private static Object monitor = new Object();

	static class Producer {

		/**
		 * It fixes the Race condition problem unless buffer is FULL.
		 * 
		 * Problem happens when buffer becomes FULL. while (isFull(buffer)) {} loop runs
		 * infinitely, also THREAD (e.g. thread2) HOLDS the key, it does not release it,
		 * ...
		 * 
		 * And what happens in CONSUMER? Thread (e.g. thread1) running the consume()
		 * method will be WAITING the KEY (KEY is not available) which is hold by other
		 * Thread(thread2) which runs producer's produce() method.
		 * 
		 * So, thread1 can not run consume() method to make a empty space in BUFFER an
		 * object. So, buffer will be FULL forever. And thread2 will not release the KEY
		 * (holds for ever) because while (isFull(buffer)) {} // will run forever } runs
		 * infinitely
		 * 
		 * This Causes a DEADLOCK
		 * 
		 * 
		 * Solution, should be Thread (thread2) running PRODUCER;s produce() method
		 * should release a KEY, so Thread (thread1) which runs Consumer's consume()
		 * method may have a chance to make a space in Buffer
		 * 
		 * 
		 */
		public void produce() {
			synchronized (monitor) {
				while (isFull(buffer)) { // causes DEADLOCK
				}
				buffer[count++] = 1;
			}
		}
	}

	static class Consumer {

		/**
		 * It fixes the Race condition problem unless buffer is EMPTY.
		 * 
		 * Problem happens when buffer becomes empty. while (isEmpty(buffer)) {} loop
		 * runs infinitely, also THREAD (e.g. thread1) HOLDS the key, it does not
		 * release it, ...
		 * 
		 * And what happens in PRODUCER? Thread (e.g. thread2) running the produce()
		 * method will be WAITING the KEY (KEY is not available) which is hold by other
		 * Thread(thread1) which runs consumer's consume() method.
		 * 
		 * So, thread2 can not run producer() method to add an object. So, buffer will
		 * be empty forever. And thread1 will not release the KEY (holds for ever)
		 * because while (isEmpty(buffer)) { // will run forever } runs infinitely
		 * 
		 * This Causes a DEADLOCK
		 * 
		 * Solution, should be Thread (thread1) running CONSUMER's consume() method
		 * should release a KEY, so Thread (thread2) which runs Producer produce()
		 * method may have a chance to add elements
		 * 
		 * 
		 */
		public void consume() {
			synchronized (monitor) {
				while (isEmpty(buffer)) { // causes DEADLOCK
				}
				buffer[--count] = 0;
			}
		}
	}

	private static boolean isEmpty(int[] buffer) {
		return count == 0;
	}

	private static boolean isFull(int[] buffer) {
		return buffer[buffer.length - 1] == 1;
	}

	public static void main(String[] args) throws InterruptedException {
		buffer = new int[10];
		count = 0;

		Producer producer = new Producer();
		Consumer consumer = new Consumer();

		Runnable producerTask = () -> {
			for (int i = 0; i < 100; i++) {
				producer.produce();
			}
			System.out.println("All produced");
		};

		Runnable consumerTask = () -> {
			for (int i = 0; i < 97; i++) {
				consumer.consume();

			}
			System.out.println("All consumed");
		};

		Thread producerWorker = new Thread(producerTask);
		Thread consumerWorker = new Thread(consumerTask);

		producerWorker.start();
		consumerWorker.start();

		/*
		 * This join makes sure that main-thread joins after application threads, and
		 * last line will be executed once threads finishes its execution
		 */
		consumerWorker.join();
		producerWorker.join();

		System.out.println("Remained data in buffer: " + count);
	}
}

/**
 * 3-try solves all the issues (race condition and deadlock)
 * 
 * - use dedicated lock object - use wait/notify pattern
 *
 */
class ProducerConsumerThirdTryFixedAllIssues {

	/**
	 * Producer can not publish if buffer is full and consumer can not consume if it
	 * is empty. So, always keep buffer under control to prevent race condition.
	 */
	private static int[] buffer;
	private static int count;

	/**
	 * This LOCK object will be common to all threads, and helps us to solve the
	 * RACE condition once used by multiple threads
	 */
	private static Object monitor = new Object();

	static class Producer {

		public void produce() {
			synchronized (monitor) {
				if (isFull(buffer)) {
					try {
						monitor.wait(); // release the KEY, will be available for consumer
						Thread currentThread = Thread.currentThread();
						System.out.println(currentThread.getName() + " state: " + currentThread.getState());
					} catch (InterruptedException e) {
						System.err.println(e.getMessage());
					}
				}
				buffer[count++] = 1; // without synchronization causes race condition
				monitor.notifyAll();
			}
		}
	}

	static class Consumer {

		public void consume() {
			synchronized (monitor) {
				if (isEmpty(buffer)) {
					try {
						monitor.wait();
						Thread currentThread = Thread.currentThread();
						System.out.println(currentThread.getName() + " state: " + currentThread.getState());
					} catch (InterruptedException e) {
						System.err.println(e.getMessage());
					}
				}
				buffer[--count] = 0; // without synchronization causes race condition
				monitor.notifyAll();
			}
		}
	}

	private static boolean isEmpty(int[] buffer) {
		return count == 0;
	}

	private static boolean isFull(int[] buffer) {
		return buffer[buffer.length - 1] == 1;
	}

	public static void main(String[] args) throws InterruptedException {
		buffer = new int[10];
		count = 0;

		Producer producer = new Producer();
		Consumer consumer = new Consumer();

		Runnable producerTask = () -> {
			for (int i = 0; i < 100; i++) {
				producer.produce();
			}
			System.out.println("All produced");
		};

		Runnable consumerTask = () -> {
			for (int i = 0; i < 97; i++) {
				consumer.consume();

			}
			System.out.println("All consumed");
		};

		Thread producerWorker = new Thread(producerTask);
		Thread consumerWorker = new Thread(consumerTask);

		producerWorker.start();
		consumerWorker.start();

		/*
		 * This join makes sure that main-thread joins after application threads, and
		 * last line will be executed once threads finishes its execution
		 */
		consumerWorker.join();
		producerWorker.join();

		System.out.println("Remained data in buffer: " + count);
		/*
		 * All consumed All produced Remained data in buffer: 3
		 */
	}
}
