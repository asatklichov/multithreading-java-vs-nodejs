package concurrency.java.core.api;


public class ThreadLocalDemo {

	// https://www.baeldung.com/java-threadlocal
	public static void main(String[] args) {

		ThreadLocal<Integer> threadLocalValue = new ThreadLocal<>();
		threadLocalValue.set(1);
		Integer result = threadLocalValue.get();
		System.out.println(result);

		// We can construct an instance of the ThreadLocal by using the withInitial()
		// static method and passing a supplier to it:
		ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 23);
		System.out.println(threadLocal.get());
		threadLocal.remove();
		/**
		 * Returns the value in the current thread's copy of thisthread-local variable.
		 * If the variable has no value for thecurrent thread, it is first initialized
		 * to the value returnedby an invocation of the initialValue method.
		 */
		System.out.println(threadLocal.get());//current thread so you see still 23
		

	}

}
