package concurrency.part4.reactive.api.spring.reactor.core;

import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Flux;

/**
 * Perform operations on the data in our stream, responding to events
 */
public class D_OperatingOnStreams {
	public static void main(String[] args) {

		List<Integer> elements = new ArrayList<>();

		System.out.println("Mapping Data in a Stream");
		Flux.just(1, 2, 3, 4).log().map(i -> {
			System.out.println(i + ": " + Thread.currentThread());
			return i * 2;
		}).subscribe(elements::add);
		System.out.println(elements);

		System.out.println();
		System.out.println("Combining Two Streams");
		/**
		 * We are creating another Flux that keeps incrementing by one and streaming it
		 * together with our original one.
		 */
		elements = new ArrayList<>();
		List<String> elms = new ArrayList<>();
		Flux.just(1, 2, 3, 4).log().map(i -> i * 10).zipWith(Flux.range(1, Integer.MAX_VALUE),
				(one, two) -> String.format("First Flux: %d, Second Flux: %d", one, two)).subscribe(elms::add);
		System.out.println(elms);
	}

}
