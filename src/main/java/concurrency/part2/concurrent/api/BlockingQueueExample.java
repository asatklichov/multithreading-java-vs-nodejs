package concurrency.part2.concurrent.api;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

class ProducesConsumerPattern {
	/**
	 * <pre>
	 * Use Cases
	There are several different kinds of use cases for the producer consumer design pattern. Some of the most common are:
	
	
	 * https://jenkov.com/tutorials/java-concurrency/producer-consumer.html
	
	Reduce foreground thread latency.
	Load balance work between different threads.
	Backpressure management.
	 * </pre>
	 */
	public static void main(String[] args) {

	}
}

/**
 * 
 * https://jenkov.com/tutorials/java-concurrency/blocking-queues.html
 * https://jenkov.com/tutorials/java-util-concurrent/blockingqueue.html
 * 
 * 
 * ArrayBlockingQueue is a bounded, blocking queue that stores the elements
 * internally in an array. That it is bounded means that it cannot store
 * unlimited amounts of elements. There is an upper bound on the number of
 * elements it can store at the same time. You set the upper bound at
 * instantiation time, and after that it cannot be changed.
 * 
 * The ArrayBlockingQueue stores the elements internally in FIFO (First In,
 * First Out) order. The head of the queue is the element which has been in
 * queue the longest time, and the tail of the queue is the element which has
 * been in the queue the shortest time.
 * 
 * <pre>
 * BlockingQueue Implementations
Since BlockingQueue is an interface, you need to use one of its implementations to use it. 
The java.util.concurrent package has the following implementations of the BlockingQueue interface:

ArrayBlockingQueue
DelayQueue
LinkedBlockingQueue
LinkedBlockingDeque
LinkedTransferQueue
PriorityBlockingQueue
SynchronousQueue
 * </pre>
 *
 */
class ArrayBlockingQueueExample {

	public static void main(String[] args) throws Exception {

		/**
		 * There is an upper bound on the number of elements it can store at the same
		 * time.
		 * 
		 * Here upper boud 1024 elements
		 */
		BlockingQueue queue = new ArrayBlockingQueue(1024);

		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);

		new Thread(producer).start();
		new Thread(consumer).start();

		Thread.sleep(4000);
		System.out.println("ArrayBlockingQueue done");

		queue = new ArrayBlockingQueue(1024);
		queue.put("1");

		Object object = queue.take();
	}
}



/**
 * The LinkedBlockingQueue keeps the elements internally in a linked structure
 * (linked nodes). This linked structure can optionally have an upper bound if
 * desired. If no upper bound is specified, Integer.MAX_VALUE is used as the
 * upper bound.
 * 
 * The LinkedBlockingQueue stores the elements internally in FIFO (First In,
 * First Out) order. The head of the queue is the element which has been in
 * queue the longest time, and the tail of the queue is the element which has
 * been in the queue the shortest time.
 *
 */
class LinkedBlockingQueueExample {

	// https://jenkov.com/tutorials/java-util-concurrent/linkedblockingqueue.html
	public static void main(String[] args) throws Exception {

		BlockingQueue queue = new LinkedBlockingQueue<>(); // no need to define UPPER BOUND, or still OK to defne 1024

		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);

		new Thread(producer).start();
		new Thread(consumer).start();

		Thread.sleep(4000);

		System.out.println("LinkedBlockingQueue done");

		BlockingQueue<String> unbounded = new LinkedBlockingQueue<String>();
		BlockingQueue<String> bounded = new LinkedBlockingQueue<String>(1024);

		bounded.put("Value");

		String value = bounded.take();

	}
}

/**
 * The PriorityBlockingQueue is an unbounded concurrent queue. It uses the same
 * ordering rules as the java.util.PriorityQueue class. You cannot insert null
 * into this queue.
 * 
 * All elements inserted into the PriorityBlockingQueue must implement the
 * java.lang.Comparable interface. The elements thus order themselves according
 * to whatever priority you decide in your Comparable implementation.
 * 
 * Notice that the PriorityBlockingQueue does not enforce any specific behaviour
 * for elements that have equal priority (compare() == 0).
 * 
 * Also notice, that in case you obtain an Iterator from a
 * PriorityBlockingQueue, the Iterator does not guarantee to iterate the
 * elements in priority order.
 * 
 */
class PriorityBlockingQueueExample {

	// https://jenkov.com/tutorials/java-util-concurrent/priorityblockingqueue.html
	public static void main(String[] args) throws Exception {

		BlockingQueue queue = new PriorityBlockingQueue();

		// String implements java.lang.Comparable
		queue.put("Value");

		String value = (String) queue.take();
		System.out.println(value);

		System.out.println("PriorityBlockingQueueExample done");

	}
}

/**
 * The SynchronousQueue is a queue that can only contain a single element
 * internally. A thread inserting an element into the queue is blocked until
 * another thread takes that element from the queue. Likewise, if a thread tries
 * to take an element and no element is currently present, that thread is
 * blocked until a thread insert an element into the queue.
 *
 */
class SynchronousQueueExample {

	public static void main(String[] args) throws Exception {

		// https://jenkov.com/tutorials/java-util-concurrent/synchronousqueue.html
		/**
		 * The SynchronousQueue is a queue that can be used to exchange a single element
		 * with another thread.
		 */
		System.out.println("SynchronousQueueExample done");

	}
}

/**
 * The DelayQueue blocks the elements internally until a certain delay has
 * expired. The elements must implement the interface
 * java.util.concurrent.Delayed. Here is how the interface looks:
 * 
 * public interface Delayed extends Comparable<Delayed< {
 * 
 * public long getDelay(TimeUnit timeUnit);
 * 
 * }
 *
 */
class DelayQueue_Example {

	public static void main(String[] args) throws Exception {
		DelayQueue queue = new DelayQueue();


		//https://jenkov.com/tutorials/java-util-concurrent/delayqueue.html
		// The DelayQueue blocks the elements internally until a certain delay has
				// expired.
		Delayed element1 = new DelayObject("Kuzey Yildiz", (new Date()).getTime());

		queue.put(element1);

		Delayed element2 = queue.take();
		System.out.println(element2);

		System.out.println("DelayQueueExample done");

	}
}

 

class Producer implements Runnable {

	protected BlockingQueue queue = null;

	public Producer(BlockingQueue queue) {
		this.queue = queue;
	}

	public void run() {
		try {
			queue.put("1");
			Thread.sleep(1000);
			queue.put("2");
			Thread.sleep(3000);
			queue.put("3");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Consumer implements Runnable {

	protected BlockingQueue queue = null;

	public Consumer(BlockingQueue queue) {
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
