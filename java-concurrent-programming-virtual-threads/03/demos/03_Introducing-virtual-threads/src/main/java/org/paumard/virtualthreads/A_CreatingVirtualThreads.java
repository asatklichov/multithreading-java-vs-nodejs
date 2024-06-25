package org.paumard.virtualthreads;

public class A_CreatingVirtualThreads {

    public static void main(String[] args) throws InterruptedException {

        Runnable task = () -> {
            System.out.println("I am running in the thread " +
                               Thread.currentThread().getName());
            System.out.println("I am running in daemon thread? " +
                               Thread.currentThread().isDaemon());
        };

        Thread thread1 = new Thread(task);
        thread1.start();
        thread1.join();

        Thread thread2 = Thread.ofPlatform()
              .daemon()
              .name("Platform thread 2")
              .unstarted(task);
        thread2.start();
        thread2.join();

        Thread thread3 = Thread.ofVirtual()
              .name("Virtual thread 3")
              .unstarted(task);
        thread3.start();
        thread3.join();

        Thread thread4 = Thread.startVirtualThread(task);
        thread4.join();

    }
}










