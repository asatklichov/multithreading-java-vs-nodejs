package concurrency.java.concurrent.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnExecutorServiceDemo {
	public static void main(String[] args) {

		ExecutorService es = Executors.newFixedThreadPool(3);
		BankAccount ba = new BankAccount(100);
		for (int i = 0; i < 5; i++) {
			Cashier work = new Cashier(ba, OperationType.DEPOSIT, 20);
			Cashier work2 = new Cashier(ba, OperationType.WITHDRAWAL, 5);
			es.submit(work);
			es.submit(work2);
		}
	}
}

class Cashier implements Runnable {

	private BankAccount account;
	private OperationType operationType;
	private int amount;

	public Cashier(BankAccount account) {
		this.account = account;
	}

	public Cashier(BankAccount account, OperationType operationType, int amount) {
		this.account = account;
		this.operationType = operationType;
		this.amount = amount;

	}

	@Override
	public void run() {
		//for (int i = 0; i < 10; i++) {
			synchronized (account) {
				System.out.print("startBalance: " + account.getBalance());
				if (operationType == OperationType.DEPOSIT) {
					account.putDeposit(amount);
				} else if (operationType == OperationType.WITHDRAWAL) {
					account.getMoney(amount);
				}
			}
			System.out.println(", finalBalance: " + account.getBalance());
		//}
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
