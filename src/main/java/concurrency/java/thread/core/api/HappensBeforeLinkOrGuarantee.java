package concurrency.java.thread.core.api;

public class HappensBeforeLinkOrGuarantee {
	public static void main(String[] args) {
		// https://jenkov.com/tutorials/java-concurrency/java-happens-before-guarantee.html
		//https://jenkov.com/tutorials/java-concurrency/volatile.html
		/**
		 * The Java happens before guarantee is a set of rules that govern how the Java
		 * VM and CPU is allowed to reorder instructions for performance gains.
		 * 
		 * 
		 * - The Java volatile Visibility Guarantee 
		 * 
		 * - The Java volatile Read Visibility
		 * Guarantee
		 * 
		 * 
		 * 
		 * 
		 * 
		 * The Java volatile keyword provides some visibility guarantees for when writes
		 * to, and reads of, volatile variables result in synchronization of the
		 * variable's value to and from main memory. This synchronization to and from
		 * main memory is what makes the value visible to other threads. Hence the term
		 * visibility guarantee.
		 * 
		 */
	}

}
