package concurrency.java.thread.core.api;

/**
 * What is the result of this code?
 * 
 * <pre>
 * 
 *  A. It prints X and exits
 * B. It prints X and never exits
 * C. It prints XY and exits almost immeditately
 * D. It prints XY with a 10-second delay between X and Y
 * E. It prints XY with a 10000-second delay between X and Y
 * F. The code does not compile
 * G. An exception is thrown at runtime
 * 
 * 
 * 
 * Answer:
 *   ✓    G is correct. The code does not acquire a lock on t before calling t.wait(), so it throws an 
 * IllegalMonitorStateException. The method is synchronized, but it's not synchronized 
 * on t so the exception will be thrown. If the wait were placed inside a synchronized(t) 
 * block, then the answer would have been D.
 *     A, B, C, D, E, and F are incorrect based the logic described above. (Objective 4.2)
 * </pre>
 * 
 */
public class Test_ThreadExamples {

    public static synchronized void main(String[] args) throws InterruptedException {
        Thread t = new Thread();
        t.start(); // after this thread becomes DEAD
        System.out.print("X");
        t.wait(10000);
        System.out.print("Y");
    }

}

class MyThreadz1 extends Thread {
    public static void main(String[] args) {
        MyThreadz1 t = new MyThreadz1();
        t.start();
        System.out.print("one. ");
        t.start();
        System.out.print("two. ");
    }

    public void run() {
        System.out.print("Thread ");
    }
}

class TestCallStart2More {
    public static synchronized void main(String[] args) throws InterruptedException {
        Thread t = new Thread();
        t.start();
        System.out.print("X *****    \n");
        t.wait(10000);
        System.out.print("Y");
    }
}

/**
 * Which are true? (Choose all that apply.)
 * 
 * <pre>
 * 
 *   A. Compilation fails
 *   B. The output could be r1 r2 m1 m2
 *   C. The output could be m1 m2 r1 r2
 *   D. The output could be m1 r1 r2 m2
 *   E. The output could be m1 r1 m2 r2
 *   F. An exception is thrown at runtime
 *   
 *   
 *   Answer:
 *      ✓    A is correct. The join() must be placed in a try/catch block. If it were, answers B and 
 * D would be correct. The join() causes the main thread to pause and join the end of the 
 * other thread, meaning "m2" must come last.
 *        B, C, D, E, and F are incorrect based on the above. (Objective 4.2)
 * </pre>
 * 
 */
class Leader implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Leader());
        t.start();
        System.out.print("m1 ");

        /**
         * The join( ) Method
         * 
         * <pre>
         * 
         * The non-static join() method of class Thread lets one thread "join onto the end"  of another thread. 
         * 
         * If you have a thread B that can't do its work until another thread 
         * A has completed its work, then you want thread B to "join" thread A. 
         * 
         * This means that  thread B will not become runnable until A has finished (and entered the dead state).
         * 
         * Thread t = new Thread();
         * t.start();
         * t.join();
         * 
         * 
         * The preceding code takes the currently running thread (if this were in the 
         * main() method, then that would be the main thread) and joins it to the end of the 
         * thread referenced by t. This blocks the current thread from becoming runnable 
         * until after the thread referenced by t is no longer alive. In other words, the 
         * code 
         * 
         * t.join() means "Join me (the current thread) to the end of t, so that t 
         * must finish before I (the current thread) can run again." 
         * 
         * You can also call one 
         * of the overloaded versions of join() that takes a timeout duration, so that 
         * you're saying, "wait until thread t is done, but if it takes longer than 5,000 
         * milliseconds, then stop waiting and become runnable anyway." Figure 9-3 shows 
         * the effect of the join() method.
         * 
         * 
         * So far we've looked at three ways a running thread could leave the running state:
         * 
         * ■  A call to sleep()  Guaranteed to cause the current thread to stop execut-
         * ing for at least the specified sleep duration (although it might be interrupted 
         * before its specified time).
         * ■  A call to yield()  Not guaranteed to do much of anything, although 
         * typically it will cause the currently running thread to move back to runnable 
         * so that a thread of the same priority can have a chance.
         * ■  A call to join()  Guaranteed to cause the current thread to stop execut-
         * ing until the thread it joins with (in other words, the thread it calls join()
         * 
         * 
         * 
         * Besides those three, we also have the following scenarios in which a thread might 
         * leave the running state:
         * 
         * ■   The thread's run() method completes. Duh.
         * ■   A call to wait() on an object (we don't call wait() on a thread, as we'll 
         *   see in a moment).
         * ■   A thread can't acquire the lock on the object whose method code it's 
         *   attempting to run.
         * ■   The thread scheduler can decide to move the current thread from running  
         *   to runnable in order to give another thread a chance to run. No reason is 
         *   needed—the thread scheduler can trade threads in and out whenever it likes.
         * 
         * </pre>
         * 
         */

        // finish this t-thread, then continue with MAIN-thread
        t.join(); /// must handle or declare: InterruptedException
        System.out.print("m2 ");
    }

    public void run() {
        System.out.print("r1 ");
        System.out.print("r2 ");
    }
}

