package concurrency.part4.reactive.api.spring.webflux.webclient;

import org.springframework.web.reactive.function.client.WebClient;

import concurrency.web.apps.springboot.EmployeeReactiveController;
import reactor.core.publisher.Mono;

/**
 * See {@link EmployeeReactiveController}
 */
public class Z_FunctionalStyleWebClient {

	public static void main(String[] args) throws InterruptedException {
		WebClient client = WebClient.create("http://localhost:9999");
		System.out.println(client);
		System.out.println("Functional style request handling client");
		//// http://localhost:9999/helloRouter
		Mono<String> bodyToMono = client.get().uri("/helloRouter").retrieve().bodyToMono(String.class);
		bodyToMono.subscribe(System.out::println);
		Thread.sleep(2000);

		System.out.println("done");
	}
}
