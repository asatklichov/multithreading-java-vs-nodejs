package concurrency.java.concurrent.api;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

/**
 * 
 * See {@link Queue_DequeDemo} in ilki project 
 * 
 * This is also referred to as the "Last In First Out (LIFO)" principle. In
 * contrast, a Java Queue uses a "First In First Out (FIFO)" principle, where
 * elements are added to the end of the queue, and removed from the beginning of
 * the queue.
 *
 */
public class StackDemo {
	public static void main(String[] args) {
		Stack stack = new Stack();

		stack.push("1");
		stack.push("3");
		stack.push("2");
		System.out.println(stack);

		int index = stack.search("3"); // index = 1

		stack = new Stack();

		stack.push("123");
		stack.push("456");
		stack.push("789");

		Iterator iterator = stack.iterator();
		while (iterator.hasNext()) {
			Object value = iterator.next();
			System.out.println(value);
		}

		Stream stream = stack.stream();

		stream.forEach((element) -> {
			System.out.println(element); // print element
		});

		List list = new ArrayList();
		list.add("A");
		list.add("B");
		list.add("C");
		System.out.println(list);

		stack = new Stack();
		while (list.size() > 0) {
			stack.push(list.remove(0));
		}

		while (stack.size() > 0) {
			list.add(stack.pop());
		}

		System.out.println(list);

		System.out.println("\n Use a Java Deque as a Stack - see DequeDemo for more Deque examples example");
		Deque<String> dequeAsStack = new ArrayDeque<String>();

		dequeAsStack.push("one");
		dequeAsStack.push("two");
		dequeAsStack.push("three");
		System.out.println(dequeAsStack);

		String one = dequeAsStack.pop();
		System.out.println("As you see last in first out");
		System.out.println(one);
		String two = dequeAsStack.pop();
		System.out.println(two);
		String three = dequeAsStack.pop();
		System.out.println(three);
		System.out.println(dequeAsStack);

		/**
		 * Stack Use Cases A Stack is really handy for some types of data processing,
		 * for instance if you are parsing an XML file using either SAX or StAX. For an
		 * example, see my Java SAX Example in my Java XML tutorial.
		 * 
		 * 2. For Math arithmetic operation (2-8*(7-3)) etc. 
		 */
	}

}