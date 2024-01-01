package concurrency.java.concurrent.api;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//  https://github.com/JosePaumard
public class ConcurrentHashMapParallelism {

	public static void main(String[] args) {

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
