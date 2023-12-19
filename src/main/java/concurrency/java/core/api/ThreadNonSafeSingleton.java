package concurrency.java.core.api;

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
 * - Difficult to unit test
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
public class ThreadNonSafeSingleton {

	// makes eager initialization
	// private static final ThreadNonSafeSingleton instance = new
	// ThreadNonSafeSingleton();

	// for lazy init
	private static ThreadNonSafeSingleton instance;

	/**
	 * private constructor is not visible besides this class
	 */
	private ThreadNonSafeSingleton() {

	}

	// for eagerly usage 
//	public static ThreadNonSafeSingleton getInstance() {
//		return instance;
//	}

	public static ThreadNonSafeSingleton getInstance() {
		if (instance == null) {
			instance = new ThreadNonSafeSingleton();
		}
		return instance;
	}

	@Override
	public String toString() {
		return "Hello from " + this.getClass().getSimpleName() + "#" + hashCode();
	}
}
