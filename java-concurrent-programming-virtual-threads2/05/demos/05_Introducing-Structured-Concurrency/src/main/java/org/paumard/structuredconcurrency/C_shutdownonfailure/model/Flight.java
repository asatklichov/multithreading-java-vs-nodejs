package org.paumard.structuredconcurrency.C_shutdownonfailure.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;

public record Flight(String from, String to, int price, String airline) {

    private static Random random = new Random();

    public static class FlightException extends RuntimeException {
    }

    private static class FlightScope extends StructuredTaskScope<Flight> {

        private volatile Collection<Flight> flights = new ConcurrentLinkedQueue<>();
        private volatile Collection<Throwable> exceptions = new ConcurrentLinkedQueue<>();

        @Override
        protected void handleComplete(Subtask<? extends Flight> subtask) {
            switch (subtask.state()) {
                case UNAVAILABLE -> throw new IllegalStateException("Task should be done");
                case SUCCESS -> this.flights.add(subtask.get());
                case FAILED -> this.exceptions.add(subtask.exception());
            }
        }

        public FlightException exceptions() {
            FlightException flightException = new FlightException();
            this.exceptions.forEach(flightException::addSuppressed);
            return flightException;
        }

        public Flight bestFlight() {
            return this.flights.stream()
                  .min(Comparator.comparingInt(Flight::price))
                  .orElseThrow(this::exceptions);
        }
    }

    public static Flight readFlight(String from, String to) {

        FlightQuery query = new FlightQuery(from, to);

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
        if (from.equals("New York")) {
            throw null;
        }
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
