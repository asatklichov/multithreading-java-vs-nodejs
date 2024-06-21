package concurrency.part4.reactive.api.spring.webflux;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class F_LoggingOperationsTest {

	@Test 
	public void logTest() {
		Flux<String> beltColors = Flux
				.just("white", "yellow", "orange", "green", "purple", "blue")
				.log();
		beltColors.subscribe();
		System.out.println();
	}

	@Test 
	public void logInMapTest() {
		Flux<String> beltColors = Flux
				.just("white", "yellow", "orange", "green", "purple", "blue")
				.map(cb -> cb.toUpperCase())
				.log();
		beltColors.subscribe();
		System.out.println();
	}

	@Test
	public void logInFlatMapTest() throws Exception {
		//to run in parallel  
		Flux<String> beltColors = Flux
				.just("white", "yellow", "orange", "green", "purple", "blue")
				.flatMap(cb -> Mono.just(cb)
						.map(c -> c.toUpperCase())
						.log()
						.subscribeOn(Schedulers.parallel()));
		beltColors.subscribe();
		System.out.println();

		Thread.sleep(3000L);
	}

}
