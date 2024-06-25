package org.paumard.virtualthread;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class C_BlockingVirtualThreads {

    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {

        var lock = new ReentrantLock();

        Runnable task1 =
                () -> {
                    System.out.println(Thread.currentThread());
                    lock.lock();
                    try {
                        counter++;
                        sleepFor(1, ChronoUnit.MICROS);
                    } finally {
                        lock.unlock();
                    }
                    System.out.println(Thread.currentThread());
                    lock.lock();
                    try {
                        counter++;
                        sleepFor(1, ChronoUnit.MICROS);
                    } finally {
                        lock.unlock();
                    }
                    System.out.println(Thread.currentThread());
                    lock.lock();
                    try {
                        counter++;
                        sleepFor(1, ChronoUnit.MICROS);
                    } finally {
                        lock.unlock();
                    }
                    System.out.println(Thread.currentThread());
                };
        Runnable task2 =
                () -> {
                    lock.lock();
                    try {
                        counter++;
                        sleepFor(1, ChronoUnit.MICROS);
                    } finally {
                        lock.unlock();
                    }
                    lock.lock();
                    try {
                        counter++;
                        sleepFor(1, ChronoUnit.MICROS);
                    } finally {
                        lock.unlock();
                    }
                    lock.lock();
                    try {
                        counter++;
                        sleepFor(1, ChronoUnit.MICROS);
                    } finally {
                        lock.unlock();
                    }
                };

        int N_THREADS = 2_000;

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

        System.out.println("# threads = " + N_THREADS);
        System.out.println("counter   = " + counter);
    }

    private static void sleepFor(int amount, ChronoUnit unit) {
        try {
            Thread.sleep(Duration.of(amount, unit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
