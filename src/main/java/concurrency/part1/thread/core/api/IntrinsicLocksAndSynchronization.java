package concurrency.part1.thread.core.api;

public class IntrinsicLocksAndSynchronization {
	// https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html
	public static void main(String[] args) {
		/**
		 * Synchronization is built around an internal entity known as the intrinsic
		 * lock or monitor lock. (The API specification often refers to this entity
		 * simply as a "monitor.") Intrinsic locks play a role in both aspects of
		 * synchronization: enforcing exclusive access to an object's state and
		 * establishing happens-before relationships that are essential to visibility.
		 * 
		 * Intrinsic [natural, real, yaradylyşdan, hakyky] 
		 */
		System.out.println("     Intrinsic[natural, real ] lock or monitor lock    ");
		System.out.println(
				"Every object has an intrinsic lock associated with it. By convention, a thread that needs exclusive and consistent access to an object's \n"
						+ "fields has to acquire the object's intrinsic lock before accessing them, and then release the intrinsic lock when it's done with them.");
		System.out.println("As long as a thread owns an intrinsic lock, no other thread can acquire the same lock.");
		System.out.println("e.g. Locks In Synchronized Methods ");

		System.out.println("\n   Reentrant Synchronization");
		System.out.println(
				"Recall that a thread cannot acquire a lock owned by another thread. But a thread can acquire a lock that it already owns. /n"
						+ "Allowing a thread to acquire the same lock more than once enables reentrant synchronization. ");
		/**
		 * Recall that a thread cannot acquire a lock owned by another thread. But a
		 * thread can acquire a lock that it already owns. Allowing a thread to acquire
		 * the same lock more than once enables reentrant synchronization. This
		 * describes a situation where synchronized code, directly or indirectly,
		 * invokes a method that also contains synchronized code, and both sets of code
		 * use the same lock. Without reentrant synchronization, synchronized code would
		 * have to take many additional precautions to avoid having a thread cause
		 * itself to block.
		 */

	}
}
