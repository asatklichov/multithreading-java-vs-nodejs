package concurrency.java.core.api;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Java ThreadLocal
 * 
 * <pre>
*The Java ThreadLocal class enables you to create variables that can only 
*be read and written by the same thread.
*
*Thus, even if two threads are executing the same code, and the code has 
*a reference to the same ThreadLocal variable, the two threads cannot see each other's 
*ThreadLocal variables.
 * </pre>
 */
public class ThreadLocalDemo {

	public static void main(String[] args) throws InterruptedException {
		MyRunnablee sharedRunnableInstance = new MyRunnablee();

		/**
		 * Both threads execute the run() method, and thus sets different values on the
		 * ThreadLocal instance. If the access to the set() call had been synchronized,
		 * and it had not been a ThreadLocal object, the second thread would have
		 * overridden the value set by the first thread.
		 */
		Thread thread1 = new Thread(sharedRunnableInstance);
		Thread thread2 = new Thread(sharedRunnableInstance);

		thread1.start();
		thread2.start();

		thread1.join(); // wait for thread 1 to terminate
		System.out.println();
		thread2.join(); // wait for thread 2 to terminate

	}
}

class MyRunnablee implements Runnable {

	private ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

	/**
	 * https://www.baeldung.com/java-threadlocal
	 */
	private ThreadLocal<Integer> threadLocalValue = new ThreadLocal<>();

	/**
	 * We can construct an instance of the ThreadLocal by using the withInitial()
	 * static method and passing a supplier to it:
	 */
	ThreadLocal<Integer> threadLocalWithInitial = ThreadLocal.withInitial(() -> 1);

	@Override
	public void run() {
		int rndVal = (int) (Math.random() * 100D);
		threadLocal.set(rndVal);

		threadLocalValue.set(rndVal * 100);
		Integer result = threadLocalValue.get();
		System.out.println("threadLocalValue = " + result);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		System.out.println(threadLocal.get());
		/**
		 * To remove value from the ThreadLocal we can call a remove() method:
		 */
		threadLocal.remove();
		threadLocalValue.remove();
		System.out.println("--"+threadLocal.get());
		System.out.println("=="+threadLocalValue.get());
	}
}

class MyDateFormatter {

	private ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<>();

	public String format(Date date) {
		SimpleDateFormat simpleDateFormat = getThreadLocalSimpleDateFormat();
		return simpleDateFormat.format(date);
	}

	private SimpleDateFormat getThreadLocalSimpleDateFormat() {
		SimpleDateFormat simpleDateFormat = simpleDateFormatThreadLocal.get();
		if (simpleDateFormat == null) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			simpleDateFormatThreadLocal.set(simpleDateFormat);
		}
		return simpleDateFormat;
	}
}
