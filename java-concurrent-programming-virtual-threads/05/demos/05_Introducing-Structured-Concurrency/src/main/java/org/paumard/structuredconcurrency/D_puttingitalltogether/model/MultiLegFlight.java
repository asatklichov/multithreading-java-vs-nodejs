package org.paumard.structuredconcurrency.D_puttingitalltogether.model;

import org.paumard.structuredconcurrency.D_puttingitalltogether.scope.MultiLegFlightScope;

import java.util.concurrent.StructuredTaskScope;

public record MultiLegFlight(String from, String via, String to, int price, String airline)
      implements Travel {

    public static MultiLegFlight of(Flight flight1, Flight flight2) {
        return new MultiLegFlight(flight1.from(), flight1.to(), flight2.to(),
              flight1.price() + flight2.price(), flight1.airline());
    }

    public static Travel readMultiLegFlight(TravelQuery travelQuery1, TravelQuery travelQuery2) {

        try (var scope = new MultiLegFlightScope()) {

            scope.fork(() -> readFromGlobalAirLines(travelQuery1, travelQuery2));
            scope.fork(() -> readFromAlphaAirLines(travelQuery1, travelQuery2));
            scope.fork(() -> readFromPlanetAirLines(travelQuery1, travelQuery2));

            scope.join();

            return scope.bestTravel();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Travel readFromAlphaAirLines(TravelQuery query1, TravelQuery query2) {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            var t1 = scope.fork(query1::readFromAlphaAirlines);
            var t2 = scope.fork(query2::readFromAlphaAirlines);

            scope.join();

            return of(t1.get(), t2.get());

        } catch (InterruptedException | IllegalStateException e) {
//            throw new RuntimeException(e);
            return new NoFlight();
        }
    }

    public static Travel readFromGlobalAirLines(TravelQuery query1, TravelQuery query2) {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            var t1 = scope.fork(query1::readFromGlobalAirlines);
            var t2 = scope.fork(query2::readFromGlobalAirlines);

            scope.join();

            MultiLegFlight travel = of(t1.get(), t2.get());
            return travel;

        } catch (InterruptedException | IllegalStateException e) {
//            throw new RuntimeException(e);
            return new NoFlight();
        }
    }

    public static Travel readFromPlanetAirLines(TravelQuery query1, TravelQuery query2) {

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            var t1 = scope.fork(query1::readFromPlanetAirlines);
            var t2 = scope.fork(query2::readFromPlanetAirlines);

            scope.join();

            MultiLegFlight travel = of(t1.get(), t2.get());
            return travel;

        } catch (InterruptedException | IllegalStateException e) {
//            throw new RuntimeException(e);
            return new NoFlight();
        }
    }
}
