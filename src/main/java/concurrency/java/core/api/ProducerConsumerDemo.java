package concurrency.java.core.api;

public class ProducerConsumerDemo {

	/**
	 * Producer can not publish if buffer is full and consumer can not consume if it is empty. 
	 * So, always keep buffer under control to prevent race condition.
	 */
	private static int[] buffer;
	private static int count;

	private static Object monitor = new Object();

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

		consumerWorker.join();
		producerWorker.join();

		System.out.println("Remained data in buffer: " + count);
	}

	static class Producer {

		public void produce() {
			synchronized (monitor) {
				if (isFull(buffer)) {
					try {
						monitor.wait(); //release KEY, will be available for consumer
						Thread currentThread = Thread.currentThread();
						System.out.println(currentThread.getName() + " state: " + currentThread.getState());
					} catch (InterruptedException e) {
						System.err.println(e.getMessage());
					}
				}
				buffer[count++] = 1; //without synchronization causes race condition
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
				buffer[--count] = 0; //without synchronization causes race condition
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
}