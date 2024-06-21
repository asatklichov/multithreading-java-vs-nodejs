package concurrency.part4.reactive.api.spring.webflux;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.test.StepVerifier;

public class G_HandlingExceptionsInReactorTest {

	@Test
	public void expectErrorInMapMethodTest() {
		// 1. Throwing Exceptions Directly in a Pipeline Operator
		/**
		 * The simplest way to handle an Exception is by throwing it. If something
		 * abnormal happens during the processing of a stream element, we can throw an
		 * Exception with the throw keyword as if it were a normal method execution.
		 */
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
		StepVerifier.create(outFlux).expectNext(1).expectNext(4).expectError(NumberFormatException.class).verify();
	}

	@Test
	public void handleErrorMethodTest() {
		// 2. Handling Exceptions in the handle Operator
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
		StepVerifier.create(outFlux).expectNext(1).expectError(NumberFormatException.class).verify();
	}

	@Test
	public void handleErrorInFlatMapMethodTest() {
		Function<String, Publisher<Integer>> mapper = input -> {
			if (input.matches("\\D")) {
				return Mono.error(new NumberFormatException());
			} else {
				return Mono.just(Integer.parseInt(input));
			}
		};

		Flux<String> inFlux = Flux.just("1", "1.5", "2");
		Flux<Integer> outFlux = inFlux.flatMap(mapper);

		/**
		 * Another commonly used operator that supports error handling is flatMap. This
		 * operator transforms input elements into Publishers, then flattens the
		 * Publishers into a new stream. We can take advantage of these Publishers to
		 * signify an erroneous state.
		 */
		StepVerifier.create(outFlux).expectNext(1).expectError(NumberFormatException.class).verify();
	}

	@Test
	public void avoidNPETest() {
		Function<String, Integer> mapper = input -> {
			if (input == null) {
				return 0;
			} else {
				return Integer.parseInt(input);
			}
		};

		/**
		 * Handling of null references, which often cause NullPointerExceptions, a
		 * commonly encountered Exception in Java. To avoid this exception, we usually
		 * compare a variable with null and direct the execution to a different way if
		 * that variable is actually null. It’s tempting to do the same in reactive
		 * streams.
		 * 
		 * Apparently, a NullPointerException triggered an error downstream, meaning our
		 * null check didn’t work.
		 * 
		 * Calling onSubscribe, onNext, onError or onComplete MUST return normally
		 * except when any provided parameter is null in which case it MUST throw a
		 * java.lang.NullPointerException to the caller, for all other situations the
		 * only legal way for a Subscriber to signal failure is by cancelling its
		 * Subscription. In the case that this rule is violated, any associated
		 * Subscription to the Subscriber MUST be considered as cancelled, and the
		 * caller MUST raise this error condition in a fashion that is adequate for the
		 * runtime environment.
		 * 
		 * As required by the specification, Reactor throws a NullPointerException when
		 * a null value reaches the map function.
		 */
		Flux<String> inFlux = Flux.just("1", null, "2");
		Flux<Integer> outFlux = inFlux.map(mapper);

		StepVerifier.create(outFlux).expectNext(1).expectError(NullPointerException.class).verify();
	}

}
