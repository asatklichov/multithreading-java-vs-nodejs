package concurrency.java.core.api;

/**
 * Thread safe Singleton
 * 
 * JVM creates the unique instance of this class when the class is loaded
 * 
 * <pre>
* Pros(+) and Cons(-)
 * 
 * + Thread safe on multi-threaded app.
 * 
 * + Performance improved via lazy load
 * 
 * + Lazy initialized
 * 
 * + You can not create multiple instance via reflection
 * 
 * - volatile usage causes broken synchronization with less version than JDK 1.5
 * 
 * +- [J. Bloch] To make a singleton class that is implemented using either of the previous
		approaches serializable (Chapter 11), it is not sufficient merely to add implements
		Serializable to its declaration. To maintain the singleton guarantee, you
		have to declare all instance fields transient and provide a readResolve method
		(Item 77). Otherwise, each time a serialized instance is deserialized, a new
		instance will be created, leading, in the case of our example, to spurious Elvis
		sightings. To prevent this, add this readResolve method
 * 
 * <code>
 *  readResolve method to preserve singleton property
	private Object readResolve() {
	// Return the one true ThreadSafeSingleton and let the garbage collector
	// take care of the ThreadSafeSingleton impersonator.
	return INSTANCE;
	}
 * </code>
 * </pre>
 */
public class ThreadSafeSingleton {

	/**
	 * volatile guarantees visibility between threads
	 */
	private static volatile ThreadSafeSingleton instance;

	/**
	 * You can not create multiple instance via reflection and private constructor
	 * is not visible besides this class.
	 */
	private ThreadSafeSingleton() {
		if (instance != null) {
			throw new RuntimeException(
					"This class is designed to provide single instance, so use getInstance() method to get that single instance.");
		}
	}

	/**
	 * 
	 * Thread safe - Double check mechanism guarantees only one instance is used
	 * between threads
	 * 
	 * @return
	 */
	public static ThreadSafeSingleton getInstance() {

		if (instance == null) {
			synchronized (ThreadSafeSingleton.class) {
				if (instance == null) {
					instance = new ThreadSafeSingleton();
				}
			}
		}
		return instance;
	}

	@Override
	public String toString() {
		return "Hello from " + this.getClass().getSimpleName() + "#" + hashCode();
	}
}
