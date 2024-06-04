package concurrency.part3.completablefuture.java11.httpclient;

/**
 * You can provide own custom CompletableFuture with other ExecutorService pool
 * or make your own pool, see many alternative here:
 * 
 * @link J_ExtendableCompletableFuture
 * 
 * @link G_CompletableFutureAsyncMethods_thenApplyAsync_completeAsync
 * 
 * @link ACustomizableThreadPools
 * 
 *       Custom Thread Pools:
 *       https://www.baeldung.com/java-8-parallel-streams-custom-threadpool
 * 
 */
class Z_CustomizingThreadPools {

}

/**
 * @link ZZZ_ConfigureTomcat
 */
class CustomizingThreadPools_ForWebServers {
	/**
	 *
	 * 
	 * <pre>
	 * * How do I define/instantiate the pool?
	 * 
	 * How do I access the pool? JNDI?
	 * 
	 * What management capabilities are provided by the app server?
	 * 
	 * I know how to implement an object pool in general. My question is mostly
	 * about creating a pool in a J2EE app server.
	 * Suppose I have a message driven bean (MDB) in a Java application server. The MDB receives a message from a JMS queue and passes it to a message processor. In my case, a message processor is an extremely heavy weight object that requires extensive initialization so I don't want to create a new one to handle each message. Instead I would like to create a pool of message processors ahead of time and use them to handle messages.
	
	So, my question is: what is the 'correct' way to build this pool in a J2EE app server? Do any servers have built-in support for defining custom (non-connection) object pools? I would like to leverage whatever built-in support there is for this pattern before I just cram the pool into a singleton and hope for the best. In particular:
	
	How do I define/instantiate the pool?
	How do I access the pool? JNDI?
	What management capabilities are provided by the app server?
	I know how to implement an object pool in general. My question is mostly about creating a pool in a J2EE app server.
	
	I'm planning on using Glassfish, but I"m flexible if JBoss or something else will make this easier.
	
	https://www.baeldung.com/java-web-thread-pool-config
	https://stackoverflow.com/questions/60931718/how-can-i-customize-spring-boot-embedded-tomcat-thread-pool
	
	Tomcat architecture is comprised of the following elements: Server => Service => Engine => Host => Context
	 * When configuring a standard Tomcat server, we can configure a custom thread pool by specifying the following in our server.xml file: (below is pseudo-code)
	 * 
	 * <Server>
		  <Service name="Catalina">
		    <Connector port="8080"/>
		    <Executor name="custom-pool" className=
	"my.package.poolImplementation" />
		    <Engine name="Catalina" defaultHost="localhost">  
		      <Here be more elements />
		    </Engine>
		  </Service>
		</Server>
	 * </pre>
	 * 
	 * (specifically, the Executor name="custom-pool"
	 * className="my.package.poolImplementation")
	 * 
	 * How do I configure Spring Boot to allow the same behaviour programmatically ?
	 * (WITHOUT using Spring configuration files):
	 * 
	 * 
	 * 
	 * TomcatServletWebServerFactory.java/ServletWebServerFactoryConfiguration.java)
	 * and found a way to do that.
	 * 
	 * @Bean public TomcatProtocolHandlerCustomizer<?>
	 *       tomcatProtocolHandlerCustomizer() { return protocolHandler -> {
	 *       protocolHandler.setExecutor(...); }; }
	 * 
	 */

	public static void main(String[] args) {
		/**
		 * Answers:
		 * 
		 * EJBs themselves are usually managed as pooled objects by most application
		 * servers.
		 * 
		 * 
		 * 
		 * 
		 * This is for Connection Pooling:
		 * https://www.baeldung.com/java-connection-pooling
		 * 
		 */
		// https://www.baeldung.com/java-web-thread-pool-config
		// omcatServletWebServerFactory.java/ServletWebServerFactoryConfiguration.java

	}

}
