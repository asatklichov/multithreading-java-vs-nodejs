package concurrency.java.core.api;

public class DeadThreadCallDemo {

	public static void main(String[] args) throws InterruptedException {

		Runnable runnable = () -> {
			System.out.println("I am running in " + Thread.currentThread().getName());
		};

		Thread t = new Thread(runnable);
		t.setName("My thread");

		t.start();
		t.run(); // This actually not starts a thread, just calls run() method in main()
		t.join();		
		t.start(); // thread execution already terminated already,
					// java.lang.IllegalThreadStateException
	}
}