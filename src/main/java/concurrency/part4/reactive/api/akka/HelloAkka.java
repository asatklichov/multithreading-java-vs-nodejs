package concurrency.part4.reactive.api.akka;

import akka.actor.AbstractActor;
import akka.actor.UntypedAbstractActor;

//https://www.baeldung.com/akka-actors-java
//https://www.baeldung.com/scala/akka-series
//https://fractus-io.github.io/tutorials/akka/helloworld/
public class HelloAkka extends UntypedAbstractActor {

	private int count = 0;

	public void onReceive(Object message) throws Exception {
		count++;
		System.out.println("Received message: " + message + " ! My Count is now: " + Integer.toString(count));
	}
}

class MyActor extends AbstractActor {
	public Receive createReceive() {
		return receiveBuilder().build();
	}
}