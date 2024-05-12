package concurrency.part2.concurrent.api;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//  https://github.com/JosePaumard
class ConcurrentHashMapParallelism {

	public static void main(String[] args) {

		//ConcurrentMap<Actor, Set<Movie>> map //has no .reduce method etc.  
		ConcurrentHashMap<Actor, Set<Movie>> map = new ConcurrentHashMap<>();

		MovieReader reader = new MovieReader();
		reader.addActorsToMap(map);

		System.out.println("# Actors = " + map.size());

		int maxMoviesForOneActor = map.reduce(63, (actor, movies) -> movies.size(), Integer::max);
		System.out.println("Max movies for one actor = " + maxMoviesForOneActor);

		Actor mostSeenActor = map.search(63, (actor, movies) -> movies.size() == maxMoviesForOneActor ? actor : null);
		System.out.println("Most seen actor = " + mostSeenActor);

		int numberOfMoviesReferences = map.reduce(63, (actor, movies) -> movies.size(), Integer::sum);

		System.out.println("Average movies per actor = " + numberOfMoviesReferences / map.size());
	}
}

/**
 * 
 * ConcurrentMap Implementations
 *
 * <pre>
Since ConcurrentMap is an interface, you need to use one of its 
implementations in order to use it. The java.util.concurrent package 
contains the following implementations of the ConcurrentMap interface:

ConcurrentHashMap
ConcurrentHashMap
The ConcurrentHashMap is very similar to the java.util.HashTable class, except 
that ConcurrentHashMap offers better concurrency than HashTable does. ConcurrentHashMap does not lock the Map while you are reading from it. Additionally, ConcurrentHashMap does not lock the entire Map when writing to it. It only locks the part of the Map that is being written to, internally.

Another difference is that ConcurrentHashMap does not throw 
ConcurrentModificationException if the ConcurrentHashMap is changed while being iterated. 
The Iterator is not designed to be used by more than one thread though.
 * </pre>
 */
class ConcurrentMapExample {

	// https://jenkov.com/tutorials/java-util-concurrent/concurrentmap.html
	public static void main(String[] args) {
		ConcurrentMap concurrentMap = new ConcurrentHashMap();

		concurrentMap.put("key", "value");

		Object value = concurrentMap.get("key");
		System.out.println(value);
	}

}