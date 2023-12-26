package concurrency.java.core.api;

public class TestOverriddenRun {

}

class MyThread3 extends Thread {
	MyThread3() {
		System.out.print(" MyThread");
	}

	public void run() {
		System.out.println(" bar");
	}

	public void run(String s) {
		System.out.print(" baz");
	}
}

class TestThreads3 {
	public static void main(String[] args) throws InterruptedException {

		//MyThread3 t = new MyThread3();
		Thread t = new MyThread3();
		t.start();
		System.out.println(); 
		
		Thread.sleep(1000); 

		t = new MyThread3() {
			public void run() {
				System.out.println(); 
				System.out.print(" foo");
			}
		};
		t.start();
	}
}
