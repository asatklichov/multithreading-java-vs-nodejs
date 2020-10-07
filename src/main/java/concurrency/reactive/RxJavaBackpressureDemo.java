package concurrency.reactive;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.schedulers.Schedulers;

public class RxJavaBackpressureDemo {

	public static void main(String[] args) throws InterruptedException {
		Flowable<Integer> feed = Flowable.<Integer>create(emitter -> emit(emitter), BackpressureStrategy.BUFFER)
				.observeOn(Schedulers.computation(), true, 2);

		feed.subscribe(d -> process(d));

		Thread.sleep(1000);
	}

	private static void emit(FlowableEmitter<Integer> emitter) throws InterruptedException {
		int count = 0;
		while (count++ < 10) {
			System.out.println("Emitting " + count);
			emitter.onNext(count);
		}

		Thread.sleep(1000);
	}

	public static void process(int data) throws InterruptedException {
		System.out.println(data);
		Thread.sleep(1000);
	}
}