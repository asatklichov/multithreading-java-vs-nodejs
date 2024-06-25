package org.paumard.structuredconcurrency.C_shutdownonfailure.model;

import java.util.concurrent.StructuredTaskScope;

public record MultiLegFlight(String from, String via, String to, int price, String airline) {

    public static MultiLegFlight of(Flight flight1, Flight flight2) {
        return new MultiLegFlight(flight1.from(), flight1.to(), flight2.to(),
              flight1.price() + flight2.price(), flight1.airline());
    }

    public static MultiLegFlight readMultiLegFlight(FlightQuery flightQuery1, FlightQuery flightQuery2) {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            var t1 = scope.fork(flightQuery1::readFromAlphaAirlines);
            var t2 = scope.fork(flightQuery2::readFromAlphaAirlines);

            scope.join();

            MultiLegFlight travel = of(t1.get(), t2.get());
            return travel;

        } catch (InterruptedException | IllegalStateException e) {
//            throw new RuntimeException(e);
            return null;
        }
    }
}
