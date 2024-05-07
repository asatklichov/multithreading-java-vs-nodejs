package concurrency.part2.concurrent.api;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * BlockingDeque
 * 
 * <pre>
 *Usage A BlockingDeque could be used if threads are both
 * producing and consuming elements of the same queue. It could also just be
 * used if the producting thread needs to insert at both ends of the queue, and
 * the consuming thread needs to remove from both ends of the queue. Here is an
 * illustration of that: 
 * 
 *  LinkedBlockingDeque is a Deque which will block if a thread attempts to 
 *  take elements out of it while it is empty, regardless of what end the thread 
 *  is attempting to take elements from.
 * 
 * </pre>
 */

public class BlockingDequeExample {

	// https://jenkov.com/tutorials/java-util-concurrent/blockingdeque.html
	public static void main(String[] args) throws InterruptedException {
		BlockingDeque<String> deque = new LinkedBlockingDeque<String>();

		deque.addFirst("1");
		deque.addLast("2");
		deque.addLast("3");

		String two = deque.takeLast();
		String one = deque.takeFirst();

		System.out.println(two + "; " + one);
	}
}

class PriorityBlockingDequeExample {

	// https://jenkov.com/tutorials/java-util-concurrent/linkedblockingdeque.html
	public static void main(String[] args) throws Exception {

		BlockingDeque<String> deque = new LinkedBlockingDeque<String>();

		deque.addFirst("1");
		deque.addLast("2");

		String two = deque.takeLast();
		System.out.println(two);
		String one = deque.takeFirst();
		System.out.println(one);

		System.out.println("PriorityBlockingQueueExample done");

	}
}