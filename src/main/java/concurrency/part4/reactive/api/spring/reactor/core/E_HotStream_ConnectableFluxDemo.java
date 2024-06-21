package concurrency.part4.reactive.api.spring.reactor.core;

import java.time.Duration;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

/**
 * Hot Streams Currently, we’ve focused primarily on cold streams. These are
 * static, fixed-length streams that are easy to deal with. A more realistic use
 * case for reactive might be something that happens infinitely.
 * 
 * For example, we could have a stream of mouse movements that constantly needs
 * to be reacted to or a Twitter feed. These types of streams are called hot
 * streams, as they are always running and can be subscribed to at any point in
 * time, missing the start of the data.
 */
public class E_HotStream_ConnectableFluxDemo {
	public static void main(String[] args) {
		/**
		 * Creating a ConnectableFlux One way to create a hot stream is by converting a
		 * cold stream into one. Let’s create a Flux that lasts forever, outputting the
		 * results to the console, which would simulate an infinite stream of data
		 * coming from an external resource:
		 */

		ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
			while (true) {
				fluxSink.next(System.currentTimeMillis());
			}
		}).publish();

		/**
		 * By calling publish() we are given a ConnectableFlux. This means that calling
		 * subscribe() won’t cause it to start emitting, allowing us to add multiple
		 * subscriptions:
		 */
		publish.subscribe(System.out::println);
		publish.subscribe(System.out::print);
		/**
		 * If we try running this code, nothing will happen. It’s not until we call
		 * connect(), that the Flux will start emitting:
		 */

		// try to disable this
		publish.connect();
	}

}

/**
 * If we run above code, console will be overwhelmed with logging. This is
 * simulating a situation where too much data is being passed to consumers.
 */
class ThrottlingDemo {
	public static void main(String[] args) {
		/**
		 * Here, we’ve introduced a sample() method with an interval of two seconds. Now
		 * values will only be pushed to our subscriber every two seconds, meaning the
		 * console will be a lot less hectic.
		 * 
		 * Of course, there are multiple strategies to reduce the amount of data sent
		 * downstream, such as windowing and buffering, but they will be left out of
		 * scope for this article.
		 */
		ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
			while (true) {
				fluxSink.next(System.currentTimeMillis());
			}
		}).sample(Duration.ofMillis(2000)).publish(); 

		publish.subscribe(System.out::println);

		publish.connect();

	}

}