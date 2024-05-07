package concurrency.part1.thread.core.api;

/**
 * Synchronizing Code (Objective 4.3)
 * 
 * <pre>
 * 
 * 4.3 Given a scenario, write code that makes appropriate use of object locking to 
 * protect static or instance variables from concurrent access problems.
 * 
 * Can you imagine the havoc that can occur when two different threads have access 
 * to a single instance of a class, and both threads invoke methods on that object…and 
 * those methods modify the state of the object? In other words, what might happen 
 * if two different threads call, say, a setter method on a single object? 
 * 
 * A scenario 
 * like that might corrupt an object's state (by changing its instance variable values in 
 * an inconsistent way), and if that object's state is data shared by other parts of the 
 * program, well, it's too scary to even visualize.
 * But just because we enjoy horror, let's look at an example of what might happen. 
 * The following code demonstrates what happens when two different threads are 
 * accessing the same account data. Imagine that two people each have a checkbook 
 * for a single checking account (or two people each have ATM cards, but both cards 
 * are linked to only one account).
 * </pre>
 * 
 */
public class Synchronizing_Code {
	// see: AccountDanger, AccountSafe

}
