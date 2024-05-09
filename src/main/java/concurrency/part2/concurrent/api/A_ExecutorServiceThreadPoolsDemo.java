package concurrency.part2.concurrent.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class _ExecutorServiceDemo {
	public static void main(String[] args) {

		ExecutorService es = Executors.newFixedThreadPool(3);
		BankAccount ba = new BankAccount(100);
		for (int i = 0; i < 5; i++) {
			Cashier work = new Cashier(ba, OperationType.DEPOSIT, 20, "Deposit Thread");
			Cashier work2 = new Cashier(ba, OperationType.WITHDRAWAL, 5, "Withdrawal Thread");
			es.submit(work);
			es.submit(work2);
		}
		System.out.println(Thread.currentThread().getName());
	}
}

//references
//http://tutorials.jenkov.com/java-util-concurrent/executorservice.html
//http://tutorials.jenkov.com/java-util-concurrent/threadpoolexecutor.html

public class A_ExecutorServiceThreadPoolsDemo {
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
		/**
		 * The Java ExecutorService execute(Runnable) method takes a java.lang.Runnable
		 * object, and executes it asynchronously.
		 */

		// Executor executorService = Executors.newSingleThreadExecutor();
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Runnable task = () -> System.out
				.println("Asynchronous task done by newSingleThreadExecutor" + Thread.currentThread().getName());
		for (int i = 0; i < 7; i++) {
			executorService.execute(task);
		}
		executorService.shutdown();

		System.out.println();
		// using 10 threads, to execute 7 same
		task = () -> System.out
				.println("Asynchronous task done by newFixedThreadPool " + Thread.currentThread().getName());

