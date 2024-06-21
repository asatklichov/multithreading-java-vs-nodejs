package concurrency.part4.reactive.api.spring.webflux;

import java.time.Duration;

import org.junit.jupiter.api.Test;

//import org.junit.Test; 

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

/**
 * When you want to merge two reactive types, or you want to aplit a Flux into
 * more than one reactive types, then use operations that combine and split
 * Reactor’s Flux and Mono.
 */

public class B_CombiningOperationsTest {

	@Test
	public void mergeTest() {

		/**
		 * Flux publishes data as quickly as it possibly can. Therefore, you use a
		 * delayElements() operation on both of the created Flux streams to slow them
		 * down a little—only emitting an entry every 500 ms.
		 * 
		 * That is why delays needed to avoid the first flux from streaming the data
		 * through before subscribing to the second flux.
		 * 
		 * Furthermore, food Flux starts streaming after the character Flux, you apply a
		 * delaySubscription() operation to the food Flux so that it won’t emit any data
		 * until 250 ms have passed following a subscription.
		 */
		Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa")
				.delayElements(Duration.ofMillis(500));
		Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples")
				.delaySubscription(Duration.ofMillis(250))
				.delayElements(Duration.ofMillis(500));

		/**
		 * From two Flux streams makes a single resulting Flux
		 */
		Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);
		mergedFlux.subscribe(System.out::println);//do print

		/**
		 * The order of items emitted from the merged Flux aligns with the timing of how
		 * they’re emitted from the sources.
		 * 
		 * Because mergeWith() can’t guarantee a perfect back and forth between its
		 * sources, you may want to consider the zip() operation instead.
		 */
		StepVerifier.create(mergedFlux)
		.expectNext("Garfield")
		.expectNext("Lasagna")
		.expectNext("Kojak")
		.expectNext("Lollipops")
		.expectNext("Barbossa")
		.expectNext("Apples")
		.verifyComplete();
	}

	@Test
	public void zipTest() {
		Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa", "Bowenjik", "Jady");
		Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples", "Bowrek");

		/**
		 * When two Flux objects are zipped together, it results in a new Flux that
		 * produces a tuple of items, where the tuple contains one item from each source
		 * Flux.
		 * 
		 * 
		 * Unlike mergeWith(), the zip() operation is a static creation
		 * operation. The created Flux has a perfect alignment between characters and
		 * their favorite foods. No need DELAYs
		 */
		Flux<Tuple2<String, String>> zippedFlux = Flux.zip(characterFlux, foodFlux);
		//you see, if no pair, e.g. "Jady", it will not TUPLEd
		zippedFlux.subscribe(System.out::println);

		StepVerifier.create(zippedFlux)
				.expectNextMatches(p -> p.getT1().equals("Garfield") && p.getT2().equals("Lasagna"))
				.expectNextMatches(p -> p.getT1().equals("Kojak") && p.getT2().equals("Lollipops"))
				.expectNextMatches(p -> p.getT1().equals("Barbossa") && p.getT2().equals("Apples"))
				.expectNextMatches(p -> p.getT1().equals("Bowenjik") && p.getT2().equals("Bowrek"))
				.verifyComplete();
	}

	/**
	 * Each item emitted from the zipped Flux is a Tuple  (a
	 * container object that carries two other objects). If you’d rather not work
	 * with a Tuple and would rather work with some other type, you can provide a
	 * Function to zip() that produces any object you’d like, given the two items 
	 */
	@Test
	public void zipToDTOTest() {
		Flux<String> characterFlux = Flux.just("Garfield", "Kojak", "Barbossa","Bowenjik", "Jady");
		Flux<String> foodFlux = Flux.just("Lasagna", "Lollipops", "Apples", "Bowrek");

		Flux<String> zippedFlux = Flux.zip(characterFlux, foodFlux, (c, f) -> c + " eats " + f);
		zippedFlux.subscribe(System.out::println);

		StepVerifier.create(zippedFlux)
		.expectNext("Garfield eats Lasagna")
		.expectNext("Kojak eats Lollipops")
		.expectNext("Barbossa eats Apples")
		.expectNext("Bowenjik eats Bowrek")
		.verifyComplete();
	}

	@Test
	public void selectFirstTest() {
		/**
		 * Two Flux objects are given, and you do not want to merge them but you
		 * merely want to create a new Flux that emits the values from the first Flux
		 * that produces a value.
		 */
		// delay needed to "slow down" the slow Flux

		Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
				.delaySubscription(Duration.ofMillis(200));
		Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

		/**
		 * Here slow Flux won’t publish any because of 200 ms and there fast Flux has
		 * started publishing, the newly created Flux will simply ignore the slow Flux
		 * and only publish values from the fast Flux.
		 * 
		 * Pick the first {@link Publisher} to emit any signal
		 * (onNext/onError/onComplete) and replay all signals from that
		 * {@link Publisher}, effectively behaving like the fastest of these competing
		 * sources.
		 * 
		 * Simply select the fastes one not to lose the time ;)
		 */
		Flux<String> firstFlux = Flux.first(slowFlux, fastFlux);
		firstFlux.subscribe(System.out::println);

		StepVerifier.create(firstFlux)
		.expectNext("hare")
		.expectNext("cheetah")
		.expectNext("squirrel")
		.verifyComplete();
	}

}
