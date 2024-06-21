package concurrency.web.apps.springboot;

import static concurrency.part3.async.api.completablefuture.java11.httpclient.Util.DOMAINS_TXT2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * https://spring.io/guides/gs/async-method/
 * 
 * https://developer.github.com/v3/#rate-limiting
 * 
 * https://developers.google.com/drive/api/v3/handle-errors#quota
 * 
 * https://howtodoinjava.com/spring-boot2/rest/enableasync-async-controller/
 *
 * You will build a lookup service that queries GitHub user information and
 * retrieves data through GitHub’s API. One approach to scaling services is to
 * run expensive jobs in the background and wait for the results by using Java’s
 * CompletableFuture interface.
 * 
 * Java’s CompletableFuture is an evolution from the regular Future. It makes it
 * easy to pipeline multiple asynchronous operations and merge them into a
 * single asynchronous computation.
 * 
 * 
 * To compare how long this takes without the asynchronous feature, try
 * commenting out the @Async annotation and runing the service again. The total
 * elapsed time should increase noticeably, because each query takes at least a
 * second. You can also tune the Executor to increase the corePoolSize attribute
 * for instance.
 * 
 * 
 * @EnableAsync annotation switches on Spring’s ability to run @Async methods in
 *              a background thread pool.
 */
@SpringBootApplication
@EnableAsync
@Slf4j
public class AsyncMethodApplication {
	
	private LookupService lookupService;
	private static HttpClient httpClient;
	
	/*
	 * public Application(LookupService lookupService) { this.lookupService =
	 * lookupService; }
	 */

	public static void main(String[] args) {
		SpringApplication.run(AsyncMethodApplication.class, args);
	}

	/**
	 * This class also customizes the Executor by defining a new bean. Here, the
	 * method is named taskExecutor.
	 * 
	 * If you do not define an Executor bean, Spring creates a
	 * SimpleAsyncTaskExecutor and uses that.
	 * 
	 * https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#scheduling-task-executor
	 * 
	 * @Bean(name = "taskExecutor")
	 */
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//log.debug("Creating Async Task Executor");
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("LookupThread-");
		executor.initialize();
		return executor;
	}
	
	public void run2(String... args) throws Exception {
		long start = System.currentTimeMillis();
		//log.info("Time start: " + new Date(System.currentTimeMillis()));

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
			//log.info("--> " + completableFuture.get());
		}
		//log.info("Elapsed time: " + (System.currentTimeMillis() - start));
		
		////second part  
		httpClient = HttpClient.newHttpClient();
		List<CompletableFuture<String>> completableFutureStringListResponse = Files.lines(Path.of(DOMAINS_TXT2))
				.map(AsyncMethodApplication::validateLink).collect(Collectors.toList());
		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(System.out::println);
		// CPU intensive calc
//		completableFutureStringListResponse.stream().map(CompletableFuture::join).forEach(v -> {
//			long s = (long) (Math.random() *10 + 1);
//			Random generator = new Random(s);
//			heavySum(generator.nextInt());
//			System.out.println(v);
//		});
 
		System.out.println("Result with Spring @Async");
	}

	private static CompletableFuture<String> validateLink(String link) {
		HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(link)).GET().build();

		// including exception handling
		return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.discarding())
				.thenApply(
						asynResult -> 200 == asynResult.statusCode() ? link + " access OK  " : link + " access Failed")
				.exceptionally(e -> "Error occured once accessing to " + link + ", reasonis: " + e.getMessage());

	}

}
