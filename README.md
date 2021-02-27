# Getting Started

This application is used to test multithreading aspects in Java and Node.js. 
There are multiple examples, some of them solved in multiple ways in Java and in Node.js
and as a result we have a measurement table with respective outputs for comparison in the multithreading-java-vs-nodejs.pdf. 
Also there is a **medium article** [Multithreading in Java vs Node.JS](https://medium.com/modern-mainframe/multithreading-in-java-vs-node-js-c558d59050c9) 
which describes multithreading  [in Java](https://docs.oracle.com/javase/8/docs/technotes/guides/concurrency/index.html )  and    [Node.js](https://medium.com/@mohllal/node-js-multithreading-a5cd74958a67 )  

Examples
- Then there are multiple Java-main applications, to test core and enhanced Java concurrency features
- There are synchronous, asynchronous and asynchronous examples using parallelism examples build onto Java 11 http2 features
- There is a SpringBoot application, where you can see @Async feature using Java CompletableFuture
- For Reactive programming there are pub-sub examples and custom server implementation, which is a unique example 
- Finally in node.js app there are multiple synch and async examples 

# Installation
- For Spring application just execute:  run.bat
- For Node.js just execute: npm ci
- Other examples are just via plain java main method
 
# Testing


### Java Thread Core API  
- Run main methods under 'concurrency.java.core.api' package to see 
core Java multithreading API examples e.g. deadlock, or race-condition examples, debug it to deep dive  

### Java Concurrency API 

- Run main methods under 'concurrency.java.concurrent.api' package to see 
Java concurrency API examples e.g. different locks, executors, producer-consumers, .. 

### Java Enhanced Concurrency - CompletableFuture with Java 11 http2 client

- See concurrency.completablefuture.api package to understand CompletableFuture
- Run main methods under 'concurrency.completablefuture.java11.httpclient' package to see 
synchronous, asynchronous and asynchronous examples using parallelism examples build onto Java 11 http2 features 

### SpringBoot - @Async with CompletableFuture

- Run: mvn spring-boot:run and look the console output for async test,
- For sync example use: http://localhost:8080/findAll?userNames=PivotalSoftware,CloudFoundry,Spring-Projects,asatklichov


### Java Enhanced Concurrency - Reactive Programming with Flow API, custom http-server

- Run Java9SubmissionPublisherDemo &  Java9FlowableStreamDemo to play with Reactive Programming basics which came with Java 9 Flow API  
- Run AkkaPubSubDemo.java to play with akka reactive library 
- Run RxJavaBackpressureDemo.java to play with rxjava reactive library 
- Bonus: Run custom http-server server: MySimpleHttpServer.java then run either java-client MyHttpClient 
or request it via browser: http://localhost:8000/serve



### Node.js 
After you have installed the dependencies, described as above, just run like below examples. 
Also you can change the parameters or input file accordingly. 
- Sync:  multithreading-java-vs-nodejs\nodejs-app\src>node validate-domain-async3.js
- Async: multithreading-java-vs-nodejs\nodejs-app\src>node validate-domain-sync.js
- Async Axios: multithreading-java-vs-nodejs\nodejs-app\src>node validate-domain-axios.js
- Async Got: multithreading-java-vs-nodejs\nodejs-app\src>node validate-domain-got.js
- Worker: multithreading-java-vs-nodejs\nodejs-app\src>node worker-parent.js
