package concurrency.web.apps.springboot;

import java.time.Duration;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.reactivex.Observable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeReactiveController {

	// private final EmployeeRepository employeeRepository; TODO

	// http://localhost:9999/employees/87
	@GetMapping("/{id}")
	public Mono<Employee> getEmployeeById(@PathVariable String id) {
		// return employeeRepository.findEmployeeById(id);
		Employee user = new Employee("Alma", id);

		return Mono.just(user);
	}

	// http://localhost:9999/employees
	@GetMapping
	public Flux<Employee> getEmployees() {
		// return employeeRepository.findAllEmployees();
		Employee user1 = new Employee("Hurma", "85");
		Employee user2 = new Employee("Enar", "47");
		Employee user3 = new Employee("Uzum", "53");

		return Flux.just(user1, user2, user3).delayElements(Duration.ofMillis(500));
	}

	@GetMapping("/iterable")
	public Flux<Employee> getEmps() {
		// return employeeRepository.findAllEmployees();
		Employee user1 = new Employee("Hurma", "85");
		Employee user2 = new Employee("Enar", "47");
		Employee user3 = new Employee("Uzum", "53");

		return Flux.fromIterable(List.of(user1, user2, user3));
	}

	// RxJava Type
	/**
	 * WORKING WITH RXJAVA TYPES It’s worth pointing out that although Reactor types
	 * like Flux and Mono are a natural choice when working with Spring WebFlux, you
	 * can also choose to work with RxJava types like Observable and Single.
	 * 
	 * @return
	 */
	@GetMapping("/iterableRx")
	public Observable<Employee> getEmpz() {
		// return employeeRepository.findAllEmployees();
		Employee user1 = new Employee("Hurma", "85");
		Employee user2 = new Employee("Enar", "47");
		Employee user3 = new Employee("Uzum", "53");

		return Observable.fromArray(user1, user2, user3);

	}

}