		executorService = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 7; i++) {
			executorService.execute(task);
		}
		System.out.println();
		executorService.execute(new Runnable() {
			public void run() {
				System.out.println("Asynchronous task done by newFixedThreadPool ");
			}
		});

		// or using Anonymous class
		executorService.execute(new Runnable() {
			public void run() {
				System.out.println("Asynchronous task done by newFixedThreadPool");
			}
		});

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

		// by lambda
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

		System.out.println();
		System.out.println("Java Executor newWorkStealingPool() Method");
		ExecutorService excr = Executors.newWorkStealingPool();
		excr.submit(new MyThreadImpl());
		excr.submit(new MyThreadImpl());
		excr.shutdown();
	}

	static class MyThreadImpl implements Runnable {

		public void run() {
			try {
				Long num = (long) (Math.random() / 30);
				System.out.println("Before Name: " + Thread.currentThread().getName());
				TimeUnit.SECONDS.sleep(num);
				System.out.println("After Name: " + Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

/**
 * A classical thread pool has one queue, and each thread-pool-thread locks the
 * queue, dequeue a task and then unlocks the queue.   If the tasks are short
 * and there are many of them, there is a lot of contention on the queue. Using
 * a lock-free queue really helps here, but doesn't solve the problem entirely.
 * 
 * 
 * Modern thread pools use work stealing - each thread has its own queue. When a
 * thread pool thread produces a task - it enqueues it to his own queue. When a
 * thread pool thread wants to dequeue a task - it first tries to dequeue a task
 * out of his own queue and if it doesn't have any - it "steals" work from other
 * thread queues. This really decreases the contention of the threadpool and
 * improves performance. newWorkStealingPool creates a workstealing-utilizing
 * thread pool with the number of threads as the number of processors.
 * newWorkStealingPool presents a new problem. If I have four logical cores,
 * then the pool will have four threads total. If my tasks block - for example
 * on synchronous IO - I don't utilize my CPUs enough. What I want is
 * four active threads at any given moment, for example - four threads which
 * encrypt AES and another 140 threads which wait for the IO to finish. This is
 * what ForkJoinPool provides - if your task spawns new tasks and that task
 * waits for them to finish - the pool will inject new active threads in order
 * to saturate the CPU. It is worth mentioning that ForkJoinPool utilizes work
 * stealing too. Which one to use? If you work with the fork-join model or you
 * know your tasks block indefinitely, use the ForkJoinPool. If your tasks are
 * short and are mostly CPU-bound, use newWorkStealingPool. And after anything
 * has being said, modern applications tend to use thread pool with the number
 * of processors available and utilize asynchronous
 * IO and lock-free-containers in order to prevent blocking. this (usually)
 * gives the best performance.
 * 
 * 
 * 
 * Which one to use? If you work with the fork-join model or you know your tasks
 * block indefinitely, use the ForkJoinPool. If your tasks are short and are
 * mostly CPU-bound, use newWorkStealingPool.
 *
 */
class NewWorkStealingPool {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newWorkStealingPool();

		List<Callable<String>> callables = Arrays.asList(() -> "task1", () -> "task2", () -> "task3");

		executor.invokeAll(callables).stream().map(future -> {
			try {
				return future.get();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}).forEach(System.out::println);
	}

}

//https://www.baeldung.com/java-threadlocal
/**
 * beforeExecute() and afterExecute() methods. The thread pool will call the
 * beforeExecute() method before running anything using the borrowed thread. On
 * the other hand, it’ll call the afterExecute() method after executing our
 * logic.
 * 
 */
class ThreadLocalAwareThreadPool extends ThreadPoolExecutor {

	public ThreadLocalAwareThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		// Call remove on each ThreadLocal
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
	}
}

class Cashier implements Runnable {

	private BankAccount account;
	private OperationType operationType;
	private int amount;
	private String threadCustomName;

	public Cashier(BankAccount account) {
		this.account = account;
	}

	public Cashier(BankAccount account, OperationType operationType, int amount, String threadCustomName) {
		this.account = account;
		this.operationType = operationType;
		this.amount = amount;
		this.threadCustomName = threadCustomName;

	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName()); // to print executor thread name
		// for (int i = 0; i < 10; i++) {
		synchronized (account) {
			System.out.print("startBalance: " + account.getBalance());
			if (operationType == OperationType.DEPOSIT) {
				account.putDeposit(amount);
			} else if (operationType == OperationType.WITHDRAWAL) {
				account.getMoney(amount);
			}
		}
		System.out.println(", finalBalance: " + account.getBalance() + ", " + threadCustomName);
		// }
		System.out.println();
	}
}

class BankAccount {
	private int balance;

	public BankAccount() {
	}

	public BankAccount(int balance) {
		this.balance = balance;
	}

	public synchronized int getBalance() {
		return balance;
	}

	public synchronized void putDeposit(int amount) {
		// += non atomic operation, read/write issue
		this.balance += amount;
	}

	public synchronized void getMoney(int amount) {
		// += non atomic operation, read/write issue
		this.balance -= amount;
	}
}

enum OperationType {
	DEPOSIT, WITHDRAWAL;
}

/**
 * Thread Pools
 * 
 * <pre>
 * https://jenkov.com/tutorials/java-concurrency/thread-pools.html
 * 
 *Thread Pools are useful when you need to limit the number of threads running
 * in your application at the same time. There is a performance overhead
 * associated with starting a new thread, and each thread is also allocated some
 * memory for its stack etc.
 * 
 * Instead of starting a new thread for every task to execute concurrently, the
 * task can be passed to a thread pool. As soon as the pool has any idle threads
 * the task is assigned to one of them and executed. Internally the tasks are
 * inserted into a Blocking Queue which the threads in the pool are dequeuing
 * from. When a new task is inserted into the queue one of the idle threads will
 * dequeue it successfully and execute it. The rest of the idle threads in the
 * pool will be blocked waiting to dequeue tasks.
 * 
 * Thread pools are often used in multi threaded servers. Each connection
 * arriving at the server via the network is wrapped as a task and passed on to
 * a thread pool. The threads in the thread pool will process the requests on
 * the connections concurrently. A later trail will get into detail about
 * implementing multithreaded servers in Java.
 * 
 * Java 5 comes with built in thread pools in the java.util.concurrent package,
 * so you don't have to implement your own thread pool. You can read more about
 * it in my text on the java.util.concurrent.ExecutorService. Still it can be
 * useful to know a bit about the implementation of a thread pool anyways.
 * 
 * Here is a simple thread pool implementation. Please note that this
 * implementation uses my own BlockingQueue class as explained in my Blocking
 * Queues tutorial. In a real life implementation you would probably use one of
 * Java's built-in blocking queues instead.
 * 
 * 
 * https://jenkov.com/tutorials/java-util-concurrent/threadpoolexecutor.html
 * </pre>
 */
class JavaThreadPool {

	public static void main(String[] args) {

	}
}

class ThreadPool {

	private BlockingQueue taskQueue = null;
	private List<PoolThread> threads = new ArrayList<PoolThread>();
	private boolean isStopped = false;

	public ThreadPool(int noOfThreads, int maxNoOfTasks) {
		taskQueue = new ArrayBlockingQueue<>(maxNoOfTasks);

		for (int i = 0; i < noOfThreads; i++) {
			threads.add(new PoolThread(taskQueue));
		}
		for (PoolThread thread : threads) {
			thread.start();
		}
	}

	public synchronized void execute(Runnable task) throws Exception {
		if (this.isStopped)
			throw new IllegalStateException("ThreadPool is stopped");

		this.taskQueue.take(); // enqueue(task);
	}

	public synchronized void stop() {
		this.isStopped = true;
		for (PoolThread thread : threads) {
			thread.doStop();
		}
	}

}

class PoolThread extends Thread {

	private BlockingQueue taskQueue = null;
	private boolean isStopped = false;

	public PoolThread(BlockingQueue queue) {
		taskQueue = queue;
	}

	public void run() {
		while (!isStopped()) {
			try {
				Runnable runnable = (Runnable) taskQueue.take(); // dequeue();
				runnable.run();
			} catch (Exception e) {
				// log or otherwise report exception,
				// but keep pool thread alive.
			}
		}
	}

	public synchronized void doStop() {
		isStopped = true;
		this.interrupt(); // break pool thread out of dequeue() call.
	}

	public synchronized boolean isStopped() {
		return isStopped;
	}
}