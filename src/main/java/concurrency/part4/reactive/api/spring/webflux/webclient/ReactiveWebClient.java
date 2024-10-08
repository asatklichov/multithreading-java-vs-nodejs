package concurrency.part4.reactive.api.spring.webflux.webclient;

import org.springframework.web.reactive.function.client.WebClient;

import concurrency.web.apps.springboot.Employee;
import concurrency.web.apps.springboot.EmployeeReactiveController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * See {@link EmployeeReactiveController} {@link EmployeeReactiveControllerTest}
 */
public class ReactiveWebClient {

	public static void main(String[] args) throws InterruptedException {
		WebClient client = WebClient.create("http://localhost:9999");
		System.out.println(client);

		System.out.println("Retrieving a Mono and Flux Resources");
		// http://localhost:9999/employees/55
		Mono<Employee> employeeMono = client.get().uri("/employees/{id}", "55").retrieve().bodyToMono(Employee.class);
		employeeMono.subscribe(System.out::println);
		Thread.sleep(2000);

		//// http://localhost:9999/employees
		Flux<Employee> employeeFlux = client.get().uri("/employees").retrieve().bodyToFlux(Employee.class);
		employeeFlux.subscribe(System.out::println);
		Thread.sleep(2000);

		System.out.println("done");
	}
}

class ReactiveWebClientForMVCUsage {

	public static void main(String[] args) throws InterruptedException {
		WebClient client = WebClient.create("http://localhost:9999");
		System.out.println(client);

		System.out.println("Retrieving a Mono from MVC Controller, lets see - we know RestTemplate must be used");
		// http://localhost:9999/employees/55
		Mono<Employee> bodyToMono = client.get().uri("/employeez/{id}", "55").retrieve().bodyToMono(Employee.class);
		bodyToMono.subscribe(System.out::println);
		Thread.sleep(2000);

		System.out.println("wow  worked ");
	}
}
