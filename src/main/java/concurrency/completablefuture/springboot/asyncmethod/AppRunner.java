package concurrency.completablefuture.springboot.asyncmethod;

import static concurrency.completablefuture.java11.httpclient.Util.DOMAINS_TXT2;
import static concurrency.completablefuture.java11.httpclient.Util.printElapsedTime;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AppRunner implements CommandLineRunner {

	private final LookupService lookupService;
	private static HttpClient httpClient;

	public AppRunner(LookupService lookupService) {
		this.lookupService = lookupService;
	}

	public void run(String... args) throws Exception {
		long start = System.currentTimeMillis();
		log.info("Time start: " + new Date(System.currentTimeMillis()));

		CompletableFuture<User>[] completableFutures = new CompletableFuture[19];

		String[] names = "PivotalSoftware,CloudFoundry,Spring-Projects,asatklichov,teverett,johnpapa,LSP,DAP,code4z,temanbrcom,zacanbrcom,sergiuilie,ishche,grianbrcom,DStatWriter,zeibura,zimlu02,tomcec,filipkroupa"
				.split(",");
		for (int i = 0; i < names.length; i++) {
			completableFutures[i] = lookupService.findUser(names[i]);
		}

		// asynchronous lookups
		// CompletableFuture<User> user4 = lookupService.findUser("asatklichov");

		// Wait until completed
		CompletableFuture.allOf(completableFutures).join();
		// CompletableFuture.allOf(user1, user2, 9).join();

		for (CompletableFuture<User> completableFuture : completableFutures) {
			log.info("--> " + completableFuture.get());
		}
		log.info("Elapsed time: " + (System.currentTimeMillis() - start));

	}

	public void run2(String... args) throws Exception {
		Instant start = Instant.now();
		httpClient = HttpClient.newHttpClient();
		List<CompletableFuture<String>> completableFutureStringListResponse = Files.lines(Path.of(DOMAINS_TXT2))
				.map(AppRunner::validateLink).collect(Collectors.toList());
		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(System.out::println);
		// CPU intensive calc
//		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(v -> {
//			long s = (long) (Math.random() *10 + 1);
//			Random generator = new Random(s);
//			heavySum(generator.nextInt());
//			System.out.println(v);
//		});

		printElapsedTime(start);
		System.out.println("Result with Spring @Async");
	}

	private static CompletableFuture<String> validateLink(String link) {
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(link)).GET().build();

		// including exception handling
		return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding())
				.thenApply(
						asynResult -> 200 == asynResult.statusCode() ? link + " access OK  " : link + " access Failed")
				.exceptionally(e -> "Error occured once accessing to " + link + ", reson is: " + e.getMessage());

	}

}
