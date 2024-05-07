package concurrency.java.core.thread.api;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Thread-Safe Classes
 * 
 * <pre>
 * 
 * When a class has been carefully synchronized to protect its data (using the rules 
 * just given, or using more complicated alternatives), we say the class is "thread-safe."  
 * Many classes in the Java APIs already use synchronization internally in order to 
 * make the class "thread-safe."  
 * 
 * For example, StringBuffer and StringBuilder are nearly 
 * identical classes, except that all the methods in StringBuffer are synchronized 
 * when necessary, while those in StringBuilder are not.  Generally, this makes 
 * StringBuffer safe to use in a multithreaded environment, while StringBuilder is not.  
 * (In return, StringBuilder is a little bit faster because it doesn't bother synchronizing.) 
 * 
 *  
 * However, even when a class is "thread-safe," it is often dangerous to rely on these 
 * classes to  provide the thread protection you need.  (C'mon, the repeated quotes 
 * used around "thread-safe" had to be a clue, right?)  You still need to think carefully 
 * about how you use these classes, As an example, consider the following class.
 * </pre>
 * 
 */
public class ThreadSafeClasses {

}

class NameList {
	/**
	 * * The thing to realize here is that in a "thread-safe" class like the one
	 * returned by synchronizedList(), each individual method is synchronized.
	 * So names.size() is synchronized, and names.remove(0) is synchronized.
	 * 
	 * But nothing prevents another thread from doing something else to the list
	 * in between those two calls. And that's where problems can happen. </pre>
	 */
	private List names = Collections.synchronizedList(new LinkedList());

	public void add(String name) {
		names.add(name);
	}

	public String removeFirst() {
		if (names.size() > 0)
			return (String) names.remove(0);
		else
			return null;
	}

	/**
	 * The method Collections.synchronizedList() returns a List whose methods
	 * are all
	 * 
	 * synchronized and "thread-safe" according to the documentation (like a
	 * Vector—but since this is the 21st century, we're not going to use a
	 * Vector here).
	 * 
	 * The question is, can the NameList class be used safely from multiple
	 * threads?
	 * 
	 * It's tempting to think that yes, since the data in names is in a
	 * synchronized collection, the NameList class is "safe" too. However that's
	 * not the case—the
	 * 
	 * removeFirst() may sometimes throw a NoSuchElementException. What's the
	 * problem?
	 * 
	 * Doesn't it correctly check the size() of names before removing anything,
	 * to make sure there's something there? How could this code fail? Let's try
	 * to use NameList like this:
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final NameList nl = new NameList();
		nl.add("Ozymandias");
		class NameDropper extends Thread {
			public void run() {
				String name = nl.removeFirst();
				System.out.println(name);
			}
		}
		Thread t1 = new NameDropper();
		Thread t2 = new NameDropper();
		t1.start();
		t2.start();
		System.out.println("done");
		/**
		 * <pre>
		 * What might happen here is that one of the threads will remove the one name 
		 * and print it, then the other will try to remove a name and get null.  
		 * If we think just 
		 * about the calls to names.size() and names.get(0), they occur in this order:
		 * 
		 * 
		 * Thread t1 executes names.size(), which returns 1.
		 * Thread t1 executes names.remove(0), which returns Ozymandias.
		 * Thread t2 executes names.size(), which returns 0.
		 * Thread t2 does not call remove(0).
		 * 
		 * The output here is
		 * Ozymandias
		 * null
		 * 
		 * 
		 * However, if we run the program again something different might happen:
		 * 
		 * Thread t1 executes names.size(), which returns 1.
		 * Thread t2 executes names.size(), which returns 1.
		 * Thread t1 executes names.remove(0), which returns Ozymandias.
		 * Thread t2 executes names.remove(0), which throws an exception because the 
		 *     list is now empty.
		 *     
		 *     The thing to realize here is that in a "thread-safe" class like the one returned by 
		 * synchronizedList(), each individual method is synchronized.  So names.size() 
		 * is synchronized, and names.remove(0) is synchronized.  
		 * 
		 * But nothing prevents 
		 * another thread from doing something else to the list in between those two calls.  And 
		 * that's where problems can happen.
		 * </pre>
		 */
	}
}

/**
 * There's a solution here for above NameList: don't rely on
 * Collections.synchronizedList(). Instead, synchronize the code yourself:
 * 
 * 
 * Now the entire removeFirst() method is synchronized, and once one thread
 * starts it and calls names.size(), there's no way the other thread can cut in
 * and steal the last name. The other thread will just have to wait until the
 * first thread completes the removeFirst() method.
 * 
 * 
 * The moral here is that just because a class is described as "thread-safe"
 * doesn't mean it is always thread-safe.
 * 
 * If individual methods are synchronized, that may not be enough—you may be
 * better off putting in synchronization at a higher level (i.e., put it in the
 * block or method that calls the other methods).
 * 
 * Once you do that, the original synchronization (in this case, the
 * synchronization inside the object returned by Collections.synchronizedList())
 * may well become redundant.
 *  
 * 
 */
class NameList2 {
	private List names = new LinkedList();

	public synchronized void add(String name) {
		names.add(name);
	}

	public synchronized String removeFirst() {
		if (names.size() > 0)
			return (String) names.remove(0);
		else
			return null;
	}

	public static void main(String[] args) {
		final NameList2 nl = new NameList2();
		nl.add("Ozymandias");
		class NameDropper extends Thread {
			public void run() {
				String name = nl.removeFirst();
				System.out.println(name);
			}
		}
		Thread t1 = new NameDropper();
		Thread t2 = new NameDropper();
		t1.start();
		t2.start();
		System.out.println("done");
	}
}