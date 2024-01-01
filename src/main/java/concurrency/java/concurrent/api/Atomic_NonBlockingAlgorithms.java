package concurrency.java.concurrent.api;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.function.Function;

import sun.misc.Unsafe;

class Counter {
	private int c = 0;

	public void increment() {
		c++;
	}

	public void decrement() {
		c--;
	}

	public int value() {
		return c;
	}

}

/**
 * One way to make Counter safe from thread interference is to make its methods
 * synchronized, as in SynchronizedCounter:
 * 
 * @return
 */
class SynchronizedCounter {
	private int c = 0;

	public synchronized void increment() {
		c++;
	}

	public synchronized void decrement() {
		c--;
	}

	public synchronized int value() {
		return c;
	}

}

/**
 * For this simple class, synchronization is an acceptable solution. But for a
 * more complicated class, we might want to avoid the liveness impact of
 * unnecessary synchronization. Replacing the int field with an AtomicInteger
 * allows us to prevent thread interference without resorting to
 * synchronization, as in AtomicCounter:
 */

class AtomicCounterz {
	private AtomicInteger c = new AtomicInteger(0);

	public void increment() {
		c.incrementAndGet();
	}

	public void decrement() {
		c.decrementAndGet();
	}

	public int value() {
		return c.get();
	}

}

class AtomicCounterDemo {

	private static class MyAtomicCounter extends AtomicInteger {

		private static Unsafe unsafe = null;

