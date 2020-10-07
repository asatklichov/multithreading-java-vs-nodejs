package concurrency.java.concurrent.api;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class ArrayBlockingQueueExample {

	public static void main(String[] args) throws Exception {

		BlockingQueue queue = new ArrayBlockingQueue(1024);

		MyProducer producer = new MyProducer(queue);
		MyConsumer consumer = new MyConsumer(queue);

		new Thread(producer).start();
		new Thread(consumer).start();

		Thread.sleep(2000);
		System.out.println("done");
	}
}

class MyProducer implements Runnable {

	protected BlockingQueue queue = null;

	public MyProducer(BlockingQueue queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			queue.put("1");
			Thread.sleep(1000);
			queue.put("2");
			Thread.sleep(1000);
			queue.put("3");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class MyConsumer implements Runnable {

	protected BlockingQueue queue = null;

	public MyConsumer(BlockingQueue queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			System.out.println(queue.take());
			System.out.println(queue.take());
			System.out.println(queue.take());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
