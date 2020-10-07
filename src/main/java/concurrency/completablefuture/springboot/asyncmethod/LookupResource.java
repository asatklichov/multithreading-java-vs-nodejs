package concurrency.completablefuture.springboot.asyncmethod;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LookupResource {

	@Autowired
	private LookupService lookupService;

	/**
	 * PivotalSoftware, CloudFoundry, Spring-Projects ,asatklichov, teverett,
	 * johnpapa ,LSP ,DAP, code4z
	 * 
	 * 
	 * http://localhost:8080/findAll?userNames=PivotalSoftware,CloudFoundry,Spring-Projects,asatklichov,teverett,johnpapa,LSP,DAP,code4z
	 * 
	 * @param userNames
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	public List<User> findAllUsers(@RequestParam List<String> userNames) {

		long start = System.currentTimeMillis();
		log.info("Time start: " + new Date(System.currentTimeMillis()));
		List<User> users = lookupService.findAllByUserNames(userNames);

		if (users == null || users.isEmpty()) {
			throw new UserNotFoundException();
		}

		log.info("Elapsed time: " + (System.currentTimeMillis() - start));
		return users;

	}

	@ResponseBody
	@RequestMapping(value = "/findAllAsynch", method = RequestMethod.GET)
	public List<User> findAllUsersAsynch(@RequestParam List<String> userNames) throws InterruptedException {

		long start = System.currentTimeMillis();
		log.info("Time start: " + new Date(System.currentTimeMillis()));

		// CompletableFuture<List<User>>

		return (List<User>) lookupService.findUsers(userNames).<ResponseEntity>thenApply(ResponseEntity::ok)
				.exceptionally(handleGetCarFailure);

	}

	private static Function<Throwable, ResponseEntity<? extends List<User>>> handleGetCarFailure = throwable -> {
		log.error("Failed to read records: {}", throwable);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	};

	/**
	 * http://localhost:8080/findAll?userNames=PivotalSoftware,CloudFoundry,Spring-Projects,asatklichov,teverett,johnpapa,LSP,DAP,code4z
	 * 
	 * 
	 * @param userNames
	 * @return
	 */
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public String getAllUsers(@RequestParam List<String> userNames, Model model) {

		long start = System.currentTimeMillis();
		log.info("Time start: " + new Date(System.currentTimeMillis()));
		List<User> users = lookupService.findAllByUserNames(userNames);

		log.info("Elapsed time: " + (System.currentTimeMillis() - start));

		model.addAttribute("users", users);

		return "users";

	}

	/*
	 * http://localhost:8080/findUser?userName=code4z
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/findUser", method = RequestMethod.GET)
	public User getUser(
			@RequestParam(name = "userName", required = false, defaultValue = "asatklichov") String userName) {

		long start = System.currentTimeMillis();
		log.info("Time start: " + new Date(System.currentTimeMillis()));
		List<User> users = lookupService.findAllByUserNames(List.of(userName));

		if (users == null || users.isEmpty()) {
			throw new UserNotFoundException();
		}

		log.info("Elapsed time: " + (System.currentTimeMillis() - start));
		return users.get(0);

	}

}
