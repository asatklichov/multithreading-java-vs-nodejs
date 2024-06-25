package concurrency.virtual.threads.api;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class C_VirtualThreadsUnderTheHood {

	public static final Pattern POOL_NAME = Pattern.compile("ForkJoinPool-[\\d?]");
	public static final Pattern WORKER_NAME = Pattern.compile("worker-[\\d?]+");

	public static void main(String[] args) throws InterruptedException {

		Set<String> poolNames = ConcurrentHashMap.newKeySet();
		Set<String> pThreadNames = ConcurrentHashMap.newKeySet();
		int numThreads = 1_000_000;// million VTs

		var threads = IntStream.range(0, numThreads).mapToObj(i -> Thread.ofVirtual().unstarted(() -> {
			String poolName = readPoolName();
			poolNames.add(poolName);
			String workerName = readWorkerName();
			pThreadNames.add(workerName);
		})).toList();

		Instant begin = Instant.now();
		for (Thread thread : threads) {
			thread.start();
		}

		for (Thread thread : threads) {
			thread.join();// as in classic core Thread
		}

		Instant end = Instant.now();
		System.out.println("# Virtual Threads = " + numThreads);
		System.out.println("# cores = " + Runtime.getRuntime().availableProcessors());
		System.out.println("# Time = " + Duration.between(begin, end).toMillis() + " ms");
		System.out.println("---- Pools ---");
		poolNames.forEach(System.out::println);
		System.out.println("---- Platform Threads ---");
		pThreadNames.forEach(System.out::println);
		System.out.println(pThreadNames.size());

	}

	private static String readPoolName() {
		String name = Thread.currentThread().toString();
		Matcher poolMatcher = POOL_NAME.matcher(name);
		if (poolMatcher.find()) {
			return poolMatcher.group();
		}
		return "pool not found";
	}

	private static String readWorkerName() {
		String name = Thread.currentThread().toString();
		// ComBA time, CoRIA reg-ex usages ;) gowy gunlerdi 2008
		Matcher workerMatcher = WORKER_NAME.matcher(name);
		if (workerMatcher.find()) {
			return workerMatcher.group();
		}
		return "not found";
	}

}