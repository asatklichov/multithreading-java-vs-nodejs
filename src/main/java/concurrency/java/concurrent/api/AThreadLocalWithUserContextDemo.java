package concurrency.java.concurrent.api;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AThreadLocalWithUserContextDemo {

	public void whenStoringUserDataInAMap_thenCorrect() throws IOException {
		/**
		 * We can easily test our code by creating and starting two threads for two
		 * different userIds and asserting that we have two entries in the
		 * userContextPerUserId map:
		 */
		SharedMapWithUserContext firstUser = new SharedMapWithUserContext();
		SharedMapWithUserContext secondUser = new SharedMapWithUserContext();
		new Thread(firstUser).start();
		new Thread(secondUser).start();

		// assertEquals(SharedMapWithUserContext.userContextPerUserId.size(), 2);
	}

	public void whenStoringUserDataInATreadLocal_thenCorrect() throws IOException, InterruptedException {

		ThreadLocalWithUserContext firstUser = new ThreadLocalWithUserContext();
		ThreadLocalWithUserContext secondUser = new ThreadLocalWithUserContext();
		Thread t1 = new Thread(firstUser);
		t1.start();
		t1.join();
		Thread t2 = new Thread(secondUser);
		t2.start();

		// assertEquals(ThreadLocalWithUserContext.userContext.size(), 2);
	}

}

class ThreadLocalWithUserContext implements Runnable {

	/**
	 * We can rewrite our example to store the user Context instance using a
	 * ThreadLocal. Each thread will have its own ThreadLocal instance.
	 * 
	 * When using ThreadLocal we need to be very careful because every ThreadLocal
	 * instance is associated with a particular thread. In our example, we have a
	 * dedicated thread for each particular userId and this thread is created by us
	 * so we have full control over it.
	 */
	private static ThreadLocal<Context> userContext = new ThreadLocal<>();
	private Integer userId;
	// private UserRepository userRepository = new UserRepository();

	@Override
	public void run() {
		userId = (int) (Math.random() * 100);
		String userName = "just4test " + userId; // String userName = userRepository.getUserNameForUserId(userId);
		userContext.set(new Context(userName));
		System.out.println("thread context for given userId: " + userId + " is: " + userContext.get());
	}

}

/**
 * Storing User Data in a Map
 * 
 * We want to have one thread per user id.
 * 
 * We'll create a SharedMapWithUserContext class that implements a Runnable
 * interface. The implementation in the run() method calls some database through
 * the UserRepository class that returns a Context object for a given userId.
 *
 */
class SharedMapWithUserContext implements Runnable {

	public static Map<Integer, Context> userContextPerUserId = new ConcurrentHashMap<>();
	private Integer userId;
	// private UserRepository userRepository = new UserRepository();

	@Override
	public void run() {
		userId = (int) (Math.random() * 100);
		String userName = "just4test " + userId; // userRepository.getUserNameForUserId(userId);
		userContextPerUserId.put(userId, new Context(userName));
		System.out.println("thread context for given userId: " + userId + " is: " + userContextPerUserId.get(userId));

	}

}

class Context {
	private String userName;

	public Context(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}

class ThreadLocalRandomDemo {
	// see https://www.baeldung.com/java-thread-local-random
}