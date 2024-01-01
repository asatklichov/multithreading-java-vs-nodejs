package concurrency.java.concurrent.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//http://tutorials.jenkov.com/java-util-concurrent/executorservice.html
//http://tutorials.jenkov.com/java-util-concurrent/threadpoolexecutor.html

public class AThreadPoolExecutorService {
    /**
     * The Java ExecutorService interface, java.util.concurrent.ExecutorService,
     * represents an asynchronous execution mechanism which is capable of executing
     * tasks concurrently in the background. In this Java ExecutorService tutorial I
     * will explain how to create a ExecutorService, how to submit tasks for
     * execution to it, how to see the results of those tasks, and how to shut down
     * the ExecutorService again when you need to.
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * 
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        System.out.println("-- execute() Runnable --");
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            public void run() {
                System.out.println("Asynchronous task done by newSingleThreadExecutor");
            }
        });
        executorService.shutdown();
        
        executorService = Executors.newSingleThreadExecutor();

        executorService.execute( 
                () ->   System.out.println("lambda Asynchronous task done by newSingleThreadExecutor"));
          
        executorService.shutdown();

        executorService = Executors.newFixedThreadPool(10);

        /**
         * The Java ExecutorService execute(Runnable) method takes a java.lang.Runnable
         * object, and executes it asynchronously.
         */
        executorService.execute(new Runnable() {
            public void run() {
                System.out.println("Asynchronous task done by newFixedThreadPool");
            }
        });

        System.out.println();
        System.out.println("\n-- submit() Runnable return null Future  --");
        /**
         * The Java ExecutorService submit(Runnable) method also takes a Runnable
         * implementation, but returns a Future object.
         */
        Future future = executorService.submit(new Runnable() {
            public void run() {
                System.out.println("Asynchronous Runnable task by submit and return  FUTURE ");
            }
        });
        System.out.println("future.get1() = " + future.get()); // returns null if the task has finished correctly.

        // by lamda
        Runnable r = () -> System.out.println("Asynchronous Runnable task by submit and return  FUTURE  in LAMBDA");
        future = executorService.submit(r);
        System.out.println("future.get2() = " + future.get()); // returns null if the task has finished correctly.

        System.out.println("\n-- submit() CALLABLE return Future value --");
        /**
         * Submit Callable The Java ExecutorService submit(Callable) method is similar
         * to the submit(Runnable) method except it takes a Java Callable instead of a
         * Runnable.
         */
        future = executorService.submit(new Callable() {
            public String call() throws Exception { // Object
                System.out.println("Asynchronous Callable by submit and return  FUTURE");
                return "Callable Result";
            }
        });
        System.out.println("future.get3() = " + future.get());

        Callable c = () -> "Asynchronous Callable by submit and return  FUTURE in LAMBDA";
        future = executorService.submit(c);
        System.out.println("future.get4() = " + future.get());

        System.out.println("\n-- invokeAny() CALLABLE return Result --");
        invokeAnyExample(executorService);

        executorService.shutdown();

        System.out.println("\n-- invokeAll() CALLABLE return List<Future<T> values --");
        executorService = Executors.newFixedThreadPool(4);
        invokeAllExample(executorService);
        invokeAllExample2(executorService);

        executorService.shutdown();

        System.out.println("\n   -- ScheduledExecutorService Example -- ScheduledExecutorService Example");
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

        ScheduledFuture scheduledFuture = scheduledExecutorService.schedule(new Callable() {
            public Object call() throws Exception {
                System.out.println("Executed!");
                return "Called!";
            }
        }, 5, TimeUnit.SECONDS);

        System.out.println("scheduledFuture future.get = " + scheduledFuture.get());
        scheduledExecutorService.shutdownNow();

        /**
         * The ForkJoinPool was added to Java in Java 7. The ForkJoinPool is similar to
         * the Java ExecutorService but with one difference. The ForkJoinPool makes it
         * easy for tasks to split their work up into smaller tasks which are then
         * submitted to the ForkJoinPool too. Tasks can keep splitting their work into
         * smaller subtasks for as long as it makes to split up the task. It may sound a
         * bit abstract, so in this fork and join tutorial I will explain how the
         * ForkJoinPool works, and how splitting tasks up work.
         */
        System.out.println("\n -- ForkJoinPool --");
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        /**
         * You submit tasks to a ForkJoinPool similarly to how you submit tasks to an
         * ExecutorService. You can submit two types of tasks. A task that does not
         * return any result (an "action"), and a task which does return a result (a
         * "task"). These two types of tasks are represented by the RecursiveAction and
         * RecursiveTask classes. How to use both of these tasks and how to submit them
         * will be covered in the following sections.
         */
        System.out.println("  -- RecursiveAction --");
        MyRecursiveAction myRecursiveAction = new MyRecursiveAction(24);
        forkJoinPool.invoke(myRecursiveAction);
        System.out.println(" -- RecursiveTask --");
        MyRecursiveTask myRecursiveTask = new MyRecursiveTask(128);

