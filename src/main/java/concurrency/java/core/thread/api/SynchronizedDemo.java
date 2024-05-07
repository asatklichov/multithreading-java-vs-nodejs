package concurrency.java.core.thread.api;
 

class AccountDanger implements Runnable {
	private Account acct = new Account();

	public static void main(String[] args) {
		/**
		 * So what happened?
		 * 
		 * Is it possible that, say, Lucy checked the balance, fell asleep, Fred checked
		 * the balance, Lucy woke up and completed her withdrawal, then Fred completes
		 * his withdrawal, and in the end they overdraw the account? Look at the
		 * (numbered) output: ...
		 * 
		 * 
		 * Preventing the Account Overdraw So what can be done?
		 * 
		 * 
		 * The solution is actually quite simple. see: AccountSafe
		 * 
		 * 
		 * 
		 * We must guarantee that the two steps of the withdrawal— checking the balance
		 * and making the withdrawal—are never split apart. We need them to always be
		 * performed as one operation, even when the thread falls asleep in between step
		 * 1 and step 2!
		 * 
		 * We call this an "atomic operation" (although the physics is a little
		 * outdated, in this case "atomic" means "indivisible") because the operation,
		 * regardless of the number of actual statements (or underlying byte code
		 * instructions), is completed before any other thread code that acts on the
		 * same data.
		 */
		AccountDanger r = new AccountDanger();
		Thread one = new Thread(r);
		Thread two = new Thread(r);
		one.setName("Fred");
		two.setName("Lucy");
		one.start();
		two.start();
	}

	public void run() {
		for (int x = 0; x < 5; x++) {
			makeWithdrawal(10);
			if (acct.getBalance() < 0) {
				System.out.println("account is overdrawn!");
			}
		}
	}

	private void makeWithdrawal(int amt) {
		if (acct.getBalance() >= amt) {
			System.out.println(Thread.currentThread().getName() + " is going to withdraw");
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
			acct.withdraw(amt);
			System.out.println(Thread.currentThread().getName() + " completes the withdrawal");
		} else {
			System.out.println("Not enough in account for " + Thread.currentThread().getName() + " to withdraw "
					+ acct.getBalance());
		}
	}
}

class Account {
	private int balance = 50;

	public int getBalance() {
		return balance;
	}

	public void withdraw(int amount) {
		balance = balance - amount;
	}
}

/**
 * 
 * Preventing the Account Overdraw So what can be done?
 * 
 * The solution is actually quite simple.
 * 
 * We must guarantee that the two steps of the withdrawal— checking the balance
 * and making the withdrawal—are never split apart. We need them to always be
 * performed as one operation, even when the thread falls asleep in between step
 * 1 and step 2! We call this an "atomic operation" (although the physics is a
 * little outdated, in this case "atomic" means "indivisible") because the
 * operation, regardless of the number of actual statements (or underlying byte
 * code instructions), is completed before any other thread code that acts on
 * the same data.
 * 
 * You can't guarantee that a single thread will stay running throughout the
 * entire atomic operation. But you can guarantee that even if the thread
 * running the atomic operation moves in and out of the running state, no other
 * running thread will be able to act on the same data. In other words, If Lucy
 * falls asleep after checking the balance, we can stop Fred from checking the
 * balance until after Lucy wakes up and completes her withdrawal.
 * 
 * So how do you protect the data? You must do two things:
 * 
 * ■ Mark the variables private.
 * 
 * ■ Synchronize the code that modifies the variables.
 * 
 * 
 * 
 * 
 */
class AccountSafe implements Runnable {
	private Account2 acct = new Account2();

	public static void main(String[] args) {
		AccountDanger r = new AccountDanger();
		Thread one = new Thread(r);
		Thread two = new Thread(r);
		one.setName("Fred");
		two.setName("Lucy");
		one.start();
		two.start();
	}

	public void run() {
		for (int x = 0; x < 5; x++) {
			makeWithdrawal(10);
			if (acct.getBalance() < 0) {
				System.out.println("account is overdrawn!");
			}
		}
	}

