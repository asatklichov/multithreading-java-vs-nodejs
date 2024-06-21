package concurrency.part4.reactive.api.spring.reactor.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

/**
 * During processing the data flowing through a Flux, you might find
 * it helpful to break the stream of data into bite-size chunks.
 */

public class D_BufferingOperationsTest {

  @Test
  public void bufferTest() {
    Flux<String> fruitFlux = Flux.just(
        "apple", "orange", "banana", "kiwi", "strawberry");
    
	/**
	 * where each List has no more than a specified number of elements.
	 * 
	 * Consequently, the original Flux that emits five String values will be
	 * converted to a Flux that emits two List collections, one containing three
	 * fruits and the other with two fruits.
	 * 
	 * So what? Buffering values from a reactive Flux into non-reactive List
	 * collections seems counterproductive.
	 * 
	 * see below test
	 */
    Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);
    
    StepVerifier
        .create(bufferedFlux)
        .expectNext(Arrays.asList("apple", "orange", "banana"))
        .expectNext(Arrays.asList("kiwi", "strawberry"))
        .verifyComplete();
  }
  
  @Test
  public void bufferAndFlatMapTest() throws Exception {
		/**
		 * when you combine buffer() with flatMap(), it enables each of the List
		 * collections to be processed in parallel
		 * 
		 * The log() operation simply logs all Reactive Streams events, so that you can
		 * see what’s really happening.
		 * 
		 *Two buffers are processed in parallel
		 */
    Flux.just(
        "apple", "orange", "banana", "kiwi", "strawberry")
        .buffer(3) //split into two buffers max size 3 elements 
        .flatMap(x -> 
          Flux.fromIterable(x)
            .map(y -> y.toUpperCase())
            .subscribeOn(Schedulers.parallel())   
            .log()
        ).subscribe();
	/**
	 *You see: [parallel-1], [parallel-2]
	 * <pre>
	16:59:21.658 [parallel-1] INFO reactor.Flux.SubscribeOn.1 -- onNext(APPLE)
	16:59:21.658 [parallel-2] INFO reactor.Flux.SubscribeOn.2 -- onNext(KIWI)
	16:59:21.658 [parallel-1] INFO reactor.Flux.SubscribeOn.1 -- onNext(ORANGE)
	16:59:21.658 [parallel-2] INFO reactor.Flux.SubscribeOn.2 -- onNext(STRAWBERRY)
	16:59:21.658 [parallel-1] INFO reactor.Flux.SubscribeOn.1 -- onNext(BANANA)
	16:59:21.660 [parallel-1] INFO reactor.Flux.SubscribeOn.1 -- onComplete()
	16:59:21.660 [parallel-2] INFO reactor.Flux.SubscribeOn.2 -- onComplete()
	 * </pre>
	 */

  }
  
  @Test
  public void collectListTest() {
    Flux<String> fruitFlux = Flux.just(
        "apple", "orange", "banana", "kiwi", "strawberry");
    
	/**
	 * This results in a new Flux that emits a List that contains all the items
	 * published by the source Flux. You can do it with collectList() operation
	 */
    Mono<List<String>> fruitListMono = fruitFlux.collectList();
    fruitListMono.subscribe(System.out::print);
    
    StepVerifier
        .create(fruitListMono)
        .expectNext(Arrays.asList(
            "apple", "orange", "banana", "kiwi", "strawberry"))
        .verifyComplete();
  }
  
  @Test
  public void collectMapTest() {
    Flux<String> animalFlux = Flux.just(
        "aardvark", "elephant", "koala", "eagle", "kangaroo");
    
	/**
	 * Collect them into a Map. Where the key is derived from some characteristic of
	 * the incoming messages. In below example, if key is existing already it is
	 * overridden.
	 */
    Mono<Map<Character, String>> animalMapMono = 
        animalFlux.collectMap(a -> a.charAt(0));
    animalMapMono.subscribe(System.out::print);
    
    StepVerifier
        .create(animalMapMono)
        .expectNextMatches(map -> {
          return
              map.size() == 3 &&
              map.get('a').equals("aardvark") &&
              map.get('e').equals("eagle") &&
              map.get('k').equals("kangaroo");
        })
        .verifyComplete();
  } 
  
}
