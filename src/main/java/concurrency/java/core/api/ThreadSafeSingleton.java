package concurrency.java.core.api;

class ThreadSafeSingletonFirstSolution {

	private static ThreadSafeSingletonFirstSolution instance;

	private ThreadSafeSingletonFirstSolution() {

	}

	/**
	 * Race condition is fixed
	 * 
	 * 1. On Single CPU T1, T2, ... can read correct value, no problem
	 * 
	 * 2. What happens on Two CORE? CPU tries to parallelize BUT can not do it
	 * because of synchronized method
	 * 
	 * 3. It would be good to do READ operation for parallelism, so multiple threads
	 * at least can READ it
	 * 
	 * 4. So, this solution is giving PERFORMANCE PENALTY
	 * 
	 */
	public static synchronized ThreadSafeSingletonFirstSolution getInstance() {
		// Yes, "Happens Before Link" by synchronized keyword now
		if (instance == null) {
			instance = new ThreadSafeSingletonFirstSolution();
		}
		return instance;
	}

	@Override
	public String toString() {
		return "Hello from " + this.getClass().getSimpleName() + "#" + hashCode();
	}
}

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

//Double Check is BUGGY PAttern,
class ThreadSafeSingletonWithDoubleCheckMechanism {

	/**
	 * volatile guarantees visibility between threads
	 */
	private static ThreadSafeSingletonWithDoubleCheckMechanism instance;

	/**
	 * You can not create multiple instance via reflection and private constructor
	 * is not visible besides this class.
	 */
	private ThreadSafeSingletonWithDoubleCheckMechanism() {
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
	 * 1. As you see we have no synchronized on method LEVEL 2. So, it allows on
	 * Multi CORE CPU parallelism on READ operation 3.
	 * 
	 * So, READING problem solved? Right?
	 * 
	 * 2. BUT, there is a BUG still?
	 * 
	 * Because READ operation is not a Synchronized READ or Volatile READ (cache
	 * problems? /// )
	 * 
	 * 
	 * 
	 * @return
	 */
	public static ThreadSafeSingletonWithDoubleCheckMechanism getInstance() {

		// allows READ in parallel, BUT this is not synchronized or volatile READ (may
		// cause issue on cache)
		// we have non-synchronized READ
		// Which means updates in CPU CACHE may not guaranteed here, so still blocking
		// may happen
		if (instance != null) {
			return instance;
		}

		// WRITE operation is synchronized or volatile WRITE?
		// Yes by synchronized block (not by volatile)
		// We have synchronized WRITE
		synchronized (ThreadSafeSingletonWithDoubleCheckMechanism.class) {
			if (instance == null) {
				instance = new ThreadSafeSingletonWithDoubleCheckMechanism();
			}
		}

		// So to GURANTEEE READ operation get value by WRITE,
		// WE need a "Happens Before LINK" between READ and WRITE operation
		// BUT we do not HAVE it
		// Because "Happens Before LINK" between synchronized or volatile READ/WRITE
		// together

		// So DOUBLE check is BUGGY
		// It is not happening on SINGLE CORE CPU (No Visibility issue on Single core
		// CPU)
		//

		return instance;
	}
	
	//this does not provide solution to READ issue 
	public static ThreadSafeSingletonWithDoubleCheckMechanism getInstance2() { // Single Checked
		if (instance == null) {

			synchronized (ThreadSafeSingletonWithDoubleCheckMechanism.class) {
				// Double checked
				if (instance == null) {
					instance = new ThreadSafeSingletonWithDoubleCheckMechanism();
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

//This fixes above Double Lock Mechanism READ issues
//But again end up PERFORMANCE PENALTY like in First Solution
class ThreadSafeSingletonWithVolatile {

	/**
	 * volatile guarantees visibility between threads
	 */
	private static volatile ThreadSafeSingletonWithVolatile instance;

	/**
	 * You can not create multiple instance via reflection and private constructor
	 * is not visible besides this class.
	 */
	private ThreadSafeSingletonWithVolatile() {
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
	public static ThreadSafeSingletonWithVolatile getInstance() {

		// Now we have a VOLATILE READ
		if (instance != null) {
			return instance;
		}

		// WRITE operation is synchronized or volatile WRITE?
		// Yes by synchronized block (not by volatile)
		// We have synchronized WRITE
		synchronized (ThreadSafeSingletonWithVolatile.class) {
			if (instance == null) {
				instance = new ThreadSafeSingletonWithVolatile();
			}
		}

		return instance;
	}

	@Override
	public String toString() {
		return "Hello from " + this.getClass().getSimpleName() + "#" + hashCode();
	}
}

// e,g, see Java 8 Comparator#naturalOrder 
//BEST solution is use ENUM with SINGLE field, it is thread safe+performant(READ is available for parallelism)
/**
 *  * Singleton via Enum
 * 
 * Best way so far to implement singleton. Simple, short and provides safety and
 * serialization for free. 
 * 
 * <pre>
* Pros(+) and Cons(-)
* 
* + JVM ensures only one instantiation of enum so it is by default Thread Safe
 * 
 * + Easy to implement
 * 
 * + No explicit synchronization needed reason is Enums are thread safe
 * 
 * + No issue with Serialization
 * 
 * + There are no problems with respect to reflection (you can not create
 * multiple instances) 
 * 
 * - Enum is somewhat inflexible as superclass is always enum
 * 
 * - Enum's additional public methods may make an issue or situation more confused or complicated.
 * @author as892333
 *
 */
enum EnumSingletonRightSolution {
	INSTANCE;

	public void hi() {
		System.out.println("");
	}

	@Override
	public String toString() {
		return "Hello from " + this.getClass().getSimpleName() + "#" + hashCode();
	}

}
 
