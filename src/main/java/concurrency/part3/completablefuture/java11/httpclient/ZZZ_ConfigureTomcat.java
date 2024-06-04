package concurrency.part3.completablefuture.java11.httpclient;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardThreadExecutor;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

public class ZZZ_ConfigureTomcat {

}

@Component
class TomcatServerConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	private final StandardThreadExecutor customExecutor;

	public TomcatServerConfig() {
		this.customExecutor = new MyHTTPThreadExecutor();
	}

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		/* This web server is the Tomcat server embedded in Spring Boot */
		TomcatWebServer webServer = (TomcatWebServer) factory.getWebServer();
		webServer.getTomcat().getService().addExecutor(this.customExecutor);
	}

}

/**
 * similar code needs to be written for the Tomcat Connectors
 */
@Component
class TomcatConnectorConfig implements TomcatConnectorCustomizer {
	private final StandardThreadExecutor customExecutor;

	public TomcatConnectorConfig() {
		this.customExecutor = new MyHTTPThreadExecutor();
	}

	@Override
	public void customize(Connector connector) {
		connector.getProtocolHandler().setExecutor(this.customExecutor);
	}
}

/**
 * You can then set different properties of the server, service, engine,
 * connector. From the object service you can also access the executor and
 * change i
 */
@Component
class TomcatCustomizer extends TomcatServletWebServerFactory {
	@Override
	protected void postProcessContext(Context context) {
		Engine engine = (Engine) context.getParent().getParent();
		Service service = engine.getService();
		Server server = service.getServer();
		Connector connector = service.findConnectors()[0];
	}
}

class MyHTTPThreadExecutor extends StandardThreadExecutor {
	public MyHTTPThreadExecutor() {
		super();

		/* Custom constructor code here */
	}

	@Override
	protected void startInternal() throws LifecycleException {
		super.startInternal();

		/* Any work you need done upon startup */
	}
}