package org.paumard.structuredconcurrency.D_puttingitalltogether;


import org.paumard.structuredconcurrency.D_puttingitalltogether.model.*;

import java.time.Duration;
import java.time.Instant;

public class D_BookMultiLegFlight {

    public static void main(String[] args) {

        String from = "New York";
        String via = "Atlanta";
        String to = "San Francisco";

        TravelQuery travelQuery0 = new TravelQuery(from, to);
        TravelQuery travelQuery1 = new TravelQuery(from, via);
        TravelQuery travelQuery2 = new TravelQuery(via, to);

        var begin = Instant.now();
        Travel.readTravel(travelQuery1, travelQuery2, travelQuery0);
        var end = Instant.now();
        System.out.println("Done in " + Duration.between(begin, end).toMillis() + " ms");
    }

}
