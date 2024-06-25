package org.paumard.virtualthread;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class B1_BlockingVirtualThreads {

    public static void main(String[] args) throws InterruptedException {

        Runnable task1 =
                () -> {
                    System.out.println(Thread.currentThread());
                    sleepFor(10, ChronoUnit.MICROS);
                    System.out.println(Thread.currentThread());
                    sleepFor(10, ChronoUnit.MICROS);
                    System.out.println(Thread.currentThread());
                    sleepFor(10, ChronoUnit.MICROS);
                    System.out.println(Thread.currentThread());
                };
        Runnable task2 =
                () -> {
                    sleepFor(10, ChronoUnit.MICROS);
                    sleepFor(10, ChronoUnit.MICROS);
                    sleepFor(10, ChronoUnit.MICROS);
                };

        int N_THREADS = 10;
        var threads = new ArrayList<Thread>();
        for (int index = 0; index < N_THREADS; index++) {
            var thread =
            index == 0 ?
                    Thread.ofVirtual().unstarted(task1) :
                    Thread.ofVirtual().unstarted(task2);
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    private static void sleepFor(int amount, ChronoUnit unit) {
        try {
            Thread.sleep(Duration.of(amount, unit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
