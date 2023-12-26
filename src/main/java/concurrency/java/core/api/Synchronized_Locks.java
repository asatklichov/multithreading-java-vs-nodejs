package concurrency.java.core.api;

/**
 * Synchronization and Locks
 * 
 * 
 * How does synchronization work?
 * 
 * <pre>
 * 
 * 
 * With locks. Every object in Java has a built-in lock that only comes into
 * play when the object has synchronized method code. When we enter a
 * synchronized non-static method, we automatically acquire the lock associated
 * with the current instance of the class whose code we're executing (the this
 * instance).
 * 
 * Acquiring a lock for an object is also known as getting the lock, or locking
 * the object, locking on the object, or synchronizing on the object. 
 * 
 * We may also use the 
 * term monitor 
 * 
 * to refer to the object whose lock we're acquiring.
 * 
 * 
 * Technically the lock and the monitor are two different things, but most
 * people talk about the two interchangeably, and we will too.
 * 
 * 
 * Since there is only one lock per object, if one thread has picked up the
 * lock, no other thread can pick up the lock until the first thread releases
 * (or returns) the lock.
 * 
 * 
 * This means no other thread can enter the synchronized code (which means it
 * can't enter any synchronized method of that object) until the lock has been
 * released. 
 * 
 * Typically, releasing a lock means 
 * the thread holding the lock (in
 * other words, the thread currently in the synchronized method) exits the
 * synchronized method. At that point, the lock is free until some other thread
 * enters a synchronized method on that object. 
 * 
 * Remember the following key points about locking and synchronization:
 * 
 * ■ Only methods (or blocks) can be synchronized, not variables or classes. 
 * ■ Each object has just one lock. 
 * ■ Not all methods in a class need to be synchronized. A class can have both  
 * synchronized and non-synchronized methods. 
 * ■ If two threads are about to execute a synchronized method in a class, and 
 * both threads are using the same instance of the class to invoke the method, 
 * 
 * only one thread at a time will be able to execute the method.  
 * 
 * The other thread will need to wait until the first one finishes its method call.  In other 
 * words, once a thread acquires the lock on an object, no other thread can 
 * enter any of the synchronized methods in that class (for that object).
 * 
 * ■ If a thread goes to sleep, it holds any locks it has—it doesn't release them. 
 * 
 * ■ A thread can acquire more than one lock. For example, a thread can enter a 
 * synchronized method, thus acquiring a lock, and then immediately invoke 
 * a synchronized method on a different object, thus acquiring that lock as 
 * well. As the stack unwinds, locks are released again. Also, if a thread acquires 
 * a lock and then attempts to call a synchronized method on that same 
 * object, no problem. The JVM knows that this thread already has the lock for 
 * this object, so the thread is free to call other synchronized methods on the 
 * same object, using the lock the thread already has. 
 * 
 * ■ You can synchronize a block of code rather than a method.
 * </pre>
 * 
 * 
 */
public class Synchronized_Locks {

}

class Sync_InstanceMethod_OrBlock {
	/*
	 * Because synchronization does hurt concurrency, you don't want to
	 * synchronize any more code than is necessary to protect your data. So if
	 * the scope of a method is more than needed, you can reduce the scope of
	 * the synchronized part to something less than a full method—to just a
	 * block. We call this, strangely, a synchronized block, and it looks like
	 * this:
	 */
	public void doStuffSynch_block() {
		System.out.println("not synchronized");
		synchronized (this) {
			System.out.println("synchronized");
		}
	}

	// synchronized method
	synchronized public void doStuffSynch_method() {
		System.out.println("synchronized method");
	}

	// /locks on WHAT, see below discussion
	/**
	 * When you synchronize a method, the object used to invoke the method is
	 * the object whose lock must be acquired. But when you synchronize a block
	 * of code, you specify which object's lock you want to use as the lock, so
	 * you could, for example, use some third-party object as the lock for this
	 * piece of code.
	 * 
	 * That gives you the ability to have more than one lock for code
	 * synchronization within a single object. Or you can synchronize on the
	 * current instance (this) as in the code above.
	 * 
	 * 
	 * Since that's the same instance that synchronized methods lock on, it
	 * means that you could always replace a synchronized method with a
	 * non-synchronized method containing a synchronized block. In other words,
	 * this:
	 */
	public synchronized void doStuffX() {
		System.out.println("synchronized");
	}

	// is equivalent to this:
	public void doStuffX_Alternative() {
		synchronized (this) {
			System.out.println("synchronized");
		}
	}

	/*
	 * These methods both have the exact same effect, in practical terms.
	 * 
	 * The compiled bytecodes may not be exactly the same for the two methods,
	 * but they could be—and any differences are not really important.
	 * 
	 * The first form is shorter and more familiar to most people, but the
	 * second can be more flexible.
	 */
}

/**
 * So What About Static Methods? Can They Be Synchronized?
 * 
 * static methods can be synchronized.
 * 
 * There is only one copy of the static data you're trying to protect, so you
 * only need one lock per class to synchronize static methods—a lock for the
 * whole class.
 * 
 * There is such a lock; every class loaded in Java has a corresponding instance
 * of java.lang.Class representing that class. It's that java.lang.Class
 * instance whose lock is used to protect the static methods of the class (if
 * they're synchronized).
 * 
 * There's nothing special you have to do to synchronize a static method: public
 * static synchronized int getCount() { return count; }
 * 
 * @author Azat
 * 
 */
class Sync_StaticMethod_OrBlock {

	private static int count = 10;

	/**
	 * So What About Static Methods? Can They Be Synchronized?
	 * 
	 * static methods can be synchronized. There is only one copy of the static
	 * data you're trying to protect, so you only need one lock per class to
	 * synchronize static methods—a lock for the whole class.
	 * 
	 * There is such a lock; every class loaded in Java has a corresponding
	 * instance of java.lang.Class representing that class.
	 * 
	 * It's that java.lang.Class instance whose lock is used to protect the
	 * static methods of the class (if they're synchronized). There's nothing
	 * special you have to do to synchronize a static method:
	 */
	public static synchronized int getCountSynch_method() {
		return ++count;
	}

	/**
	 * Again, this could be replaced with code that uses a synchronized block.
	 * If the method is defined in a class called MyClass, the equivalent code
	 * is as follows:
	 */
	public static int getCountSynch_block() {
		synchronized (Sync_StaticMethod_OrBlock.class) { // Wait—what's that
															// MyClass.class
															// thing?
			return ++count;
		}
	}

	/**
	 * Wait—what's that MyClass.class thing?
	 * 
	 * That's called a class literal. It's a special feature in the Java
	 * language that tells the compiler (who tells the JVM): go and find me the
	 * instance of Class that represents the class called MyClass. You can also
	 * do this with the following code:
	 */
	public static void classMethod() {
		Class cl;
		try {
			cl = Class.forName("MyClass");
			synchronized (cl) {
				// do stuff
			}
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}