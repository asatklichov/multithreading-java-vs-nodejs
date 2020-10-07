package concurrency.completablefuture.springboot.asyncmethod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LookupService {

	// https://api.github.com/users/asatklichov
	private static final String URL = "https://api.github.com/users/%s";

	private final RestTemplate restTemplate;

	public LookupService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	/**
	 * To compare how long this takes without the asynchronous feature, try
	 * commenting out the @Async annotation and runing the service again. The total
	 * elapsed time should increase noticeably, because each query takes at least a
	 * second. You can also tune the Executor to increase the corePoolSize attribute
	 * for instance.
	 * 
	 * https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#scheduling-task-executor
	 * 
	 * @param userName
	 * @return
	 * @throws InterruptedException
	 */
	@Async
	public CompletableFuture<User> findUser(String userName) throws InterruptedException {
		log.info("Looking up " + userName);
		User user = restTemplate.getForObject(String.format(URL, userName), User.class);
		// delay to demon
		// Thread.sleep(3000L);
		return CompletableFuture.completedFuture(user);
	}

	/**
	 * returning a new CompletableFuture that are already completed with the given
	 * values.
	 * 
	 * @param userNames
	 * @return
	 * @throws InterruptedException
	 */
	@Async
	public CompletableFuture<List<User>> findUsers(List<String> userNames) throws InterruptedException {
		return CompletableFuture.completedFuture(findAllByUserNames(userNames));
	}

	public List<User> findAllByUserNames(List<String> userNames) {
		List<User> users = new ArrayList<User>();
		userNames.forEach(userName -> {
			users.add(restTemplate.getForObject(String.format(URL, userName), User.class));
		});

		return users;
	}

}
