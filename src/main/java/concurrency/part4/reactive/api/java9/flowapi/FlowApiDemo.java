package concurrency.part4.reactive.api.java9.flowapi;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

public class FlowApiDemo {
	static class NewsSubscriber implements Flow.Subscriber<News> {

		private Subscription subscription;
		private static final int MAX_NEWS = 3;
		private int newsReceived = 0;

		@Override
		public void onSubscribe(Subscription subscription) {
			System.out.printf("new subscription %s\n", subscription);
			this.subscription = subscription;
			subscription.request(1);
		}

		@Override
		public void onNext(News item) {
			System.out.printf("news received: %s (%s)\n", item.getHeadline(), item.getDate());
			newsReceived++;
			if (newsReceived >= MAX_NEWS) {
				System.out.printf("%d news received (max: %d), cancelling subscription\n", newsReceived, MAX_NEWS);
				subscription.cancel();
				return;
			}

			subscription.request(1);

		}

		@Override
		public void onError(Throwable throwable) {
			System.err.printf("error occurred fetching news: %s\n", throwable.getMessage());
			throwable.printStackTrace(System.err);

		}

		@Override
		public void onComplete() {
			System.out.println("fetching news completed");
		}
	}

	public static void main(String[] args) {
		try (SubmissionPublisher<News> newsPublisher = new SubmissionPublisher()) {

			NewsSubscriber newsSubscriber = new NewsSubscriber();
			newsPublisher.subscribe(newsSubscriber);

			List.of(News.create("Important news"), News.create("Some other news"), News.create("And news, news, news"))
					.forEach(newsPublisher::submit);

			while (newsPublisher.hasSubscribers()) {
				// wait
			}
			System.out.println("no more news subscribers left, closing publisher..");
		}
	}
}

class PublishingAndConsumingMessageDemo {
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

//https://www.baeldung.com/java-9-reactive-streams
//https://akarnokd.blogspot.com/2017/09/java-9-flow-api-taking-and-skipping.html

class EndSubscriber<T> implements Subscriber<T> {
	private Subscription subscription;
	public List<T> consumedElements = new LinkedList<>();

	/**
	 * The onSubscribe() method is called before processing starts. The instance of
	 * the Subscription is passed as the argument. It is a class that is used to
	 * control the flow of messages between Subscriber and the Publisher:
	 */
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		/**
		 * I'm playing around with the Flow API and, so far, I understand that the
		 * request() method is used for back pressure. Most articles state that this is
		 * akin to controlling the speed of consumption.
		 * 
		 * However, almost every example code I see passes the value 1 into the
		 * request() method, like, subscription.request(1). But I don't quite understand
		 * how does the request() method control the speed of consumption.
		 * 
		 * I have tried to run a test by sending a bunch of items to the publisher and
		 * print the thread name and it seems like every single onNext() is running on
		 * the same worker thread be it I was using request(1) or request(50) :
		 */
		subscription.request(1);
	}

	/**
	 * We also initialized an empty List of consumedElements that’ll be utilized in
	 * the tests.
	 * 
	 * Now, we need to implement the remaining methods from the Subscriber
	 * interface. The main method here is onNext() – this is called whenever the
	 * Publisher publishes a new message:
	 */
	@Override
	public void onNext(T item) {
		System.out.println("Got : " + item);
		consumedElements.add(item);
		subscription.request(1);
	}

	/**
	 * Note that when we started the subscription in the onSubscribe() method and
	 * when we processed a message we need to call the request() method on the
	 * Subscription to signal that the current Subscriber is ready to consume more
	 * messages.
	 * 
	 * Lastly, we need to implement onError() – which is called whenever some
	 * exception will be thrown in the processing, as well as onComplete() – called
	 * when the Publisher is closed:
	 */
	@Override
	public void onError(Throwable throwable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub

	}
}
