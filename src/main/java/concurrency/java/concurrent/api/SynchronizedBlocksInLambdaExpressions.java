package concurrency.java.concurrent.api;

import java.util.function.Consumer;

public class SynchronizedBlocksInLambdaExpressions {
	/**
	 * Synchronized Blocks in Lambda Expressions It is even possible to use
	 * synchronized blocks inside a Java Lambda Expression as well as inside
	 * anonymous classes.
	 * 
	 * Here is an example of a Java lambda expression with a synchronized block
	 * inside. Notice that the synchronized block is synchronized on the class
	 * object of the class containing the lambda expression. It could have been
	 * synchronized on another object too, if that would have made more sense (given
	 * a specific use case), but using the class object is fine for this example.
	 */
	public static void main(String[] args) {

		Consumer<String> func = (String param) -> {

			synchronized (SynchronizedBlocksInLambdaExpressions.class) {

				System.out.println(Thread.currentThread().getName() + " step 1: " + param);

				try {
					Thread.sleep((long) (Math.random() * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println(Thread.currentThread().getName() + " step 2: " + param);
			}

		};

		Thread thread1 = new Thread(() -> {
			func.accept("Parameter");
		}, "Thread 1");

		Thread thread2 = new Thread(() -> {
			func.accept("Parameter");
		}, "Thread 2");

		thread1.start();
		thread2.start();
	}
}