package concurrency.part4.reactive.api.spring.webflux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


public class A_CreationOperationsTest {

	/**
	 * Use the static just() method on Flux or Mono to create a reactive type whose
	 * data is driven by those objects.
	 */
	@Test
	public void justTest() {
		Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Grape", "Banana", "Strawberry");

		/**
		 * We need to subscribe to Publisher in order for it to emit the elements - data
		 * won’t start flowing until we subscribe.
		 * 
		 * Flux.just(1, 2, 3, 4).log().map(i -> { System.out.println(i + ": " +
		 * Thread.currentThread()); return i * 2; })
		 * 
		 * .subscribe(elements::add);
		 * 
		 * For testing, use StepVerifier subscribes to the reactive type and then
		 * applies assertions against the data
		 * 
		 */
		StepVerifier.create(fruitFlux)
		.expectNext("Apple")
		.expectNext("Orange")
		.expectNext("Grape")
		.expectNext("Banana")
		.expectNext("Strawberry")
		.verifyComplete();
	}

	@Test
	public void fromArrayTest() {
		String[] fruits = new String[] { "Apple", "Orange", "Grape", "Banana", "Strawberry" };

		Flux<String> fruitFlux = Flux.fromArray(fruits);

		StepVerifier.create(fruitFlux)
		.expectNext("Apple")
		.expectNext("Orange")
		.expectNext("Grape")
		.expectNext("Banana")
		.expectNext("Strawberry")
		.verifyComplete();
	}

	@Test
	public void fromIterableTest() {
		List<String> fruitList = new ArrayList<>();
		fruitList.add("Apple");
		fruitList.add("Orange");
		fruitList.add("Grape");
		fruitList.add("Banana");
		fruitList.add("Strawberry");

		/**
		 * If you need to create a Flux from a java.util.List, java.util.Set, or any
		 * other implementation of java.lang.Iterable, you can pass it into the static
		 * fromIterable() method
		 */
		Flux<String> fruitFlux = Flux.fromIterable(fruitList);

		StepVerifier.create(fruitFlux)
		.expectNext("Apple")
		.expectNext("Orange")
		.expectNext("Grape")
		.expectNext("Banana")
		.expectNext("Strawberry")
		.verifyComplete();
	}

	@Test
	public void fromStreamTest() {
		Stream<String> fruitStream = Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

		Flux<String> fruitFlux = Flux.fromStream(fruitStream);

		StepVerifier.create(fruitFlux)
		.expectNext("Apple")
		.expectNext("Orange")
		.expectNext("Grape")
		.expectNext("Banana")
		.expectNext("Strawberry")
		.verifyComplete();
	} 

	@Test
	public void rangeTest() {
		/**
		 * When need Flux to act as a incremental counter, use range() method.
		 */
		Flux<Integer> intervalFlux = Flux.range(3, 7);

		StepVerifier.create(intervalFlux)
		.expectNext(3)
		.expectNext(4)
		.expectNext(5)
		.expectNext(6)
		.expectNext(7)
		.expectNext(8)
		.expectNext(9)
		.verifyComplete();
	}
	
	@Test
	public void intervalTest() {
		/**
		 * interval() is like the range() method, used as counter. But it is special,
		 * instead of you giving it a starting and ending value, you specify a duration
		 * or how often a value should be emitted.
		 * 
		 * Notice that the value emitted by an interval Flux starts with 0 and
		 * increments on each successive item.
		 * 
		 * interval() isn’t given a maximum value, potentially can run forever, that is
		 * why take() operation is used to limit the results to the first N (five) entries.
		 */
		Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1)).take(5);

		StepVerifier.create(intervalFlux)
		.expectNext(0L)
		.expectNext(1L)
		.expectNext(2L)
		.expectNext(3L)
		.expectNext(4L)
		.verifyComplete();
	}
}
