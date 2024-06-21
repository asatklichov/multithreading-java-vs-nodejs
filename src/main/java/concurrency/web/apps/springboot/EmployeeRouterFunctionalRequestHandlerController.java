package concurrency.web.apps.springboot;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * To demonstrate how the functional programming model might be used in a
 * real world application, let’s reinvent the functionality of
 * 
 * @see EmployeeReactiveController in the functional style.
 * 
 *      Defining functional request handlers
 * 
 * 
 *      Spring MVC’s annotation-based programming model has been around since
 *      Spring 2.5 and is widely popular. It comes with a few downsides,
 *      however. First, any annotation-based programming involves a split in the
 *      definition of what the annotation is supposed to do and how it’s
 *      supposed to do it. Annotations themselves define the what; the how is
 *      defined elsewhere in the framework code. This complicates the
 *      programming model when it comes to any sort of customization or
 *      extension because such changes require working in code that’s external
 *      to the annotation. Moreover, debugging such code is tricky because you
 *      can’t set a breakpoint on an annotation.
 * 
 *      As an alternative to WebFlux, Spring 5 has introduced a new functional
 *      programming model for defining reactive APIs. This new programming model
 *      is used more like a library and less like a framework, letting you map
 *      requests to handler code without annotations.
 * 
 *      Writing an API using Spring’s functional programming model involves four
 *      primary types:
 * 
 *      RequestPredicate, RouterFunction, ServerRequest, ServerResponse
 * 
 *      <pre>
	 *RequestPredicate—Declares the kind(s) of requests that will be handled
	 *
	  RouterFunction—Declares how a matching request should be routed to handler
	code
	
	 ServerRequest—Represents an HTTP request, including access to header and
	body information
	
    ServerResponse—Represents an HTTP response, including header and body
	information
 *      </pre>
 */

@Configuration
public class EmployeeRouterFunctionalRequestHandlerController {

	// private final EmployeeRepository employeeRepository; TODO

	/**
	 * The first thing to notice is that you’ve chosen to statically import a few
	 * helper classes that you can use to create the aforementioned functional
	 * types. You’ve also statically imported Mono to keep the rest of the code
	 * easier to read and understand.
	 * 
	 * In this @Configuration class, you have a single @Bean method of type Router-
	 * Function<?>. As mentioned, a RouterFunction declares mappings between one or
	 * more RequestPredicate objects and the functions that will handle the matching
	 * request(s).
	 * 
	 * 
	 * @return
	 */
	@Bean
	public RouterFunction<?> helloRouterFunction() {
		/**
		 * 
		 * The route() method from RouterFunctions accepts two parameters: a Request-
		 * Predicate and a function to handle matching requests.
		 * 
		 * In this case, the GET() method from RequestPredicates declares a
		 * RequestPredicate that matches HTTP GET requests for the /helloRouter path.
		 * 
		 * As for the handler function, it’s written as a lambda, although it can also
		 * be a method reference. Although it isn’t explicitly declared, the handler
		 * lambda accepts a ServerRequest as a parameter.
		 * 
		 * It returns a ServerResponse using ok() from Server- Response and body() from
		 * BodyBuilder, which was returned from ok().
		 */
//		return route(GET("/helloRouter"), request -> ok().body(just(
//				"Hello Spring’s functional programming model -  RequestPredicate, RouterFunction, ServerRequest, ServerResponse"),
//				String.class));

		/**
		 * As written, the helloRouterFunction() method declares a RouterFunction that
		 * only handles a single kind of request. But if you need to handle a different
		 * kind of request, you don’t have to write another @Bean method, although you
		 * can. You only need to call andRoute() to declare another
		 * RequestPredicate-to-function mapping. For example, here’s how you might add
		 * another handler for GET requests for /bye:
		 */
		return route(GET("/helloRouter"), request -> ok().body(just(
				"Hello Spring’s functional programming model -  RequestPredicate, RouterFunction, ServerRequest, ServerResponse"),
				String.class)).andRoute(GET("/bye"), request -> ok().body(just("See ya!"), String.class));
	}

	// http://localhost:9999/funEmp
	@Bean
	public RouterFunction<?> routerFunction() {
		return route(GET("/funEmp"), this::emp);
	}

	public Mono<ServerResponse> emp(ServerRequest request) {
		Employee user = new Employee("Alma", request.pathVariable("id"));
		return ServerResponse.ok().body(Mono.just(user), Employee.class);
	}

}