        long mergedResult = forkJoinPool.invoke(myRecursiveTask);

        System.out.println("mergedResult = " + mergedResult);

    }

    private static void invokeAnyExample(ExecutorService executorService)
            throws InterruptedException, ExecutionException {

        Set<Callable<String>> callables = new HashSet<Callable<String>>();

        callables.add(new Callable<String>() {
            public String call() throws Exception {
                return "Task 1";
            }
        });
        callables.add(new Callable<String>() {
            public String call() throws Exception {
                return "Task 2";
            }
        });
        callables.add(new Callable<String>() {
            public String call() throws Exception {
                return "Task 3";
            }
        });

        String result = executorService.invokeAny(callables);

        System.out.println("result = " + result);
    }

    private static void invokeAllExample(ExecutorService executorService) throws InterruptedException {
        Callable<String> command1 = () -> "Hello man via Lambda 1";
        Callable<String> command2 = () -> {
            return "Hello man via Lambda 2";
        };
        Collection taskList = Arrays.asList(new Callable[] { command1, command2 });
        List<Future> invokeAll = executorService.invokeAll(taskList);
        invokeAll.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private static void invokeAllExample2(ExecutorService executorService)
            throws InterruptedException, ExecutionException {
        Set<Callable<String>> callables = new HashSet<Callable<String>>();

        callables.add(new Callable<String>() {
            public String call() throws Exception {
                return "Task 1";
            }
        });
        callables.add(new Callable<String>() {
            public String call() throws Exception {
                return "Task 2";
            }
        });
        callables.add(new Callable<String>() {
            public String call() throws Exception {
                return "Task 3";
            }
        });

        List<Future<String>> futures = executorService.invokeAll(callables);

        for (Future<String> future : futures) {
            System.out.println("future.get = " + future.get());
        }
    }

}

class MyRecursiveAction extends RecursiveAction {

    private long workLoad = 0;

    public MyRecursiveAction(long workLoad) {
        this.workLoad = workLoad;
    }

    @Override
    protected void compute() {

        // if work is above threshold, break tasks up into smaller tasks
        if (this.workLoad > 16) {
            System.out.println("Splitting workLoad : " + this.workLoad);

            List<MyRecursiveAction> subtasks = new ArrayList<MyRecursiveAction>();

            subtasks.addAll(createSubtasks());

            for (RecursiveAction subtask : subtasks) {
                subtask.fork();
            }

        } else {
            System.out.println("Doing workLoad myself: " + this.workLoad);
        }
    }

    private List<MyRecursiveAction> createSubtasks() {
        List<MyRecursiveAction> subtasks = new ArrayList<MyRecursiveAction>();

        MyRecursiveAction subtask1 = new MyRecursiveAction(this.workLoad / 2);
        MyRecursiveAction subtask2 = new MyRecursiveAction(this.workLoad / 2);

        subtasks.add(subtask1);
        subtasks.add(subtask2);

        return subtasks;
    }

}

class MyRecursiveTask extends RecursiveTask<Long> {

    private long workLoad = 0;

    public MyRecursiveTask(long workLoad) {
        this.workLoad = workLoad;
    }

    protected Long compute() {

        // if work is above threshold, break tasks up into smaller tasks
        if (this.workLoad > 16) {
            System.out.println("Splitting workLoad : " + this.workLoad);

            List<MyRecursiveTask> subtasks = new ArrayList<MyRecursiveTask>();
            subtasks.addAll(createSubtasks());

            for (MyRecursiveTask subtask : subtasks) {
                subtask.fork();
            }

            long result = 0;
            for (MyRecursiveTask subtask : subtasks) {
                result += subtask.join();
            }
            return result;

        } else {
            System.out.println("Doing workLoad myself: " + this.workLoad);
            return workLoad * 3;
        }
    }

    private List<MyRecursiveTask> createSubtasks() {
        List<MyRecursiveTask> subtasks = new ArrayList<MyRecursiveTask>();

        MyRecursiveTask subtask1 = new MyRecursiveTask(this.workLoad / 2);
        MyRecursiveTask subtask2 = new MyRecursiveTask(this.workLoad / 2);

        subtasks.add(subtask1);
        subtasks.add(subtask2);

        return subtasks;
    }
}