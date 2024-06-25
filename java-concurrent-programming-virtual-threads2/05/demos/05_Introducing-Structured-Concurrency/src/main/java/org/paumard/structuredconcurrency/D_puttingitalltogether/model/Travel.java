package org.paumard.structuredconcurrency.D_puttingitalltogether.model;

import org.paumard.structuredconcurrency.D_puttingitalltogether.scope.TravelScope;

public interface Travel {


    int price();

    String from();

    String to();

    String airline();

    static void readTravel(TravelQuery travelQuery1, TravelQuery travelQuery2, TravelQuery travelQuery0) {
        try (var scope = new TravelScope()) {

            scope.fork(() -> MultiLegFlight.readMultiLegFlight(travelQuery1, travelQuery2));
            scope.fork(() -> Flight.readFlight(travelQuery0));

            scope.join();

            var bestTravel = scope.bestTravel();

            System.out.println("bestTravel = " + bestTravel);


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
