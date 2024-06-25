package concurrency.virtual.threads.api;

public class __VirtualThreads {

	/**
	 * https://jenkov.com/tutorials/java-concurrency/java-virtual-threads.html
	 * 
	 * Java virtual threads are a new thread construct added to Java from Java 19.
	 * Java virtual threads are different from the original platform threads in that
	 * virtual threads are much more lightweight in terms of how many resources
	 * (RAM) they demand from the system to run. Thus, you can have far more virtual
	 * threads running in your applications than platform threads.
	 * 
	 * With more virtual threads running you can do more blocking IO in parallel
	 * than with fewer platform threads. This is useful if your application needs to
	 * make many parallel network calls to external services such as REST APIs, or
	 * open many connections to external databases (via JDBC) or similar.
	 * 
	 * Virtual Threads are Still a Preview Feature Java virtual threads are still a
	 * preview feature in Java 19, meaning their implementation and the API to
	 * create them may still change. Also, you need to switch on Java preview
	 * features in your IDE to use them!
	 */
	public static void main(String[] args) {
		// https://docs.oracle.com/en/java/javase/20/core/virtual-threads.html#GUID-DC4306FC-D6C1-4BCC-AECE-48C32C1A8DAA
		// https://www.geeksforgeeks.org/difference-between-java-threads-and-os-threads/
		// https://medium.com/the-sixt-india-blog/java-virtual-threads-vs-platform-threads-performance-comparison-under-high-load-7a8490d1b668
 
		/**
		 * <pre>
		 * What is a Platform Thread?  (A Java thread which uses OS thread)
		A platform thread is implemented as a thin wrapper around an operating system (OS) thread. A platform 
		thread runs Java code on its underlying OS thread, and the platform thread captures its OS thread 
		for the platform thread's entire lifetime. Consequently, the number of available platform threads is 
		limited to the number of OS threads.
		
		Platform threads typically have a large thread stack and other resources that are maintained by the 
		operating system. Platform threads support thread-local variables.
		
		Platforms threads are suitable for running all types of tasks but may be a limited resource.
		
		What is a Virtual Thread?
		Like a platform thread, a virtual thread is also an instance of java.lang.Thread. However, a virtual thread 
		isn't tied to a specific OS thread.
		
		 A virtual thread still runs code on an OS thread. However, when code running in a virtual thread calls 
		 a blocking I/O operation, the Java runtime suspends the virtual thread until it can be resumed. 
		 
		 The OS thread associated with the 
		 suspended virtual thread is now FREEE to perform operations for other virtual threads.
		 
		 
		 Virtual threads are implemented in a similar way to virtual memory. To simulate a lot of memory, 
		 an operating system maps a large virtual address space to a limited amount of RAM. Similarly, to simulate a lot of threads, 
		 the Java runtime maps a large number of virtual threads to a small number of OS threads.
		 * 
		 * Unlike platform threads, virtual threads typically have a shallow call stack, performing as few as a single HTTP client 
		 * call or a single JDBC query. Although virtual threads support thread-local variables, 
		 * you should carefully consider using them because a single JVM might support millions of virtual threads.
		 * </pre>
		 */

	 

		//Thread vThread = Thread.ofVirtual().start(runnable);

	}

}
