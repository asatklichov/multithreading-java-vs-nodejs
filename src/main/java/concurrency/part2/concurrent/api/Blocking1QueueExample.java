package concurrency.part2.concurrent.api;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

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
		queue.put("11");
		System.out.println(queue.take());
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
		System.out.println(bounded.take());
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
		queue.put("Malue");
		queue.put("Walue");

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
 *
 *
 * The SynchronousQueue only has two supported operations: take() and put(), and
 * both of them are blocking.
 * 
 * For example, when we want to add an element to the queue, we need to call the
 * put() method. That method will block until some other thread calls the take()
 * method, signaling that it is ready to take an element.
 * 
 * Although the SynchronousQueue has an interface of a queue, we should think
 * about it as an exchange point for a single element between two threads, in
 * which one thread is handing off an element, and another thread is taking that
 * element.
 * 
 * see  {@link ExchangerDemo}
 */
class SynchronousQueueExample {

	public static void main(String[] args) throws Exception {

		// https://jenkov.com/tutorials/java-util-concurrent/synchronousqueue.html
		// https://www.baeldung.com/java-synchronous-queue
		/**
		 * The SynchronousQueue is a queue that can be used to exchange a single element
		 * with another thread.
		 */

		ExecutorService executor = Executors.newFixedThreadPool(2);
		SynchronousQueue<Integer> queue = new SynchronousQueue<>();
		Runnable producer = () -> {
			Integer producedElement = ThreadLocalRandom.current().nextInt();
			try {
				queue.put(producedElement);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		};
		Runnable consumer = () -> {
			try {
				Integer consumedElement = queue.take();
				System.out.println(consumedElement);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		};

		executor.execute(producer);
		executor.execute(consumer);
		executor.awaitTermination(500, TimeUnit.MILLISECONDS);
		executor.shutdown();
		System.out.println(queue.size() == 0);
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

		// https://jenkov.com/tutorials/java-util-concurrent/delayqueue.html
		// https://www.baeldung.com/java-delay-queue
		/*
		 * The DelayQueue blocks the elements internally until a certain delay has
		 * expired
		 * 
		 * when the consumer wants to take an element from the queue, they can take it
		 * only when the delay for that particular element has expired.
		 * 
		 * https://www.geeksforgeeks.org/delayqueue-class-in-java-with-example/
		 */

		// create object of DelayQueue
		// using DelayQueue() constructor
		BlockingQueue<DelayObject2> DQ = new DelayQueue<DelayObject2>();

		// Add numbers to end of DelayQueue
		DQ.add(new DelayObject2("A", 1));
		DQ.add(new DelayObject2("B", 2));
		DQ.add(new DelayObject2("C", 3));
		DQ.add(new DelayObject2("D", 4));

		// print DelayQueue
		System.out.println("DelayQueue: " + DQ);

		// create object of DelayQueue
		// using DelayQueue(Collection c)
		// constructor
		BlockingQueue<DelayObject2> DQ2 = new DelayQueue<DelayObject2>(DQ);

		// print DelayQueue
		System.out.println("DelayQueue: " + DQ2);
		System.out.println("DelayQueueExample done");

	}
}

//The DelayObject for DelayQueue
//It must implement Delayed and
//its getDelay() and compareTo() method
class DelayObject2 implements Delayed {

	private String name;
	private long time;

	// Constructor of DelayObject
	public DelayObject2(String name, long delayTime) {
		this.name = name;
		this.time = System.currentTimeMillis() + delayTime;
	}

	// Implementing getDelay() method of Delayed
	@Override
	public long getDelay(TimeUnit unit) {
		long diff = time - System.currentTimeMillis();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}

	// Implementing compareTo() method of Delayed
	@Override
	public int compareTo(Delayed obj) {
		if (this.time < ((DelayObject2) obj).time) {
			return -1;
		}
		if (this.time > ((DelayObject2) obj).time) {
			return 1;
		}
		return 0;
	}

	// Implementing toString() method of Delayed
	@Override
	public String toString() {
		return "\n{" + "name=" + name + ", time=" + time + "}";
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
