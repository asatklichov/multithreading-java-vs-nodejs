package concurrency.java.core.thread.api;

/**
 * Single-Threaded Singleton
 * 
 * JVM creates the unique instance of this class when the class is loaded
 * 
 * <pre>
* * Pros(+) and Cons(-)
 * 
 * + Works fine and provides single instance for Single-Threaded Model
 * 
 * - Difficult to unit test
 * 
 * - Non thread safe for multi-threaded app.
 * 
 * - Race condition happens 
 * 
 * - No Happens Before Link 
 * 
 * - Via reflection still you can create multiple instance of this class.
 * E.g. via invoking the private constructor reflectively  with help of 
 * AccessibleObject.setAccessible method
 * 
 * - Eagerly instantiated, slow performance
 * 
 * </code>
 * </pre>
 */
public class ThreadUnSafeSingleton {

	// static field loads only once

	// makes eager initialization
	// private static final ThreadNonSafeSingleton instance = new
	// ThreadNonSafeSingleton();

	// make lazy init
	private static ThreadUnSafeSingleton instance;

	/**
	 * private constructor is not visible besides this class
	 */
	private ThreadUnSafeSingleton() {

	}

	// for eagerly usage
//	public static ThreadNonSafeSingleton getInstance() {
//		return instance;
//	}

	public static ThreadUnSafeSingleton getInstance() {
		// Here No "Happens Before Link" (no synchronization, no volatile, ... )

		if (instance == null) { // causes race condition here ...
			instance = new ThreadUnSafeSingleton();
		}
		return instance;
	}

	

	@Override
	public String toString() {
		return "Hello from " + this.getClass().getSimpleName() + "#" + hashCode();
	}
}
