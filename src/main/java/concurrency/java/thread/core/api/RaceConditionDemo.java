package concurrency.java.thread.core.api;

import java.util.Map;

class RaceConditionNotHappensUsingOneThread {

	public static void main(String[] args) throws InterruptedException {

		LongWrapperRaceCondition longWrapper = new LongWrapperRaceCondition(0L);

		Runnable r = () -> {

			for (int i = 0; i < 1_000; i++) {
				longWrapper.incrementValue();
			}
		};

		Thread thread = new Thread(r);
		thread.start();

		thread.join();// allows to finish Threads work firstly, so main thread will wait for it

		System.out.println("Value = " + longWrapper.getValue());// always 100, because it is used only by one thread
	}
}

class RaceConditionHappens {

	public static void main(String[] args) throws InterruptedException {

		LongWrapperRaceCondition longWrapper = new LongWrapperRaceCondition(0L);

		Runnable r = () -> {

			for (int i = 0; i < 1_000; i++) {
				longWrapper.incrementValue();
			}
		};

		Thread[] threads = new Thread[1_000];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(r);
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].join();// allows to run below code after whole thread execution ends, so main thread
								// will wait all threads to be finished
		}

		/**
		 * This is the exactly the RACE Condition happens, it is in incrementValue()
		 * method
		 */
		System.out.println("Value = " + longWrapper.getValue());// every launch it has different result, Value = 995368,
																// Value = 994294

	}
}

/**
 * Here we fix the Race condition which is happened on WRITE operation. But,
 * there is a VISIBILITY problem existing on LongWrapperFixedRaceCondition, so
 * 
 * See improved version of {@link VisibilityImprovesRaceConditionFix}
 *
 */
class RaceConditionFixedDemo {

	public static void main(String[] args) throws InterruptedException {

		LongWrapperFixedRaceCondition longWrapper = new LongWrapperFixedRaceCondition(0L);

		Runnable r = () -> {

			for (int i = 0; i < 1_000; i++) {
				longWrapper.incrementValue();
			}
		};

		Thread[] threads = new Thread[1_000];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(r);
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}

		System.out.println("Value = " + longWrapper.getValue());
	}
}

class MyRunnablz implements Runnable {

	// can cause race condition / write visibility issue
	private int count = 0;

	@Override
	public void run() {
		for (int i = 0; i < 1000000; i++) {
			this.count++;
		}
		System.out.println(Thread.currentThread().getName() + ": " + count);
	}
}

class MyRunnablz2 implements Runnable {

	// can cause race condition / write visibility issue
	private int count = 0;

	@Override
	public void run() {
		synchronized (this) {
			for (int i = 0; i < 1000000; i++) {
				this.count++;
			}
			System.out.println(Thread.currentThread().getName() + ": " + count);
		}
	}
}

class RaceConditionOtherDemo {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("\nrace condition not happening here - each thread(worker) has its own task(job)");
		/**
		 * two threads has not share Runnable, that is why each has count= 1000000
		 */
		MyRunnablz run1 = new MyRunnablz();
		MyRunnablz run2 = new MyRunnablz();
		Thread t1 = new Thread(run1);
		Thread t2 = new Thread(run2);
		t1.start();
		t2.start();
		t1.join();
		t2.join();

		System.out.println("\nrace condition happens - same task(job) run by two different threads(workers)");

		/**
		 * Here two threads SHARE Runnable, then count gets below 1000000. ... That is
		 * why result is UNPREDICTABLE ...
		 * 
		 * Reason happens during READ/WRITE, they override each others COUNT and so on,
		 * One thread is updating while second is reading
		 * 
		 * FIX ?
		 */

		/**
		 * Thread-0: 1892119
		 * 
		 * Thread-1: 1892119
		 */
		MyRunnablz run = new MyRunnablz();
		/**
		 * Unpredictable
		 * 
		 * Thread-1: 1979915
		 * 
		 * Thread-0: 2000000
		 */
		// MyRunnablz2 run = new MyRunnablz2();
		t1 = new Thread(run);
		t2 = new Thread(run);

		t1.start();
		t2.start();
		t1.join();
		t2.join();

		System.out.println("\nrace condition happens - same task(job) run by multiple threads(workers)");

		run1 = new MyRunnablz();
		Thread[] threads = new Thread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(run1);
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].join();// allows to run below code after whole thread execution ends, so main thread
								// will wait all threads to be finished
		}

		System.out.println(
				"\nrace condition NOT happens (uses synchronized solution) - same task(job) run by two different threads(workers)");
		MyRunnablz2 runz = new MyRunnablz2();
		t1 = new Thread(runz);
		t2 = new Thread(runz);

		t1.start();
		t2.start();
		t1.join();
		t2.join();

		System.out.println(
				"\nrace condition NOT happens (uses synchronized solution) - same task(job) run by multiple threads(workers)");

		threads = new Thread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(runz);
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].join();// allows to run below code after whole thread execution ends, so main thread
								// will wait all threads to be finished
		}

	}
}

/**
 *
 * Two Types of Race Conditions
 * 
 * <pre>
Race conditions can occur when two or more threads read and write the same variable according to one of these two patterns:

Read-modify-write (above examples)
------------------
The read-modify-write pattern means, that two or more threads first read a given variable, 
then modify its value and write it back to the variable. For this to cause a problem, the new value must 
depend one way or another on the previous value. The problem that can occur is, if two threads read the value 
(into CPU registers) then modify the value (in the CPU registers) and then write the values back. 
This situation is explained in more detail later.


Check-then-act (below example)
--------------
The check-then-act pattern means, that two or more threads check a given condition, for instance if a 
Map contains a given value, and then go on to act based on that information, e.g. taking the value from the Map. 
The problem may occur if two threads check the Map for a given value at the same time - see that the value is 
present - and then both threads try to take (remove) that value. However, only one of the threads can actually 
take the value. The other thread will get a null value back. This could also happen if a Queue was used instead of a Map.
 * </pre>
 */
class CheckThenActExample {

	/**
	 * As also mentioned above, a check-then-act critical section can also lead to
	 * race conditions. If two threads check the same condition, then act upon that
	 * condition in a way that changes the condition it can lead to race conditions.
	 * If two threads both check the condition at the same time, and then one thread
	 * goes ahead and changes the condition, this can lead to the other thread
	 * acting incorrectly on that condition.
	 */
	public void checkThenAct(Map<String, String> sharedMap) {
		if (sharedMap.containsKey("key")) {
			String val = sharedMap.remove("key");
			if (val == null) {
				System.out.println("Value for 'key' was null");
			}
		} else {
			sharedMap.put("key", "value");
		}
	}
}