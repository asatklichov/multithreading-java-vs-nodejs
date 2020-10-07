package concurrency.reactive;

import java.io.IOException;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

public class Java9SubmissionPublisherDemo {

	// https://www.baeldung.com/rxjava-vs-java-flow-api
	public static void main(String[] args) throws InterruptedException, IOException {

		// SubmissionPublisher not for prod, Use Flowable
		SubmissionPublisher<Integer> feed = new SubmissionPublisher<Integer>();
		feed.subscribe(new Flow.Subscriber<Integer>() {

			// bridge between Publisher and Subscriber
			private Flow.Subscription subscription;

			@Override
			public void onSubscribe(Subscription subscription) {
				this.subscription = subscription;
				subscription.request(5);

			}

			@Override
			public void onNext(Integer item) {
				System.out.println("Received: " + item);
				subscription.request(1);

			}

			@Override
			public void onError(Throwable throwable) {
				System.out.println("Err: " + throwable);

			}

			@Override
			public void onComplete() {
				System.out.println("completed");

			}
		});

		feed.getSubscribers().forEach(System.out::println);
		for (int i = 0; i < 10; i++) {
			feed.submit(i);
		}
		Thread.sleep(1000);

		feed.close();

	}

}
