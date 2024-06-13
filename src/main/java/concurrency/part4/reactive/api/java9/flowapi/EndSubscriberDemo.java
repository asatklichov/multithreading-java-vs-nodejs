package concurrency.part4.reactive.api.java9.flowapi;

import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class EndSubscriberDemo {
	public static void main(String[] args) {
		// given
		SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
		EndSubscriber<String> subscriber = new EndSubscriber<>();
		publisher.subscribe(subscriber);
		List<String> items = List.of("1", "x", "2", "x", "3", "x");

		// when
		// assertThat(publisher.getNumberOfSubscribers()).isEqualTo(1);
		items.forEach(publisher::submit);
		publisher.close();

		// then - async test
		/*
		 * await().atMost(1000, TimeUnit.MILLISECONDS) .until(() ->
		 * assertThat(subscriber.consumedElements).containsExactlyElementsOf(items));
		 */
	}

}
