package concurrency.part4.reactive.api.spring.reactor.core;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Performing logic operations on reactive types
 * 
 * To check If the entries published by a Mono or Flux match some criteria. The
 * all() and any() operations perform such logic
 * 
 */
public class E_LogicalOperationsTest {

	@Test
	public void allTest() {
		Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

		/**
		 * A flux can be tested to ensure that all messages meet some condition with the
		 * all operation.
		 * 
		 * e.g. check if every String published by a Flux contains the letter a or the
		 * letter k
		 */
		Mono<Boolean> hasAMono = animalFlux.all(a -> a.contains("a")); // all has letter a
		hasAMono.subscribe(System.out::println);

		StepVerifier.create(hasAMono).expectNext(true).verifyComplete();

		Mono<Boolean> hasKMono = animalFlux.all(a -> a.contains("k"));// some has no letter k
		hasKMono.subscribe(System.out::println);
		StepVerifier.create(hasKMono).expectNext(false).verifyComplete();
	}

	@Test
	public void anyTest() {
		Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

		/**
		 * A flux can be tested to ensure that at least one message meets some condition
		 * with the any operation
		 */
		Mono<Boolean> hasAMono = animalFlux.any(a -> a.contains("a"));// yes, all elements contains 'a'
		hasAMono.subscribe(System.out::println);

		StepVerifier.create(hasAMono).expectNext(true).verifyComplete();

		Mono<Boolean> hasZMono = animalFlux.any(a -> a.contains("z"));// no single element which contains 'z'
		hasZMono.subscribe(System.out::println);
		StepVerifier.create(hasZMono).expectNext(false).verifyComplete();
	}

}