		static {
			Field unsafeField;
			try {
				unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
				unsafeField.setAccessible(true);
				unsafe = (Unsafe) unsafeField.get(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private AtomicInteger countIncrement = new AtomicInteger(0);

		public MyAtomicCounter(int counter) {
			super(counter);
		}

		public int myIncrementAndGet() {

			long valueOffset = 0L;
			try {
				valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			int v;
			do {
				v = unsafe.getIntVolatile(this, valueOffset);
				countIncrement.incrementAndGet();
			} while (!unsafe.compareAndSwapInt(this, valueOffset, v, v + 1));

			return v;
		}

		public int getIncrements() {
			return this.countIncrement.get();
		}
	}

	private static MyAtomicCounter counter = new MyAtomicCounter(0);

	public static void main(String[] args) {

		class Incrementer implements Runnable {

			public void run() {
				for (int i = 0; i < 1_000; i++) {
					counter.myIncrementAndGet();
				}
			}
		}

		class Decrementer implements Runnable {

			public void run() {
				for (int i = 0; i < 1_000; i++) {
					counter.decrementAndGet();
				}
			}
		}

		ExecutorService executorService = Executors.newFixedThreadPool(8);
		List<Future<?>> futures = new ArrayList<>();

		try {

			for (int i = 0; i < 4; i++) {
				futures.add(executorService.submit(new Incrementer()));
			}
			for (int i = 0; i < 4; i++) {
				futures.add(executorService.submit(new Decrementer()));
			}

			futures.forEach(future -> {
				try {
					future.get();
				} catch (InterruptedException | ExecutionException e) {
					System.out.println(e.getMessage());
				}
			});

			System.out.println("counter = " + counter);
			System.out.println("# increments = " + counter.getIncrements());

		} finally {
			executorService.shutdown();
		}
	}
}

/**
 * https://jenkov.com/tutorials/java-concurrency/non-blocking-algorithms.html
 * https://jenkov.com/tutorials/java-util-concurrent/atomicboolean.html
 * 
 * 
 * Blocking Concurrency Algorithms A blocking concurrency algorithm is an
 * algorithm which either:
 * 
 * A: Performs the action requested by the thread - OR B: Blocks the thread
 * until the action can be performed safely
 * 
 * Many types of algorithms and concurrent data structures are blocking. For
 * instance, the different implementations of the
 * java.util.concurrent.BlockingQueue interface are all blocking data
 * structures.
 * 
 * <pre>
 * Non-blocking Concurrency Algorithms
A non-blocking concurrency algorithm is an algorithm which either:

A: Performs the action requested by the thread - OR
B: Notifies the requesting thread that the action could not be performed


Java contains several non-blocking data structures too.

 The 
 AtomicBoolean, AtomicInteger, 
 AtomicLong and AtomicReference 
 
 are all examples of non-blocking data structures.
 * </pre>
 */
public class Atomic_NonBlockingAlgorithms {
	public static void main(String[] args) {
		System.out.println("\nNon-blocking vs Blocking Algorithms");
		System.out.println("\nnon-blocking data structures ");
		/**
		 * The main difference between blocking and non-blocking algorithms lies in the
		 * second step of their behaviour as described in the above two sections. In
		 * other words, the difference lies in what the blocking and non-blocking
		 * algorithms do when the requested action cannot be performed:
		 * 
		 * Blocking algorithms block the thread until the requested action can be
		 * performed. Non-blocking algorithms notify the thread requesting the action
		 * that the action cannot be performed.
		 */

		/*
		 * The AtomicBoolean class provides you with a boolean variable which can be
		 * read and written atomically, and which also contains advanced atomic
		 * operations like compareAndSet(). The AtomicBoolean class is located in the
		 * java.util.concurrent.atomic package, so the full class name is
		 * java.util.concurrent.atomic.AtomicBoolean . This text describes the version
		 * of AtomicBoolean found in Java 8, but the first version was added in Java 5.
		 */
		AtomicBoolean atomicBoolean = new AtomicBoolean();
		System.out.println(atomicBoolean.get());
		AtomicBoolean atomicBooleanInitialized = new AtomicBoolean(true);
		System.out.println(atomicBooleanInitialized.get());

		System.out.println();
		atomicBoolean.set(true);
		System.out.println(atomicBoolean.get());
		System.out.println("Swapping the AtomicBoolean's Value");
		atomicBooleanInitialized.getAndSet(false);
		System.out.println(atomicBooleanInitialized.get());
		System.out.println();

		/**
		 * Compare and Set AtomicBoolean's Value The method compareAndSet() allows you
		 * to compare the current value of the AtomicBoolean to an expected value, and
		 * if current value is equal to the expected value, a new value can be set on
		 * the AtomicBoolean. The compareAndSet() method is atomic, so only a single
		 * thread can execute it at the same time. Thus, the compareAndSet() method can
		 * be used to implemented simple synchronizers like locks.
		 */
		atomicBoolean = new AtomicBoolean(true);
		boolean expectedValue = true;
		boolean newValue = false;
		boolean wasNewValueSet = atomicBoolean.compareAndSet(expectedValue, newValue);
		System.out.println("wasNewValueSet: " + wasNewValueSet);
		System.out.println(atomicBoolean.get());

		System.out.println();
		AtomicInteger atomicInteger = new AtomicInteger(123);
		int theValue = atomicInteger.get();
		System.out.println(theValue);
		atomicInteger.set(3232);
		System.out.println(atomicInteger.get());

		atomicInteger = new AtomicInteger(123);

		int expectedValue2 = 123;
		int newValue2 = 2334;
		boolean compareAndSet = atomicInteger.compareAndSet(expectedValue2, newValue2);
		System.out.println("wasNewValueSet: " + compareAndSet);
		System.out.println(atomicInteger.get());
		System.out.println();

		System.out.println(atomicInteger.getAndAdd(10));// 10++
		System.out.println(atomicInteger.addAndGet(10));// ++10
		System.out.println(atomicInteger.getAndDecrement());// 1--
		System.out.println(atomicInteger.decrementAndGet());// --1

		System.out.println();
		AtomicLong atomicLong = new AtomicLong(123);
		long theLongValue = atomicLong.get();
		System.out.println(theLongValue);

		atomicLong.set(4232);
		System.out.println(theLongValue);

		atomicLong = new AtomicLong(123);

		long expectedValue3 = 123;
		long newValue3 = 2334;
		boolean compareAndSet3 = atomicLong.compareAndSet(expectedValue3, newValue3);
		System.out.println("wasNewValueSet3: " + compareAndSet3);
		System.out.println(atomicLong.get());
		System.out.println();

		System.out.println(atomicLong.getAndAdd(10));// 10++
		System.out.println(atomicLong.addAndGet(10));// ++10
		System.out.println(atomicLong.getAndDecrement());// 1--
		System.out.println(atomicLong.decrementAndGet());// --1

		System.out.println("\nUsing an AtomicLong as a Counter in a Lambda Expression");
		/**
		 * A Java Lambda Expression cannot contain any member fields, and thus they
		 * cannot keep any state internally between calls to the lambda expression.
		 * However, you can bypass this limitation by creating an AtomicLong outside the
		 * Lambda Expression and use it from inside the Lambda Expression. Here is an
		 * example of that:
		 */
		AtomicLong atomicLong2 = new AtomicLong();

		Function<Long, Long> myLambda = (input) -> {
			long noOfCalls = atomicLong2.incrementAndGet();
			System.out.println("Lambda called " + noOfCalls + " times.");
			return input * 2;
		};

		// see the problem, you can not do it without Atomic Variables
		Integer IamInt = 433432;
		Function<Integer, Integer> Integer = (input) -> {
			// IamInt++; //must be effective FINAL, you can not re-assign
			System.out.println("Lambda called " + IamInt + " times.");
			return input * 2;
		};

		System.out.println(myLambda.apply(1L));
		System.out.println(myLambda.apply(3L));
		System.out.println(myLambda.apply(5L));

		System.out.println("\nAtomicReference");
		/**
		 * The AtomicReference class provides an object reference variable which can be
		 * read and written atomically. By atomic is meant that multiple threads
		 * attempting to change the same AtomicReference (e.g. with a compare-and-swap
		 * operation) will not make the AtomicReference end up in an inconsistent state.
		 * AtomicReference even has an advanced compareAndSet() method which lets you
		 * compare the reference to an expected value (reference) and if they are equal,
		 * set a new reference inside the AtomicReference object.
		 */
		AtomicReference atomicReference = new AtomicReference();

		//// .If you need to create the AtomicReference with an initial reference, you
		//// can do so like this:

		String initialReference = "the initially referenced string";
		atomicReference = new AtomicReference(initialReference);
		// CAST needed
		String reference = (String) atomicReference.get();
		System.out.println(reference);

		// types a-ref
		AtomicReference<String> atomicStringReference = new AtomicReference<String>(initialReference);
		// no cast needed
		String ref = atomicStringReference.get();
		System.out.println(ref);

		// set
		atomicReference.set("New object referenced");
		System.out.println(atomicReference.get()); // (String) no needed - implicitly call toString

		System.out.println(atomicStringReference.get());
		System.out.println();
		// compareAndSet
		String newReference = "new value referenced";
		boolean exchanged = atomicStringReference.compareAndSet(initialReference, newReference);
		System.out.println("exchanged: " + exchanged);
		System.out.println(atomicStringReference.get());
		exchanged = atomicStringReference.compareAndSet(initialReference, newReference);
		System.out.println("exchanged: " + exchanged);

		System.out.println(atomicStringReference.get());

		System.out.println("\n AtomicStampedReference");
		/**
		 * The AtomicStampedReference class provides an object reference variable which
		 * can be read and written atomically. By atomic is meant that multiple threads
		 * attempting to change the same AtomicStampedReference will not make the
		 * AtomicStampedReference end up in an inconsistent state.
		 * 
		 * The AtomicStampedReference is different from the AtomicReference in that the
		 * AtomicStampedReference keeps both an object reference and a stamp internally.
		 * The reference and stamp can be swapped using a single atomic compare-and-swap
		 * operation, via the compareAndSet() method.
		 */

//		Object initialRef = null;
//		int initialStamp = 0;
//
//		AtomicStampedReference atomicStampedReference = new AtomicStampedReference(intialRef, initialStamp);

		String initialRef = "first text";
		int initialStamp = 0;

		AtomicStampedReference<String> atomicStampedReference =

				new AtomicStampedReference<>(initialRef, 0);

		reference = (String) atomicStampedReference.getReference();
		System.out.println(reference);
		int stamp = atomicStampedReference.getStamp();
		System.out.println(stamp);

		int[] stampHolder = new int[1];
		ref = atomicStampedReference.get(stampHolder);

		System.out.println("ref = " + ref);
		System.out.println("stamp = " + stampHolder[0]);

		String initialRefz = "initial value referenced";
		int initialStampz = 0;

		AtomicStampedReference<String> atomicStringReferencez = new AtomicStampedReference<String>(initialRef,
				initialStamp);

		String newRefz = "new value referenced";
		int newStampz = initialStamp + 1;

		boolean exchangedz = atomicStringReferencez.compareAndSet(initialRefz, newRefz, initialStampz, newStampz);
		System.out.println("exchanged: " + exchanged); // true

		exchangedz = atomicStringReferencez.compareAndSet(initialRefz, "new string", newStampz, newStampz + 1);
		System.out.println("exchanged: " + exchanged); // false

		exchangedz = atomicStringReferencez.compareAndSet(newRefz, "new string", initialStampz, newStampz + 1);
		System.out.println("exchanged: " + exchanged); // false

		exchangedz = atomicStringReferencez.compareAndSet(newRefz, "new string", newStampz, newStampz + 1);
		System.out.println("exchanged: " + exchanged); // true

		System.out.println("Helps to solve A-B-A problem");
		/**
		 * The AtomicStampedReference is designed to be able to solve the A-B-A problem
		 * which is not possible to solve with an AtomicReference alone. The A-B-A
		 * problem is explained later in this text.
		 * 
		 * 
		 * The AtomicStampedReference is designed to solve the A-B-A problem. The A-B-A
		 * problem is when a reference is changed from pointing to A, then to B and then
		 * back to A.
		 * 
		 * When using compare-and-swap operations to change a reference atomically, and
		 * making sure that only one thread can change the reference from an old
		 * reference to a new, detecting the A-B-A situation is impossible.
		 * 
		 * The A-B-A problem can occur in non-blocking algorithms. Non-blocking
		 * algorithms often use a reference to an ongoing modification to the guarded
		 * data structure, to signal to other threads that a modification is currently
		 * ongoing. If thread 1 sees that there is no ongoing modification (reference
		 * points to null), another thread may submit a modification (reference is now
		 * non-null), complete the modification and swap the reference back to null
		 * without thread 1 detecting it. Exactly how the A-B-A problem occurs in
		 * non-blocking algorithms is explained in more detail in my tutorial about
		 * non-blocking algorithms.
		 */
		stampHolder = new int[1];
		ref = atomicStampedReference.get(stampHolder);

		if (ref == null) {
			// prepare optimistic modification
		}

		// if another thread changes the
		// reference and stamp here,
		// it can be detected

		int[] stampHolder2 = new int[1];
		Object ref2 = atomicStampedReference.get(stampHolder);

		if (ref == ref2 && stampHolder[0] == stampHolder2[0]) {
			// no modification since
			// optimistic modification was prepared

		} else {
			// retry from scratch.
		}

		System.out.println("\nCreating an AtomicIntegerArray");
		/**
		 * The Java AtomicIntegerArray class
		 * (java.util.concurrent.atomic.AtomicIntegerArray) represents an array of int .
		 * The int elements in the AtomicIntegerArray can be updated atomically. The
		 * AtomicIntegerArray also supports compare-and-swap functionality.
		 */
		AtomicIntegerArray array = new AtomicIntegerArray(10);
		// This example creates a AtomicIntegerArray with a capacity of 10 ints (it has
		// space for 10 int elements).

		// The second constructor takes an int[] array as parameter.
		int[] ints = new int[10];
		ints[5] = 1023;
		array = new AtomicIntegerArray(ints);

		int value = array.get(5);
		System.out.println(value);

		array.set(5, 999);
		System.out.println(array.get(5));

		boolean swapped = array.compareAndSet(5, 999, 123);
		System.out.println(swapped);
		System.out.println(array.get(5));

		int newValuez = array.addAndGet(5, 3);// ++3
		System.out.println(newValuez);

		newValuez = array.getAndAdd(5, 3);// 3++, later becomes 129
		System.out.println(newValuez);

		newValuez = array.incrementAndGet(5); // ++1
		System.out.println(newValuez);
		newValuez = array.getAndIncrement(5); // 1++
		System.out.println(newValuez);

		System.out.println("\nAtomicLongArray");
		// https://jenkov.com/tutorials/java-util-concurrent/atomiclongarray.html

		System.out.println("\nAtomicReferenceArray");
		// https://jenkov.com/tutorials/java-util-concurrent/atomicreferencearray.html

	}

}
