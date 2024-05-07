package concurrency.part2.concurrent.api;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * The java.util.concurrent.ConcurrentNavigableMap class is a
 * java.util.NavigableMap with support for concurrent access, and which has
 * concurrent access enabled for its submaps. The "submaps" are the maps
 * returned by various methods like headMap(), subMap() and tailMap().
 * 
 * Rather than re-explain all methods found in the NavigableMap I will just look
 * at the methods added by ConcurrentNavigableMap.
 *

 */
public class ConcurrentNavigableMapExample {

	public static void main(String[] args) {
		// https://jenkov.com/tutorials/java-util-concurrent/concurrentnavigablemap.html
		ConcurrentNavigableMap map = new ConcurrentSkipListMap();

		map.put("1", "one");
		map.put("2", "two");
		map.put("3", "three");

		ConcurrentNavigableMap headMap = map.headMap("2");
		System.out.println(headMap);
		ConcurrentNavigableMap tailMap = map.tailMap("2");
		System.out.println(tailMap);

		map.put("1", "one");
		map.put("2", "two");
		map.put("3", "three");

		ConcurrentNavigableMap subMap = map.subMap("2", "3");
		System.out.println(subMap);
		/**
		 * *
		 * 
		 * <pre>
		 *More Methods
		The ConcurrentNavigableMap interface contains a few more methods that might be of use. For instance:
		
		descendingKeySet()
		descendingMap()
		navigableKeySet()
		See the official JavaDoc for more information on these methods.
		 * </pre>
		 */
	}

}
