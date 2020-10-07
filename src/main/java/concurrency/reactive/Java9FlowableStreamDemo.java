package concurrency.reactive;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;

public class Java9FlowableStreamDemo {

	public static void main(String[] args) throws InterruptedException {
		// emitter-publisher
		Flowable.interval(1, 1, TimeUnit.MILLISECONDS).filter(v -> v > 10) // processor-left, subscribtion - right
				// Processor = subscriber + Subscription
				// Backpressure -
				.map(e -> transform(e))
				// .map(e -> e * 2)
				// .subscribe(System.out::println);
				.subscribe(System.out::println, err -> System.err.println("Err: " + err),
						() -> System.out.println("completed"));
		Thread.sleep(1000);
	}

	public static long transform(long l) {
		if (l == 5)
			throw new IllegalArgumentException("non acceptable  value");
		return 2 * l;
	}
}
