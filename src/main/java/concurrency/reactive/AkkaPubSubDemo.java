package concurrency.reactive;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.AsPublisher;
import akka.stream.javadsl.JavaFlowSupport.Sink;
import akka.stream.javadsl.JavaFlowSupport.Source;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class AkkaPubSubDemo {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("temperature");
		Materializer materializer = ActorMaterializer.create(system);

		Publisher<TempInfo> publisher = Source.fromPublisher(getTemperatures("Prague"))
				.runWith(Sink.asPublisher(AsPublisher.WITH_FANOUT), materializer);
		publisher.subscribe(new TempSubscriber());

		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private static Publisher<TempInfo> getTemperatures(String town) {
		return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
	}

}

@Getter
@Setter
@Data
@AllArgsConstructor
class TempInfo {

	public static final Random random = new Random();

	private final String city;
	private final int temp;

	public static TempInfo fetch(String town) {
		if (random.nextInt(10) == 0) {
			throw new RuntimeException("Error!");
		}
		return new TempInfo(town, random.nextInt(100));
	}

}

class TempSubscriber implements Subscriber<TempInfo> {

	private Subscription subscription;

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(TempInfo tempInfo) {
		System.out.println(tempInfo);
		subscription.request(1);
	}

	@Override
	public void onError(Throwable t) {
		System.err.println(t.getMessage());
	}

	@Override
	public void onComplete() {
		System.out.println("completed!");
	}

}

class TempSubscription implements Subscription {

	private static final ExecutorService executor = Executors.newSingleThreadExecutor();

	private final Subscriber<? super TempInfo> subscriber;
	private final String city;

	public TempSubscription(Subscriber<? super TempInfo> subscriber, String town) {
		this.subscriber = subscriber;
		this.city = town;
	}

	@Override
	public void request(long n) {
		executor.submit(() -> {
			for (long i = 0L; i < n; i++) {
				try {
					subscriber.onNext(TempInfo.fetch(city));
				} catch (Exception e) {
					subscriber.onError(e);
					break;
				}
			}
		});
	}

	@Override
	public void cancel() {
		subscriber.onComplete();
	}

}
