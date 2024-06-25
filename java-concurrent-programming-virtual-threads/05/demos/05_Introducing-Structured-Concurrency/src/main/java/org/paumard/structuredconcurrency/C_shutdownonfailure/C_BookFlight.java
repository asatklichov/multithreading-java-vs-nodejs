package org.paumard.structuredconcurrency.C_shutdownonfailure;

import org.paumard.structuredconcurrency.C_shutdownonfailure.model.Flight;
import org.paumard.structuredconcurrency.C_shutdownonfailure.model.FlightQuery;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Stream;

public class C_BookFlight {

    public static void main(String[] args) {

        String from = "New York";
        String to = "San Francisco";

        var start = Instant.now();
        Flight bestFlight = Flight.readFlight(from, to);
        var end = Instant.now();

        System.out.println("Best flight = " + bestFlight +
              " in " + Duration.between(start, end).toMillis());

    }
}
