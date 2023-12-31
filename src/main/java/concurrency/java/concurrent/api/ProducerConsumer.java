package org.paumard.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProducerConsumer {

	public static void main(String[] args) throws InterruptedException {

		BlockingQueue<String> queue = new ArrayBlockingQueue<>(50);

		class Consumer implements Callable<String> {

			public String call() throws InterruptedException {
				int count = 0;
				while (count++ < 50) {
					queue.take();
				}
				return "Consumed " + (count - 1);
			}
		}

		class Producer implements Callable<String> {

			public String call() throws InterruptedException {
				int count = 0;
				while (count++ < 50) {
					queue.put(Integer.toString(count));
				}
				return "Produced " + (count - 1);
			}
		}

		List<Callable<String>> producersAndConsumers = new ArrayList<>();
		
		for (int i = 0; i < 2; i++) {
			producersAndConsumers.add(new Producer());
		}

		for (int i = 0; i < 2; i++) {
			producersAndConsumers.add(new Consumer());
		}

		System.out.println("Producers and Consumers launched");

		ExecutorService executorService = Executors.newFixedThreadPool(4);
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
}
