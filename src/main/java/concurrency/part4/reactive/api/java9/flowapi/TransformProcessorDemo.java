package concurrency.part4.reactive.api.java9.flowapi;

import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class TransformProcessorDemo {

	public static void main(String[] args) {
		// given
		SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
		TransformProcessor<String, Integer> transformProcessor = new TransformProcessor<>(Integer::parseInt);
		EndSubscriber<Integer> subscriber = new EndSubscriber<>();
		List<String> items = List.of("1", "2", "3");
		List<Integer> expectedResult = List.of(1, 2, 3);

		// when
		publisher.subscribe(transformProcessor);
		transformProcessor.subscribe(subscriber);
		items.forEach(publisher::submit);
		publisher.close();
		
		subscriber.consumedElements.forEach(System.out::println);

		// then
		//await().atMost(1000, TimeUnit.MILLISECONDS)
		//		.until(() -> assertThat(subscriber.consumedElements).containsExactlyElementsOf(expectedResult));
	}
}

class TransformProcessor<T, R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {

	private Function<T, R> function;
	private Flow.Subscription subscription;

	public TransformProcessor(Function<T, R> function) {
		super();
		this.function = function;
	}

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(T item) {
		submit(function.apply(item));
		subscription.request(1);
	}

	@Override
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	@Override
	public void onComplete() {
		close();
	}
}