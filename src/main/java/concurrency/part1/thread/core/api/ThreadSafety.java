package concurrency.part1.thread.core.api;

import java.math.BigInteger;

/**
 * https://www.baeldung.com/java-thread-safety
 * https://www.baeldung.com/java-concurrent-locks
 *
 *
 * In most cases, errors in multithreaded applications are the result of
 * incorrectly sharing state between several threads.
 * 
 * 1 - Therefore, the first approach that we'll look at is to achieve
 * thread-safety using stateless implementations.
 * 
 * 2. Thread local fields
 * a. either via private local fields on Thread extended class
 * b. or use ThreadLocal class
 * 
 * 3. Synchronized Collections
 * 
 * 4. Concurrent Collections
 * 
 * 5. Atomic objects
 * 
 * 6. Synchronized Methods
 * 
 * 7. Synchronized Statements (block )
 * 
 * 8. Volatile Fields
 * 
 * 9. Extrinsic Locking
 * 
 * 10. Reentrant Locks
 * 
 * 11. Read/Write Locks
 * 
 * 
 * 
 */
public class ThreadSafety {

}

/**
 * 1 - Therefore, the first approach that we'll look
 *
 */
class MathUtils {
    public static BigInteger factorial(int number) {
        BigInteger f = new BigInteger("1");
        for (int i = 2; i <= number; i++) {
            f = f.multiply(BigInteger.valueOf(i));
        }
        return f;
    }
}

/**
 * Even though we can use any Java object as an intrinsic lock, we should avoid
 * using Strings for locking purposes:
 * 
 * However, because of string interning, these two �Lock� values may actually
 * refer to the same object on the string pool.
 * 
 *
 */
class Class1 {
	private static final String LOCK = "Lock";
}

class Class2 {
	private static final String LOCK = "Lock";
}
