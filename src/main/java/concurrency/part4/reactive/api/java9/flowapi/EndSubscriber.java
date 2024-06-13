package concurrency.part4.reactive.api.java9.flowapi;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

//https://www.baeldung.com/java-9-reactive-streams

public class EndSubscriber<T> implements Subscriber<T> {
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
