package multithreading.in.practice;
 
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * if we look only at the output from Lucy, or Ricky. Each one individually is
 * behaving in a nice orderly manner. But together�chaos!
 *
 * <pre>
 *   Why? Because
*
*
it's up to the scheduler, and we don't control the scheduler! Which brings up
another key point to remember: Just because a series of threads are started in a
particular order doesn't mean they'll run in that order. For any group of started
threads, order is not guaranteed by the scheduler. And duration is not guaranteed.
You don't know, for example, if one thread will run to completion before the others
have a chance to get in or whether they'll all take turns nicely, or whether they'll do
a combination of both. 

There is a way, however, to start a thread but tell it not to
run until some other thread has finished.

You can do this with the join() method, which we'll look at a little later.
 * 
 * </pre>
 */
public class ThreadJoinDemo {

    /**
     * There is a way, however, to start a thread but tell it not to run until some
     * other thread has finished.
     * 
     * You can do this with the join() method, which we'll look at a little later.
     * 
     * @param args
     */

    @Test
    public void givenStartedThread_whenJoinCalled_waitsTillCompletion() throws InterruptedException {
        Thread t2 = new SampleThread(1);
        t2.start();
        System.out.println("Invoking join");
        t2.join(); //main(current) thread must wait until t2 finishes
        System.out.println("Returned from join");
        assertFalse(t2.isAlive());
    }

    /**
     * Thread.join() Methods with Timeout The join() method will keep waiting if the
     * referenced thread is blocked or is taking too long to process. This can
     * become an issue as the calling thread will become non-responsive. To handle
     * these situations, we use overloaded versions of the join() method that allow
     * us to specify a timeout period.
     * 
     * @throws InterruptedException
     */
    @Test
    public void givenStartedThread_whenTimedJoinCalled_waitsUntilTimedout() throws InterruptedException {
        Thread t3 = new SampleThread(5); //5 sec 
        t3.start();
        t3.join(1000);
        System.out.println("Returned from join, because non-responsive in 1000 sec");
        assertTrue(t3.isAlive());
    }
    
    @Test
    public void givenStartedThread_whenTimedJoinCalled_waitsUntilTimedout2() throws InterruptedException {
        Thread t3 = new SampleThread(1); //1 sec
        t3.start();
        t3.join(2000);
        System.out.println("Returned from join, because gave response in 2000 sec");
        assertFalse(t3.isAlive());
    }

}

class SampleThread extends Thread {
    public int processingCount = 0;

    SampleThread(int processingCount) {
        this.processingCount = processingCount;
        System.out.println("Thread Created");
    }

    @Override
    public void run() {
        System.out.println("Thread " + this.getName() + " started");
        while (processingCount > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread " + this.getName() + " interrupted");
            }
            processingCount--;
        }
        System.out.println("Thread " + this.getName() + " exiting");
    }
}