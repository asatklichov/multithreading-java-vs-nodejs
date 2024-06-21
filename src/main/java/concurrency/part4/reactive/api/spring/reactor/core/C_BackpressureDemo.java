package concurrency.part4.reactive.api.spring.reactor.core;

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;

/**
 * Backpressure The next thing we should consider is backpressure. In our
 * example, the subscriber is telling the producer to push every single element
 * at once. This could end up becoming overwhelming for the subscriber,
 * consuming all of its resources.
 * 
 * Backpressure is when a downstream can tell an upstream to send it less data
 * in order to prevent it from being overwhelmed.
 * 
 */
public class C_BackpressureDemo {

	public static void main(String[] args) {

		List<Integer> elements = new ArrayList<>();
		Flux.just(1, 2, 3, 4).log().subscribe(new Subscriber<Integer>() {
			private Subscription s;
			int onNextAmount;

			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				/**
				 * We can modify our Subscriber implementation to apply backpressure.  
				 * Tell upstream to only send two elements at a time by using request():
				 */
				s.request(2);
			}

			@Override
			public void onNext(Integer integer) {
				elements.add(integer);
				onNextAmount++;
				if (onNextAmount % 2 == 0) {
					s.request(2);
				}
			}

			@Override
			public void onError(Throwable t) {
			}

			@Override
			public void onComplete() {
			}
		});
	}

}