	private synchronized void makeWithdrawal(int amt) {
		if (acct.getBalance() >= amt) {
			System.out.println(Thread.currentThread().getName() + " is going to withdraw");
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
			}
			acct.withdraw(amt);
			System.out.println(Thread.currentThread().getName() + " completes the withdrawal");
		} else {
			System.out.println("Not enough in account for " + Thread.currentThread().getName() + " to withdraw "
					+ acct.getBalance());
		}
	}
}

class Account2 {
	private int balance = 50;

	public int getBalance() {
		return balance;
	}

	public void withdraw(int amount) {
		balance = balance - amount;
	}
}

//Creating a thread and Putting it to sleep
class TheCount extends Thread {
	public void run() {
		for (int i = 1; i <= 100; ++i) {
			System.out.print(i + "  ");
			if (i % 10 == 0)
				System.out.println("Hahaha");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	public static void main(String[] args) {
		new TheCount().start();
	}
}

//synchronizing a block of Code
class InSync extends Thread {
	StringBuffer letter;

	public InSync(StringBuffer letter) {
		this.letter = letter;
	}

	public void run() {
		synchronized (letter) { // #1
			for (int i = 1; i <= 100; ++i)
				System.out.print(letter);
			System.out.println();
			char temp = letter.charAt(0);
			++temp; // Increment the letter in StringBuffer:
			letter.setCharAt(0, temp);
		} // #2
	}

	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer("A");
		new InSync(sb).start();
		new InSync(sb).start();
		new InSync(sb).start();
	}
}



public class SynchronizedDemo {

	// give example with this monitor object
	// give for .class level

	// give example with different monitor object
	// like: multiple monitor objects: private Object monitor1; monitor2,... then
	// FOLLOW RIGHT ORDER to solve DEADLOCK issue, ... ...
	// shared monitor object ...

	// shared monitor object demo
	public static void main(String[] args) {
		Object monitor1 = new Object();

		SharedMonitor obj1 = new SharedMonitor(monitor1);
		SharedMonitor obj2 = new SharedMonitor(monitor1);
		// synchronized on same monitor obj. once used on multiple threads, block each
		// other
		obj1.increment();
		obj2.increment();

		Object monitor2 = new Object();
		SharedMonitor obj3 = new SharedMonitor(monitor2);
		// not blocks others threads
		obj3.increment();
	}

}

class SharedMonitor {

	private Object monitor;
	private int count;

	public SharedMonitor(Object monitor) {
		if (monitor == null) {
			throw new IllegalArgumentException("Monitor object can not be null");
		}
		this.monitor = monitor;
	}

	public void increment() {
		synchronized (this.monitor) {
			this.count++;
			System.out.println(count);
		}
	}
}

/**
 * Reentrance  Java synchronized blocks are reentrant A thread already holds,
 * is allowed to enter synchronized block which is also synchronized
 *
 */
class Reentrant {
	private int count;

	public synchronized void incrment() {
		this.count++;
	}

	public synchronized int incrmentAndGet() {
		// so here no blocked - reentrant
		incrment();
		return this.count;
	}

}

class HappensBeforeGuarantee {
	private long count = 0;
	private Object obj = null;

	public void set1(Object o) {
		//both read/write from main memory here, it is guaranteed 
		synchronized (this) {
			this.count++;
			this.obj = o;
		}
	}

	/**
	 * JVM decides to re-order, what happens
	 */
	public void set2(Object o) {
		//so here no longer guarantee, to read/write from main-memory this count
		this.count++;
		synchronized (this) {
			this.obj = o;
		}
		//but here it guarantees, however, but still not 100%
	}

	public void set3(Object o) {
		synchronized (this) {
			this.obj = o;
		}
		
		//so here no longer guarantee, to read/write from main-memory this count
		//problem is incremented count may not be written to count 
		this.count++;
	}

	public Object get() {
		synchronized (this) {
			return this.obj;
		}
	}
}
