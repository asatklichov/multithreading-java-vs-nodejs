package concurrency.part4.reactive.api.spring.webflux;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
 
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.test.StepVerifier;

/**
 * The simplest way to handle an Exception is by throwing it. If something
 * abnormal happens during the processing of a stream element, we can throw an
 * Exception with the throw keyword as if it were a normal method execution.
 */
public class G_HandlingExceptionsInReactorTest {

	@Test
	public void expectErrorTest() {
		Function<String, Integer> mapper = input -> {
			if (input.matches("\\D")) {
				throw new NumberFormatException();
			} else {
				return Integer.parseInt(input);
			}
		};

		Flux<String> inFlux = Flux.just("1", "4", "1.5", "2");
		Flux<Integer> outFlux = inFlux.map(mapper);
		
		/**
		 * This solution works, but it’s not elegant. As specified in the Reactive
		 * Streams specification, rule 2.13, an operator must return normally. Reactor
		 * helped us by converting the Exception to an error signal. However, we could
		 * do better.
		 * 
		 * Essentially, reactive streams rely on the onError method to indicate a
		 * failure condition. In most cases, this condition must be triggered by an
		 * invocation of the error method on the Publisher. Using an Exception for this
		 * use case brings us back to traditional programming
		 */
		StepVerifier.create(outFlux)
		.expectNext(1).expectNext(4)
		.expectError(NumberFormatException.class).verify();
	}
	
	@Test
	public void handleErrorTest() {
		BiConsumer<String, SynchronousSink<Integer>> handler = (input, sink) -> {
		    if (input.matches("\\D")) {
		        sink.error(new NumberFormatException());
		    } else {
		        sink.next(Integer.parseInt(input));
		    }
		};

		Flux<String> inFlux = Flux.just("1", "1.5", "2");
		Flux<Integer> outFlux = inFlux.handle(handler);
		
		/**
		 * Similar to the map operator, we can use the handle operator to process items
		 * in a stream one by one. The difference is that Reactor provides the handle
		 * operator with an output sink, allowing us to apply more complicated
		 * transformations.
		 */
		StepVerifier.create(outFlux)
		.expectNext(1).expectNext(4)
		.expectError(NumberFormatException.class).verify();
	}

}
