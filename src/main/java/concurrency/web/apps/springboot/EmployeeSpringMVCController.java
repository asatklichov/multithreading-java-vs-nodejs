package concurrency.web.apps.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employeez")
public class EmployeeSpringMVCController {

	// private final EmployeeRepository employeeRepository; TODO

	@GetMapping("/{id}")
	public Employee getEmployeeById(@PathVariable String id) {
		// return employeeRepository.findEmployeeById(id);
		Employee user = new Employee("Alma", id);

		return user;
	}

}
