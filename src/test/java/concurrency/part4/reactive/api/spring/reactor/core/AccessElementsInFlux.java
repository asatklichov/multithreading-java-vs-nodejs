package concurrency.part4.reactive.api.spring.reactor.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Below tests are about how to access the element of a Flux
 */
public class AccessElementsInFlux {

	@Test
	public void nextTest() {
		String[] fruits = new String[] { "Apple", "Orange", "Grape", "Banana", "Strawberry" };

		Flux<String> fruitFlux = Flux.fromArray(fruits);

		/**
		 * This method will return the first element of the flux, wrapped into the
		 * reactive Mono type:
		 */
		Mono<String> one = fruitFlux.next();
		StepVerifier.create(one).expectNext("Apple").verifyComplete();

		/**
		 * On the other hand, if we’ll call next() on an empty flux, the result will be
		 * an empty Mono. Consequently, blocking it will return null
		 */
		Flux<String> emptyFlux = Flux.empty();
		Mono<String> next = emptyFlux.next();
		next.subscribe(System.out::print);
		// StepVerifier.create(next).expectNext(empty).verifyComplete();
		// StepVerifier.create(next).expectError(AssertionError.class).verify();

	}

	@Test
	public void takeTest() {

		String[] fruits = new String[] { "Apple", "Orange", "Grape", "Banana", "Strawberry" };
		Flux<String> fruitFlux = Flux.fromArray(fruits);

		/**
		 * take() method of a reactive flux is equivalent to limit() for Java 8 Streams.
		 */
		Flux<String> take = fruitFlux.take(1);
		StepVerifier.create(take).expectNext("Apple").verifyComplete();

		/**
		 * Similarly, for an empty flux, take(1) will return an empty flux
		 */
		Flux<String> emptyFlux = Flux.empty();
		Flux<String> firstFlux = emptyFlux.take(1);
		StepVerifier.create(firstFlux).verifyComplete();

	}

	@Test
	public void elementAtTest() {

		String[] fruits = new String[] { "Banana", "Apple", "Orange", "Grape", "Strawberry" };
		Flux<String> fruitFlux = Flux.fromArray(fruits);

		/**
		 * We can use elementAt(0) to get the first element of a flux in a non-blocking
		 * way:
		 */
		Mono<String> first = fruitFlux.elementAt(0);
		StepVerifier.create(first).expectNext("Banana").verifyComplete();

		/**
		 * Though, if the index passed as a parameter is greater than the number of
		 * elements emitted by the flux, an error will be emitted:
		 */
		Mono<String> out = fruitFlux.elementAt(5);
		StepVerifier.create(out).expectError(IndexOutOfBoundsException.class);

		Flux<String> emptyFlux = Flux.empty();
		Mono<String> noElement = emptyFlux.elementAt(0);
		StepVerifier.create(noElement).expectError(IndexOutOfBoundsException.class);
	}

	@Test
	public void blockFirstByReactiveWorlsTest() {

		String[] fruits = new String[] { "Banana", "Apple", "Orange", "Grape", "Strawberry" };
		Flux<String> fruitFlux = Flux.fromArray(fruits);

		/**
		 * Alternatively, we can also use blockFirst(). Though, as the name suggests,
		 * this is a blocking method. As a result, if we use blockFirst(), we’ll be
		 * leaving the reactive world, and we’ll lose all its benefits
		 */
		String blockFirst = fruitFlux.blockFirst();
		assertThat(blockFirst).isEqualTo("Banana");

	}

	@Test
	public void toStreamJava8Test() {

		String[] fruits = new String[] { "Banana", "Apple", "Orange", "Grape", "Strawberry" };
		Flux<String> fruitFlux = Flux.fromArray(fruits);

		/**
		 * convert the flux to a Java 8 stream and then access the first element:
		 */
		Stream<String> java8Stream = fruitFlux.toStream();
		Optional<String> first = java8Stream.findFirst();
		assertThat(first.get()).isEqualTo("Banana");

	}

	@Test
	public void collectListTest() {
		Flux<Integer> flux = Flux.just(100, 200, 300, 400, 500);

		flux.collectList().subscribe(list -> {
			if (!list.isEmpty()) {
				Integer firstElement = list.get(0);
				System.out.println("First element of Flux: " + firstElement);
			} else {
				System.out.println("Flux is empty");
			}
		}, error -> System.err.println("Error: " + error), () -> System.out.println("Flux completed"));
	}
}
