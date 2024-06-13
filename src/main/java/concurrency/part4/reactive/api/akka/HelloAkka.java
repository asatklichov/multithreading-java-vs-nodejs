package concurrency.part4.reactive.api.akka;

import akka.actor.AbstractActor;
import akka.actor.AbstractActor.Receive;

//https://www.baeldung.com/akka-actors-java
//https://www.baeldung.com/scala/akka-series
class MyActor extends AbstractActor {
	public Receive createReceive() {
		return receiveBuilder().build();
	}
}