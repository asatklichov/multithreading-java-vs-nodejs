package concurrency.java.concurrent.api;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        System.out.println("Count: " + latch.getCount());

        Waiter waiter = new Waiter(latch);
        Decrementer decrementer = new Decrementer(latch);

        Thread wt = new Thread(waiter);
        wt.start();
        new Thread(decrementer).start();

        Thread.sleep(4000);

        wt.join();
        
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t = new Thread(() -> {
            countDownLatch.countDown();
            countDownLatch.countDown();
        });
        t.start();
        countDownLatch.await();

        System.out.println("Count: " + countDownLatch.getCount());
    }

}

class Waiter implements Runnable {

    CountDownLatch latch = null;

    public Waiter(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Waiter Released");
    }
}

class Decrementer implements Runnable {

    CountDownLatch latch = null;

    public Decrementer(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {

        try {
            System.out.println("wait .. ");
            Thread.sleep(1000);
            this.latch.countDown();

            System.out.println("wait a bit ..");
            Thread.sleep(2000);
            this.latch.countDown();

            System.out.println("wait a bit more ...");
            Thread.sleep(3000);
            this.latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}