// / other
/**
 * 
 * What is the result of this code?
 * 
 * <pre>
 *   A. Compilation fails
 *   B. 1..2..3..
 *   C. 0..1..2..3..
 *   D. 0..1..2..
 *   E. An exception occurs at runtime
 * </pre>
 * 
 */
class MyThreadZ extends Thread {
    public static void main(String[] args) {
        MyThreadZ t = new MyThreadZ();
        Thread x = new Thread(t);
        x.start();

        // x.start();
    }

    public void run() {
        for (int i = 0; i < 3; ++i) {
            System.out.print(i + "..");
        }
    }
}

// // another example
/**
 * * The static method Thread.currentThread() returns a reference to the
 * currently executing Thread object. What is the result of this code?
 * 
 * <pre>
 * 
 *   A. Each String in the array lines will output, with a 1-second pause between lines
 *   
 *   B.  Each String in the array lines will output, with no pause in between because this method 
 * is not executed in a Thread
 * 
 *   C.  Each String in the array lines will output, and there is no guarantee there will be a pause 
 * because currentThread() may not retrieve this thread
 * 
 *   D. This code will not compile
 *   
 *   E. Each String in the lines array will print, with at least a one-second pause between lines
 * </pre>
 * 
 */
class Testc {
    public static void main(String[] args) {
        printAll(args);
    }

    public static void printAll(String[] lines) {
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
            // Thread.currentThread().sleep(1000); // declare or handle
            // InterruptedException
        }
    }
}

/**
 * Which letters will eventually appear somewhere in the output? (Choose all
 * that apply.)
 * 
 * <pre>
 * 
 *   A.  A
 *   B.  B
 *   C.  C
 *   D.  D
 *   E.  E
 *   F.  F
 *   G. The answer cannot be reliably determined
 *   H. The code does not compile
 *   
 *   
 *   Answer:
 *     ✓    A, C, D, E, and F are correct. This may look like laurel and hardy are battling to cause 
 * the other to sleep() or wait()—but that's not the case. Since sleep() is a static 
 * method, it affects the current thread, which is laurel (even though the method is invoked 
 * using a reference to hardy).  That's misleading but perfectly legal, and the Thread laurel 
 * is able to sleep with no exception, printing A and C (after at least a 1-second delay). Mean-
 * while hardy tries to call laurel.wait()—but hardy has not synchronized on laurel, 
 * so calling laurel.wait() immediately causes an IllegalMonitorStateException, and 
 * so hardy prints D, E, and F.  Although the order of the output is somewhat indeterminate 
 * (we have no way of knowing whether A is printed before D, for example) it is guaranteed 
 * that A, C, D, E, and F will all be printed in some order, eventually—so G is incorrect.
 *       B, G, and H are incorrect based on the above.  (Objective 4.4)
 * </pre>
 * 
 */
class TwoThreads {
    static Thread laurel, hardy;

    public static void main(String[] args) {
        laurel = new Thread() {
            public void run() {
                System.out.println("A");
                try {
                    // does not release its lock
                    hardy.sleep(1000);
                } catch (Exception e) {
                    System.out.println("B");
                }
                System.out.println("C");
            }
        };
        hardy = new Thread() {
            public void run() {
                System.out.println("D");
                try {
                    // release its lock
                    /**
                     * hardy tries to call laurel.wait() but hardy has not synchronized on laurel,
                     * so calling laurel.wait() immediately causes an IllegalMonitorStateException
                     */
                    laurel.wait();

                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("E");
                }
                System.out.println("F");
            }
        };
        laurel.start();
        hardy.start();
    }
}

