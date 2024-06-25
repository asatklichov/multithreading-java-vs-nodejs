package org.paumard.scopedvalues.B_travelandforecast.scope;

import org.paumard.scopedvalues.B_travelandforecast.model.travel.Flight;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;

public class FlightScope extends StructuredTaskScope<Flight> {

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