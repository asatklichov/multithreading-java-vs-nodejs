package concurrency.part4.reactive.api.spring.reactor.core;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

//import org.junit.Test;
import org.junit.jupiter.api.Test;

import lombok.Data;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;


public class C_TransformingFilteringOperationsTest {

	@Test
	public void skipSomeItemsTest() {
		Flux<String> countFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
				.skip(3);//skip first 3 elements

		StepVerifier.create(countFlux)
		.expectNext("ninety nine", "one hundred")
		.verifyComplete();
	}

	@Test
	public void skipItemsForSometimeTest() {
		Flux<String> countFlux = Flux.just("one", "two", "skip a few", "ninety nine", "one hundred")
				.delayElements(Duration.ofSeconds(1))
				.skip(Duration.ofSeconds(4));//skip some items until some duration has passed.

		StepVerifier.create(countFlux).expectNext("ninety nine", "one hundred").verifyComplete();
	}

	@Test
	public void takeTest() {
		/**
		 * take() can be thought of as the opposite of skip().
		 * 
		 * Takes first N elements 
		 */
		Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Acadia")
				.take(3);

		StepVerifier.create(nationalParkFlux)
		.expectNext("Yellowstone", "Yosemite", "Grand Canyon").verifyComplete();
	}

	@Test
	public void takeForSometimeTest() {
		/**
		 * Like skip(), take() also has an alternative form that’s based on a duration
		 * rather than an item count.
		 */
		Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
				.delayElements(Duration.ofSeconds(1)).take(Duration.ofMillis(3500));

		StepVerifier.create(nationalParkFlux).expectNext("Yellowstone", "Yosemite", "Grand Canyon").verifyComplete();
	}

	@Test
	public void filterTest() {
		/**
		 * filter() is given a Predicate as a lambda that only accepts String values
		 * that don’t have any spaces.
		 */
		Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
				.filter(np -> !np.contains(" "));

		StepVerifier.create(nationalParkFlux).expectNext("Yellowstone", "Yosemite", "Zion").verifyComplete();
	}

	@Test
	public void distinctTest() {
		/**
		 * The distinct operation filters out any duplicate messages.
		 */
		Flux<String> animalFlux = Flux.just("dog", "cat", "bird", "kopek", "kopek", "dog", "bird", "anteater")
				.distinct();

		StepVerifier.create(animalFlux).expectNext("dog", "cat", "bird", "kopek", "anteater").verifyComplete();
	}


	public void mapTest() {
		Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
				.map(n -> {
			String[] split = n.split(" ");//\\s+
			return new Player(split[0], split[1]);
		});
		playerFlux.subscribe(System.out::println);

		StepVerifier.create(playerFlux)
		.expectNext(new Player("Michael", "Jordan"))
		.expectNext(new Player("Scottie", "Pippen"))
		.expectNext(new Player("Steve", "Kerr"))
		.verifyComplete();
	}
  
	public void flatMapTest() {
		Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
				
				/**
				 * Although subscribeOn() is named similarly to subscribe(), they’re quite
				 * different. Whereas subscribe() is a verb, subscribing to a reactive flow and
				 * effectively kicking it off,
				 * 
				 * subscribeOn() is more descriptive, specifying how a subscription should be
				 * handled concurrently.
				 * 
				 * Reactor doesn’t force any particular concurrency model; it’s through
				 * subscribeOn() that you can specify the concurrency model, using one of the
				 * static methods from Schedulers, that you want to use. In this example, you
				 * used parallel(), which uses worker threads from a fixed pool (sized to the
				 * number of CPU cores).
				 */
				.flatMap(n -> Mono.just(n).map(p -> {
					String[] split = p.split("\\s");
					return new Player(split[0], split[1]);
				}).subscribeOn(Schedulers.parallel()));

		List<Player> playerList = Arrays.asList(new Player("Michael", "Jordan"), new Player("Scottie", "Pippen"),
				new Player("Steve", "Kerr"));

		StepVerifier.create(playerFlux)
		.expectNextMatches(p -> playerList.contains(p))
		.expectNextMatches(p -> playerList.contains(p))
		.expectNextMatches(p -> playerList.contains(p))
		.verifyComplete();
	}

	@Data
	@EqualsAndHashCode
	private static class Player {
		public Player(String string, String string2) {
			this.firstName = string;
			this.lastName = string2;
		}

		private final String firstName;
		private final String lastName;
	}

}