// /example
class Letters extends Thread {
    private String name;

    public Letters(String name) {
        this.name = name;
    }

    public void write() {
        System.out.print(name);
        System.out.print(name);
    }

    public static void main(String[] args) {
        new Letters("X").start();
        new Letters("Y").start();
    }

    public void run2() {
        synchronized (Letters.class) {
            write();
        }
    }

    public void run() {
        synchronized (System.out) {
            write();
        }
    }

    public void run3() {
        synchronized (this) {
            write();
        }
    }

    /*
     * We want to guarantee that the output can be either XXYY or YYXX, but never
     * XYXY or any other combination. Which of the following method definitions
     * could be added to the Letters class to make this guarantee? (Choose all that
     * apply.)
     */

    // A. public void run() { write(); }
    // B. public synchronized void run() { write(); }
    // C. public static synchronized void run() { write(); }
    // D. public void run() { synchronized(this) { write(); } }
    // E. public void run() { synchronized(Letters.class) { write(); } }
    // F. public void run() { synchronized(System.out) { write(); } }
    // G. public void run() { synchronized(System.out.class) { write(); } }

    /*
     * answer: E and F are correct. B and D both synchronize on an instance of the
     * Letters class�but since there are two different instances in the main()
     * method, the two threads do not block each other and may run simultaneously,
     * resulting in output like XYXY.
     */
}

// /example
/**
 * Just because a thread�s sleep() expires, and it wakes up, does not mean it
 * will return to running! Remember, when a thread wakes up, it simply goes back
 * to the runnable state. So the time specified in sleep() is the minimum
 * duration in which the thread won�t run, but it is not the exact duration in
 * which the thread won�t run. So you can�t, for example, rely on the
 * sleep() method to give you a perfectly accurate timer. Although in many
 * applications using sleep() as a timer is certainly good enough, you must know
 * that a sleep() time is not a guarantee that the thread will start running
 * again as soon as the time expires and the thread wakes.
 * 
 */

class NameRunnable implements Runnable {
    public void run() {
        for (int x = 1; x < 4; x++) {
            System.out.println(x + " Run by " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }
}

class ManyNames {
    public static void main(String[] args) {
        // Make one Runnable
        NameRunnable nr = new NameRunnable();
        Thread one = new Thread(nr);
        one.setName("Fred");
        Thread two = new Thread(nr);
        two.setName("Lucy");
        Thread three = new Thread(nr);
        three.setName("Ricky");
        one.start();
        two.start();
        three.start();
    }
}

/// other
/**
 * 
 * When fragment I or fragment II is inserted at line 5, which are true? (Choose
 * all that apply.)
 * 
 * <pre>


  
   A. An exception is thrown at runtime
   B. With fragment I, compilation fails
   C. With fragment II, compilation fails
   D. With fragment I, the output could be yo dude dude yo
   E. With fragment I, the output could be dude dude yo yo
   F. With fragment II, the output could be yo dude dude yo
   
   
    And given these two fragments:
I.  synchronized void chat(long id) {
II. void chat(long id) {


   
Answer:
     ✓    F is correct. With fragment I, the chat method is synchronized, so the two threads can't 
swap back and forth. With either fragment, the first output must be yo.
       A, B, C, D, and E are incorrect based on the above. (Objective 4.3)
 * </pre>
 * 
 */
class Dudes { // line 3
    static long flag = 0;

    // insert code here
    void chat(long id) { // falgment II ///i put it
        if (flag == 0)
            flag = id;
        for (int x = 1; x < 3; x++) {
            if (flag == id)
                System.out.print("yo ");
            else
                System.out.print("dude ");
        }
    }
}

class DudesChat implements Runnable {
    static Dudes d;

    public static void main(String[] args) {
        new DudesChat().go();
    }

    void go() {
        d = new Dudes();
        new Thread(new DudesChat()).start();
        new Thread(new DudesChat()).start();
    }

    public void run() {
        d.chat(Thread.currentThread().getId());
    }
}
