package concurrency.java.thread.core.api;

/**
 * * Assume you have a class that holds two private variables: a and b. Which of
 * the following pairs can prevent concurrent access problems in that class?
 * (Choose all that apply.)
 * 
 * <pre>
 * 
 * A.   public int read(){return a+b;}
 * public void set(int a, int b){this.a=a;this.b=b;}
 * 
 * B.   public synchronized int read(){return a+b;}
 * public synchronized void set(int a, int b){this.a=a;this.b=b;}
 * 
 * C.   public int read(){synchronized(a){return a+b;}}
 * public void set(int a, int b){synchronized(a){this.a=a;this.b=b;}} 
 * 
 * D.   public int read(){synchronized(a){return a+b;}}
 * public void set(int a, int b){synchronized(b){this.a=a;this.b=b;}} 
 * 
 * E.   public synchronized(this) int read(){return a+b;}
 * public synchronized(this) void set(int a, int b){this.a=a;this.b=b;} 
 * 
 * F.   public int read(){synchronized(this){return a+b;}}
 * public void set(int a, int b){synchronized(this){this.a=a;this.b=b;}}
 * 
 * 
 * 
 * Answer:
 *     ✓    B and F are correct. By marking the methods as synchronized, the threads will get the 
 * lock of the this object before proceeding. Only one thread will be setting or reading at any 
 * given moment, thereby assuring that read() always returns the addition of a valid pair. 
 *       A is incorrect because it is not synchronized; therefore, there is no guarantee that the values 
 * added by the read() method belong to the same pair. C and D are incorrect; only objects 
 * can be used to synchronize on. E fails —it is not possible to select other objects (even this) 
 * to synchronize on when declaring a method as synchronized.  (Objective 4.3)
 * </pre>
 * 
 */
public class TestThreadSynch {

}

/**
 * 
 * Which of the following pairs of method invocations could NEVER be executing
 * at the same time? (Choose all that apply.)
 * 
 * <pre>
 *    A.  x.a() in thread1, and x.a() in thread2
 *    B.  x.a() in thread1, and x.b() in thread2
 *    C.  x.a() in thread1, and y.a() in thread2
 *    D.  x.a() in thread1, and y.b() in thread2
 *    E.  x.b() in thread1, and x.a() in thread2
 *    F.  x.b() in thread1, and x.b() in thread2
 *    G.  x.b() in thread1, and y.a() in thread2
 *    H.  x.b() in thread1, and y.b() in thread2
 *    
 *    
 *    
 *    Answer:
 *      ✓    A, F,  and H. A is a right answer because when synchronized instance methods are called
 *  on the same instance, they block each other. F and H can't happen because synchronized
 *  static methods in the same class block each other, regardless of which instance was used
 *  to call the methods. (An instance is not required to call static methods; only the class.)
 *        C could happen because synchronized instance methods called on different instances 
 * do not block each other. B, D, E, and G could all happen because instance methods and 
 * static methods lock on different objects, and do not block each other.  (Objective 4.3)
 * </pre>
 * 
 */
class ThreadDemo {
	synchronized void a() {
		actBusy();
	}

	static synchronized void b() {
		actBusy();
	}

	static void actBusy() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) {
		final ThreadDemo x = new ThreadDemo();
		final ThreadDemo y = new ThreadDemo();
		Runnable runnable = new Runnable() {
			public void run() {
				int option = (int) (Math.random() * 4);
				switch (option) {
				case 0:
					System.out.println("x.a()");
					;
					break;
				case 1:
					x.b();
					System.out.println("x.b()");
					break;
				case 2:
					y.a();
					System.out.println("y.a()");
					break;
				case 3:
					y.b();
					System.out.println("x.a()");
					break;
				}
			}
		};
		Thread thread1 = new Thread(runnable);
		Thread thread2 = new Thread(runnable);
		thread1.start();
		thread2.start();
	}
}

// other ex
/**
 * Which are true? (Choose all that apply.)
 * 
 * <pre>
 * 
 * 	   A. Compilation fails
 * 	   B. The output could be 4 4 2 3
 * 	   C. The output could be 4 4 2 2
 * 	   D. The output could be 4 4 4 2
 * 	   E. The output could be 2 2 4 4
 * 	   F. An exception is thrown at runtime
 * 
 * 
 * Answer:
 *      ✓    F is correct. When run() is invoked, it is with a new instance of ChicksYack and c has 
 * not been assigned to an object. If c were static, then because yack is synchronized, answers 
 * C and E would have been correct.
 *        A, B, C, D, and E are incorrect based on the above. (Objective 4.3)
 * </pre>
 * 
 */
class Chicks {
	synchronized void yack(long id) {
		for (int x = 1; x < 3; x++) {
			System.out.print(id + " ");
			Thread.yield();
		}
	}
}

class ChicksYack implements Runnable {
	Chicks c;

	public static void main(String[] args) {
		new ChicksYack().go();
	}

	void go() {
		c = new Chicks();
		new Thread(new ChicksYack()).start();
		new Thread(new ChicksYack()).start();
	}

	public void run() {
		///c = new Chicks(); //makes it correct
		c.yack(Thread.currentThread().getId());
	}
}

// /example

class Messager implements Runnable {
	public static void main(String[] args) {
		new Thread(new Messager("Wallace")).start();
		new Thread(new Messager("Gromit")).start();
	}

	private String name;

	public Messager(String name) {
		this.name = name;
	}

	public void run() {
		message(1);
		message(2);
	}

	private synchronized void message(int n) {
		System.out.print(name + "-" + n + " ");
	}
}

