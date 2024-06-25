package org.paumard.virtualthread;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class B2_PlayWithContinuation {



    // --enable-preview --add-exports java.base/jdk.internal.vm=ALL-UNNAMED
    public static void main(String[] args) {

        var scope = new ContinuationScope("My scope");
        var continuation = new Continuation(
                scope,
                () -> {
                    System.out.println("Running in a continuation");
                    Continuation.yield(scope);
                    System.out.println("After the call to yield");
                });

        System.out.println("Running in the main method");
        continuation.run();
        System.out.println("Back in the main method");
        continuation.run();
        System.out.println("Back again in the main method");

    }
}
