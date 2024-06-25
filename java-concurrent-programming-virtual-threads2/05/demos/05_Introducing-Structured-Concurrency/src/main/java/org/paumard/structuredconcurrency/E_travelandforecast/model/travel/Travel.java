package org.paumard.structuredconcurrency.E_travelandforecast.model.travel;

import org.paumard.structuredconcurrency.E_travelandforecast.scope.TravelScope;

public interface Travel {


    int price();

    String from();

    String to();

    String airline();

    static Travel readTravel(TravelQuery travelQuery0, TravelQuery travelQuery1, TravelQuery travelQuery2) {
        try (var scope = new TravelScope()) {

            scope.fork(() -> MultiLegFlight.readMultiLegFlight(travelQuery1, travelQuery2));
            scope.fork(() -> Flight.readFlight(travelQuery0));

            scope.join();

            return scope.bestTravel();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
