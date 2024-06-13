package concurrency.part4.reactive.api.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observables.ConnectableObservable;

//https://www.baeldung.com/cs/reactive-programming
public class ARxOperatorsDemo {
	public static void main(String[] args) throws InterruptedException {

		/**
		 * Create Observable
		 * 
		 * When we want to get information out of an Observable, we implement an
		 * observer interface and then call subscribe on the desired Observable:
		 */
		Observable<String> observable = Observable.just("Hello");
		observable.subscribe(s -> System.out.println(s));

		/**
		 * Creation Operators - OnNext, OnError, and OnCompleted
		 * 
		 * 
		 * As the name suggests, these operators are used to create a stream of data.
		 * For example, the fromArray() operator creates a stream from an array of
		 * values, because when you want to process data with reactive streams, it is
		 * more convenient to transform the data into a stream and represent them as
		 * Observables:
		 * 
		 * 
		 * 
		 * There are three methods on the observer interface that we want to know about:
		 * 
		 * OnNext is called on our observer each time a new event is published to the
		 * attached Observable. This is the method where we’ll perform some action on
		 * each event
		 * 
		 * OnCompleted is called when the sequence of events associated with an
		 * Observable is complete, indicating that we should not expect any more onNext
		 * calls on our observer
		 * 
		 * OnError is called when an unhandled exception is thrown during the RxJava
		 * framework code or our event handling code
		 * 
		 */
		Observable<String> workdays = Observable.fromArray("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
		workdays.subscribe(day -> System.out.println(day), // OnNext
				error -> System.out.println("Error: " + error), // OnError , Throwable::printStackTrace,
				() -> System.out.println("Stream completed.")); // OnCompleted

		System.out.println();

		System.out.println();

		/// Observable Transformations and Conditional Operators

		/**
		 * Join Creation Operators
		 * 
		 * These operators are used to create a stream from multiple other streams. For
		 * example, the concat() operator concatenates multiple streams into one:
		 */
		Observable<String> source1 = Observable.just("10", "20", "30", "40", "50");
		Observable<String> source2 = Observable.just("11", "21", "31", "41", "51");
		Observable<String> source3 = Observable.just("12", "22", "32", "42", "52");

		Observable<String> source = Observable.concat(source1, source2, source3);
		source.subscribe(s -> System.out.println(s), error -> System.out.println("Error: " + error),
				() -> System.out.println("Stream completed."));

		System.out.println();
		/**
		 * Transformation Operators
		 * 
		 * These operators are used to transform the data in a stream. For example, the
		 * map() operator transforms each value in the stream by applying a function to
		 * it:
		 */
		Observable<Integer> sourceInts = Observable.just(1, 2, 3, 4, 5);
		sourceInts.map(x -> 10 * x).subscribe(n -> System.out.println("Value: " + n),
				error -> System.out.println("Error: " + error), () -> System.out.println("Stream completed."));

		System.out.println();
		/**
		 * Filtering Operators
		 * 
		 * These operators are used to filter the data in a stream. For example, the
		 * filter() operator filters out values in the stream that don’t satisfy a
		 * predicate:
		 * 
		 */
		sourceInts = Observable.just(2, 30, 22, 5, 60, 1);
		sourceInts.filter(x -> x > 10).subscribe(n -> System.out.println("Value: " + n),
				error -> System.out.println("Error: " + error), () -> System.out.println("Stream completed."));

		System.out.println();
		/**
		 * Join Operators
		 * 
		 * These operators are used to join two streams into one. For example, the zip()
		 * operator combines two streams into one by applying a function to the values
		 * from each stream. The merge() operator merges two streams into one by
		 * interleaving the emitted items:
		 */

		System.out.println();
		/**
		 * Error Handling Operators
		 * 
		 * These operators are used to handle errors in a stream. For example, the
		 * onErrorReturnItem() operator catches an error and emits a defined item
		 * instead, whereas the onErrorResumeWith() resumes the flow with a given
		 * source, as shown in the examples below:
		 */
		Observable<Integer> numbers = Observable.just(1, 2, 0, 4, 5);
		Observable<Integer> result = numbers.map(x -> 20 / x).onErrorReturnItem(-1);
		result.subscribe(x -> System.out.println("Value: " + x), error -> System.out.println("Error: " + error),
				() -> System.out.println("Stream completed."));

		// The onErrorResumeWith() is available in RxJava as well

		System.out.println();
		/**
		 * The scan operator applies a function to each item emitted by an Observable
		 * sequentially and emits each successive value.
		 */
		String[] letters = { "a", "b", "c" };
		Observable.fromArray(letters).scan(new StringBuilder(), StringBuilder::append)
				.subscribe(total -> System.out.println(total));

		/**
		 * GroupBy
		 * 
		 * Group by operator allows us to classify the events in the input Observable
		 * into output categories.
		 * 
		 * Let’s assume that we created an array of integers from 0 to 10, then apply
		 * group by that will divide them into the categories even and odd:
		 */
		Observable.just(1, 2, 0, 4, 5).groupBy(i -> 0 == (i % 2) ? "EVEN" : "ODD")
				.subscribe(group -> group.subscribe((number) -> {
					if (group.getKey().toString().equals("EVEN")) {
						number++;
					} else {
						number++;
					}
				}));

		// Conditional Operators
		/**
		 * DefaultIfEmpty emits item from the source Observable, or a default item if
		 * the source Observable is empty:
		 */
		Observable.empty().defaultIfEmpty("Observable is empty").subscribe(s -> System.out.println(s));

		/**
		 * Connectable Observables
		 * 
		 * 
		 * A ConnectableObservable resembles an ordinary Observable, except that it
		 * doesn’t begin emitting items when it is subscribed to, but only when the
		 * connect operator is applied to it.
		 */
		String[] resultz = { "6", "3" };
		ConnectableObservable<Long> connectable = Observable.interval(200, TimeUnit.MILLISECONDS).publish();
		connectable.subscribe(i -> resultz[0] += i);

		connectable.connect();
		Thread.sleep(500);

		/**
		 * Single
		 * 
		 * Single is like an Observable who, instead of emitting a series of values,
		 * emits one value or an error notification.
		 * 
		 * With this source of data, we can only use two methods to subscribe:
		 * 
		 * OnSuccess returns a Single that also calls a method we specify
		 * 
		 * OnError also returns a Single that immediately notifies subscribers of an
		 * error
		 */
		Single<String> single = Observable.just("Hello Single").single("ImDefault")
				.doOnSuccess(i -> System.out.println(i)).doOnError(error -> {
					throw new RuntimeException(error.getMessage());
				});
		single.subscribe();

		/**
		 * Subjects
		 * 
		 * A Subject is simultaneously two elements, a subscriber and an observable. As
		 * a subscriber, a subject can be used to publish the events coming from more
		 * than one observable.
		 * 
		 * And because it’s also observable, the events from multiple subscribers can be
		 * reemitted as its events to anyone observing it.
		 * 
		 * In the next example, we’ll look at how the observers will be able to see the
		 * events that occur after they subscribe:
		 */

	}

}
