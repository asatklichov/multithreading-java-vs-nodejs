package org.paumard.scopedvalues.A_scopedvalues;

import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;

public class A_UsingScopedValues {

    public static void main(String[] args) throws InterruptedException {


        ScopedValue<String> scopedValue = ScopedValue.newInstance();

        Callable<String> task = () -> {
            if (scopedValue.isBound()) {
                System.out.println("Scoped value bound to " + scopedValue.get());
                return scopedValue.get();
            } else {
                System.out.println("Scoped value not bound");
                return "Unbound";
            }
        };


        ScopedValue.where(scopedValue, "KEY")
                    .run(() -> {
                        try (var scope = new StructuredTaskScope<String>()) {
                            scope.fork(task);
                            scope.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
    }
}
