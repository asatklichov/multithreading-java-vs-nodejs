package concurrency.completablefuture.springboot.asyncmethod;

import java.util.concurrent.Executor;

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
		log.debug("Creating Async Task Executor");
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("LookupThread-");
		executor.initialize();
		return executor;
	}

}
