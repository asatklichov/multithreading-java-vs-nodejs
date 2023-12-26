package concurrency.java.concurrent.api;

import java.util.concurrent.Exchanger;

//https://jenkov.com/tutorials/java-util-concurrent/exchanger.html
/**
 * see  {@link AnatomyOfASynchronizer}
 *
 */
public class ExchangerDemo {
	/**
	 * The java.util.concurrent.Exchanger class represents a kind of rendezvous
	 * point where two threads can exchange objects. Here is an illustration of this
	 * mechanism:
	 */
	public static void main(String[] args) {
		Exchanger exchanger = new Exchanger();

		ExchangerRunnable exchangerRunnable1 = new ExchangerRunnable(exchanger, "A");

		ExchangerRunnable exchangerRunnable2 = new ExchangerRunnable(exchanger, "B");

		new Thread(exchangerRunnable1).start();
		new Thread(exchangerRunnable2).start();
	}
}

class ExchangerRunnable implements Runnable {

	Exchanger exchanger = null;
	Object object = null;

	public ExchangerRunnable(Exchanger exchanger, Object object) {
		this.exchanger = exchanger;
		this.object = object;
	}

	public void run() {
		try {
			Object previous = this.object;

			this.object = this.exchanger.exchange(this.object);

			System.out.println(Thread.currentThread().getName() + " exchanged " + previous + " for " + this.object);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}