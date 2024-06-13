package concurrency.part4.reactive.api.java9.flowapi;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

/**
 * Since Java 9, we can create Reactive Streams by introducing four core
 * interfaces: Publisher, Subscriber, Subscription, Processor, and one concrete
 * class: SubmissionPublisher that implements the Publisher interface. Each
 * interface plays a different role, corresponding to the principles of Reactive
 * Streams. We can use the submit() method of SubmissionPublisher class to
 * publish the provided item to each subscriber.
 * 
 * 
 * public class SubmissionPublisher<T> extends Object implements
 * Flow.Publisher<T>, AutoCloseable
 * 
 * A Flow.Publisher that asynchronously issues submitted (non-null) items to
 * current subscribers until it is closed.
 * 
 * Each current subscriber receives newly submitted items in the same order
 * unless drops or exceptions are encountered.
 * 
 * Using a SubmissionPublisher allows item generators to act as compliant
 * reactive-streams Publishers relying on drop handling and/or blocking for flow
 * control. A SubmissionPublisher uses the Executor supplied in its constructor
 * for delivery to subscribers. The best choice of Executor depends on expected
 * usage. If the generator(s) of submitted items run in separate threads, and
 * the number of subscribers can be estimated, consider using a
 * Executors.newFixedThreadPool(int). Otherwise consider using the default,
 * normally the ForkJoinPool.commonPool().
 */
public class ASubmissionPublisherDemo {

	public static void main(String args[]) {
		SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
		MySubscriber<String> subscriber = new MySubscriber<>("Mine");
		MySubscriber<String> subscriberYours = new MySubscriber<>("Yours");
		MySubscriber<String> subscriberHis = new MySubscriber<>("His");
		MySubscriber<String> subscriberHers = new MySubscriber<>("Her");

		publisher.subscribe(subscriber);
		publisher.subscribe(subscriberYours);
		publisher.subscribe(subscriberHis);
		publisher.subscribe(subscriberHers);

		publisher.submit("One");
		publisher.submit("Two");
		publisher.submit("Three");
		publisher.submit("Four");
		publisher.submit("Five");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		publisher.close();
	}
}

class MySubscriber<T> implements Subscriber<T> {
	private Subscription subscription;
	private String name;

	public MySubscriber(String name) {
		this.name = name;
	}

	@Override
	public void onComplete() {
		System.out.println(name + ": onComplete");
	}

	@Override
	public void onError(Throwable t) {
		System.out.println(name + ": onError");
		t.printStackTrace();
	}

	@Override
	public void onNext(T msg) {
		System.out.println(name + ": " + msg.toString() + " received in onNext");
		subscription.request(1);
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		System.out.println(name + ": onSubscribe");
		this.subscription = subscription;
		subscription.request(1);
	}
}
