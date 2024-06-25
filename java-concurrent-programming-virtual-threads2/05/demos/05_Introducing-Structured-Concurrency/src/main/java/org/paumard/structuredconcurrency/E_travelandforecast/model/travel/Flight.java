package org.paumard.structuredconcurrency.E_travelandforecast.model.travel;

import org.paumard.structuredconcurrency.E_travelandforecast.scope.FlightScope;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public record Flight(String from, String to, int price, String airline)
      implements Travel {

    private static Random random = new Random();

    public static Flight readFlight(TravelQuery query) {

        try (var scope = new FlightScope()) {

            scope.fork(query::readFromAlphaAirlines);
            scope.fork(query::readFromGlobalAirlines);
            scope.fork(query::readFromPlanetAirlines);

            scope.join();

            return scope.bestFlight();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Flight readFromAlphaAirlines(String from, String to) {
        sleepFor(random.nextInt(80, 100), ChronoUnit.MILLIS);
        if (from.equals("Atlanta") || to.equals("Atlanta")) {
            return new Flight(from, to,
                  random.nextInt(30, 50),
                  "Alpha Air Lines");
        } else {
            return new Flight(from, to,
                  random.nextInt(70, 120),
                  "Alpha Air Lines");
        }
    }

    public static Flight readFromGlobalAirlines(String from, String to) {
//        if (to.equals("Atlanta")) {
//            throw null;
//        }
        sleepFor(random.nextInt(90, 110), ChronoUnit.MILLIS);
        if (from.equals("Atlanta") || to.equals("Atlanta")) {
            return new Flight(from, to,
                  random.nextInt(40, 50),
                  "Alpha Air Lines");
        } else {
            return new Flight(from, to,
                  random.nextInt(60, 90),
                  "Global Air Lines");
        }
    }

    public static Flight readFromPlanetAirlines(String from, String to) {
        sleepFor(random.nextInt(70, 120), ChronoUnit.MILLIS);
        if (from.equals("Atlanta") || to.equals("Atlanta")) {
            return new Flight(from, to,
                  random.nextInt(30, 50),
                  "Alpha Air Lines");
        } else {
            return new Flight(from, to,
                  random.nextInt(70, 90),
                  "Planet Air Lines");
        }
    }

    private static void sleepFor(int amount, ChronoUnit unit) {
        try {
            Thread.sleep(Duration.of(amount, unit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
