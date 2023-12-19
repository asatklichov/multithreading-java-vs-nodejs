package concurrency.java.core.api;

/**
 * Singleton via Enum
 * 
 * Best way so far to implement singleton. Simple, short and provides safety and
 * serialization for free. 
 * 
 * <pre>
* Pros(+) and Cons(-)
* 
* + JVM ensures only one instantiation of enum so it is by default Thread Safe
 * 
 * + Easy to implement
 * 
 * + No explicit synchronization needed reason is Enums are thread safe
 * 
 * + No issue with Serialization
 * 
 * + There are no problems with respect to reflection (you can not create
 * multiple instances) 
 * 
 * - Enum is somewhat inflexible as superclass is always enum
 * 
 * - Enum's additional public methods may make an issue or situation more confused or complicated.
 * </pre>
 */

public enum EnumSingleton {
	INSTANCE;

	public void hi() {
		System.out.println("");
	}

	@Override
	public String toString() {
		return "Hello from " + this.getClass().getSimpleName() + "#" + hashCode();
	}
}
