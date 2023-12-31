package concurrency.java.concurrent.api;

/**
 * 
 * Java Concurrency – Synchronizers
 * 
 * 
 * The java.util.concurrent package contains several classes that help manage a
 * set of threads that collaborate with each other. Some of these include:
 * 
 * CyclicBarrier,
 * 
 * Phaser,
 * 
 * CountDownLatch,
 * 
 * Exchanger,
 * 
 * Semaphore,
 * 
 * SynchronousQueue
 * 
 * 
 * 
 * AnatomyOfASynchronizer
 * 
 * 
 * see {@link ExchangerDemo} see {@link SemaphoresDemo}
 * 
 * 
 * https://jenkov.com/tutorials/java-concurrency/anatomy-of-a-synchronizer.html
 *
 * Even if many synchronizers (locks, semaphores, blocking queue etc.) are
 * different in function, they are often not that different in their internal
 * design. In other words, they consist of the same (or similar) basic parts
 * internally. Knowing these basic parts can be a great help when designing
 * synchronizers. It is these parts this text looks closer at.
 * 
 * Note: The content of this text is a part result of a M.Sc. student project at
 * the IT University of Copenhagen in the spring 2004 by Jakob Jenkov, Toke
 * Johansen and Lars Bj�rn. During this project we asked Doug Lea if he knew of
 * similar work. Interestingly he had come up with similar conclusions
 * independently of this project during the development of the Java 5
 * concurrency utilities. Doug Lea's work, I believe, is described in the book
 * "Java Concurrency in Practice". This book also contains a chapter with the
 * title "Anatomy of a Synchronizer" with content similar to this text, though
 * not exactly the same.
 */
public class Synchronizers {

}
