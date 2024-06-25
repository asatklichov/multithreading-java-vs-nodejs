package org.paumard.structuredconcurrency.E_travelandforecast.scope;

import org.paumard.structuredconcurrency.E_travelandforecast.model.travel.Travel;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;

public class MultiLegFlightScope extends StructuredTaskScope<Travel> {

    private volatile Collection<Travel> flights = new ConcurrentLinkedQueue<>();
    private volatile Collection<Throwable> exceptions = new ConcurrentLinkedQueue<>();


    @Override
    protected void handleComplete(Subtask<? extends Travel> subtask) {
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

    public Travel bestTravel() {
        return this.flights.stream()
              .min(Comparator.comparingInt(Travel::price))
              .orElseThrow(this::exceptions);
    }
}
