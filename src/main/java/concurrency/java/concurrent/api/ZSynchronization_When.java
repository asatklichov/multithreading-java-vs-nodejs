package concurrency.java.core.api;

/**
 * So When Do I Need to Synchronize?
 * 
 * <pre>
 * 
 * Synchronization can get pretty complicated, and you may be wondering why you 
 * would want to do this at all if you can help it.  
 * 
 * But remember the earlier "race 
 * conditions" example with Lucy and Fred making withdrawals from their account.  
 * When we use threads, we usually need to use some synchronization somewhere to 
 * make sure our methods don't interrupt each other at the wrong time and mess up our 
 * data.  
 * 
 * Generally, any time more than one thread is accessing mutable (changeable) 
 * data, you synchronize to protect that data, to make sure two threads aren't changing 
 * it at the same time (or that one isn't changing it at the same time the other is 
 * reading it, which is also confusing).  
 * 
 * You don't need to worry about local variables— each thread gets its own copy of a local variable. 
 * 
 * Two threads executing the same method at the same time will use different copies of the local variables, and they 
 * won't bother each other.  
 * 
 * However, you do need to worry about static and non-
 * static fields, if they contain data that can be changed.  
 * For changeable data in a non-static field, you usually use a non-static method 
 * to access it.  By synchronizing that method, you will ensure that any threads trying
 * </pre>
 * 
 */
public class ZSynchronization_When {

}

/**
 * 
 * 
 * 
 * However�what if you have a non-static method that accesses a static field? Or
 * a static method that accesses a non-static field (using an instance)? In
 * these cases things start to get messy quickly, and there's a very good chance
 * that things will not work the way you want. If you've got a static method
 * accessing a non-static field, and you synchronize the method, you acquire a
 * lock on the Class object.
 * 
 * But what if there's another method that also accesses the non-static field,
 * this time using a non-static method? It probably synchronizes on the current
 * instance (this) instead. Remember that a static synchronized method and a
 * non-static synchronized method will not block each other�they can run at the
 * same time. Similarly, if you access a static field using a non-static method,
 * two threads might invoke that method using two different this instances.
 * Which means they won't block each other, because they use different locks.
 * Which means two threads are simultaneously accessing the same static
 * field�exactly the sort of thing we're trying to prevent.
 * 
 * 
 * It gets very confusing trying to imagine all the weird things that can happen
 * here.
 * 
 * 
 * To keep things simple: in order to make a class thread-safe, methods that
 * access changeable fields need to be synchronized.
 * 
 * 
 * Access to static fields should be done from static synchronized methods.
 * 
 * Access to non-static fields should be done from non-static synchronized
 * methods. For example:
 * 
 */
class Things {
	private static int staticField;
	private int nonstaticField;

	public static synchronized int getStaticField() {
		return staticField;
	}

	public static synchronized void setStaticField(int staticField) {
		Things.staticField = staticField;
	}

	public synchronized int getNonstaticField() {
		return nonstaticField;
	}

	public synchronized void setNonstaticField(int nonstaticField) {
		this.nonstaticField = nonstaticField;
	}
}
