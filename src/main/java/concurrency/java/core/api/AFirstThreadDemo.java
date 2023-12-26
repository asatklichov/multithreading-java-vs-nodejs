package concurrency.java.core.api;

/**
 * Desc:
 * 
 * <pre>
 * 
 * 
 * So what exactly is a thread? In Java, "thread" means two different things:
 * 
 * ■  An instance of class java.lang.Thread
 * ■  A thread of execution
 * 
 * An instance of Thread is just…an object. Like any other object in Java, it has 
 * variables and methods, and lives and dies on the heap. 
 * 
 * But a thread of execution is   an individual process (a "lightweight" process) that has its own call stack. 
 * 
 * In Java,  there is one thread per call stack—or, to think of it in reverse, one call stack per 
 * thread. 
 * 
 * Even if you don't create any new threads in your program, threads are back 
 * there running.
 * 
 * You might find it confusing that we're talking about code running concurrently—
 * as if in parallel—given that there's only one CPU on most of the machines running 
 * Java. What gives? 
 * 
 * The JVM, which gets its turn at the CPU by whatever scheduling 
 * mechanism the underlying OS uses, operates like a mini-OS and schedules its own 
 * threads regardless of the underlying operating system. 
 * 
 * In some JVMs, the Java 
 * threads are actually mapped to native OS threads, but we won't discuss that here; 
 * native threads are not on the exam. Nor is it required to understand how threads 
 * behave in different JVM environments. In fact, the most important concept to 
 * understand from this entire chapter is this:
 * When it comes to threads, very little is guaranteed.
 * 
 * 
 * Don't make the mistake of designing your program to be dependent on a
 * particular implementation of the JVM.
 * 
 * As you'll learn a little later, different JVMs can run threads in profoundly
 * different ways. For example, one JVM might be sure that all threads get their
 * turn, with a fairly even amount of time allocated for each thread in a nice,
 * happy, round-robin fashion.
 * 
 * But in other JVMs, a thread might start running and then just hog the whole
 * show, never stepping out so others can have a turn. 
 * 
 * If you test your application on the "nice turn-taking" JVM, 
 * and you don't know what is and is
 * not guaranteed in Java, then you might be in for a big shock when you run it
 * under a JVM with a different thread scheduling mechanism.
 * 
 * 
 * </pre>
 * 
 * 
 * 
 */
public class AFirstThreadDemo {

	public static void main(String[] args) throws InterruptedException {

		Runnable runnable = () -> {
			System.out.println("I am running in " + Thread.currentThread().getName());
		};

		Thread t = new Thread(runnable);
		t.setName("My thread");

		t.start();
		t.run(); // This actually not starts a thread, just calls run() method in main()
		t.join();
		t.start(); // thread execution already terminated already,
					// java.lang.IllegalThreadStateException
	}
}

class FooRunnable implements Runnable {
	public void run() {
		for (int x = 1; x < 6; x++) {
			System.out.println("Runnable running" + Thread.currentThread().getName());
			try {
				Thread.sleep(5 * 1000);// 5 * 60 * 1000 Sleep for 5 minutes
			} catch (InterruptedException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}

class TestThreads {
	public static void main(String[] args) {
		FooRunnable r = new FooRunnable();
		Thread t = new Thread(r, " tapdym");
		// t.setName("Tapdym2");
		t.start();

		// Make one more Runnable
		FooRunnable nr = new FooRunnable();
		Thread one = new Thread(nr);
		Thread two = new Thread(nr);
		Thread three = new Thread(nr);
		one.setName(" Fred");
		two.setName(" Lucy");
		three.setName(" Ricky");
		one.start();
		two.start();
		three.start();
	}
}
