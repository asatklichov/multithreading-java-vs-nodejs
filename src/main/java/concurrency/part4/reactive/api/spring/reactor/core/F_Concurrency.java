package concurrency.part4.reactive.api.spring.reactor.core;

import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * All of our above examples have currently run on the main thread. However, we
 * can control which thread our code runs on if we want. The Scheduler interface
 * provides an abstraction around asynchronous code, for which many
 * implementations are provided for us. Let’s try subscribing to a different
 * thread to main:
 */
public class F_Concurrency {
	public static void main(String[] args) {
		List<Integer> elements = new ArrayList<>();

		/**
		 * The Parallel scheduler will cause our subscription to be run on a different
		 * thread, which we can prove by looking at the logs. We see the first entry
		 * comes from the main thread and the Flux is running in another thread called
		 * parallel-1.
		 * 
		 * Concurrency models for Schedulers: .immediate(), .single(), .newSingle(),
		 * .elastic(), .parallel().
		 * 
		 * 
		 * .immediate() Executes the subscription in the current thread.
		 * 
		 * .single() Executes the subscription in a single, reusable thread. Reuses the
		 * same thread for all callers.
		 * 
		 * .newSingle() Executes the subscription in a per-call dedicated thread.
		 * 
		 * .elastic() Executes the subscription in a worker pulled from an unbounded,
		 * elastic pool. New worker threads are created as needed, and idle workers are
		 * disposed of (by default, after 60 seconds).
		 * 
		 * .parallel() Executes the subscription in a worker pulled from a fixed-size
		 * pool, sized to the number of CPU cores.
		 * 
		 */

		Flux.just(1, 2, 3, 4)
		.map(y -> y+y)
        .subscribeOn(Schedulers.parallel())   
        .log().subscribe();
		System.out.println(elements); 

	}

}
