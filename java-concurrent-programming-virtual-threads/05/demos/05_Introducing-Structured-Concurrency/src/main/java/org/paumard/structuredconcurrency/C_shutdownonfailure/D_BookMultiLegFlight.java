package org.paumard.structuredconcurrency.C_shutdownonfailure;

import org.paumard.structuredconcurrency.C_shutdownonfailure.model.FlightQuery;
import org.paumard.structuredconcurrency.C_shutdownonfailure.model.MultiLegFlight;

import java.time.Duration;
import java.time.Instant;

public class D_BookMultiLegFlight {

    public static void main(String[] args) {

        String from = "New York";
        String via = "Atlanta";
        String to = "San Francisco";

        var start = Instant.now();
        FlightQuery flightQuery1 = new FlightQuery(from, via);
        FlightQuery flightQuery2 = new FlightQuery(via, to);

        MultiLegFlight travel = MultiLegFlight.readMultiLegFlight(flightQuery1, flightQuery2);

        var end = Instant.now();
        System.out.println("Flight = " + travel +
              " in " + Duration.between(start, end).toMillis());

    }

}
