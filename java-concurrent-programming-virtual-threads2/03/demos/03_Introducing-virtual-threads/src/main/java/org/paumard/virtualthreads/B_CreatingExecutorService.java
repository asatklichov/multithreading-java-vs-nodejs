package org.paumard.virtualthreads;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

public class B_CreatingExecutorService {

    public static void main(String[] args) {

        var set = ConcurrentHashMap.<String>newKeySet();
        Runnable task = () -> set.add(Thread.currentThread().toString());

        int N_TASKS = 2000;

        try (var es1 = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int index = 0; index < N_TASKS; index++) {
                es1.submit(task);
            }
        }

        System.out.println("# threads used = " + set.size());
    }
}
