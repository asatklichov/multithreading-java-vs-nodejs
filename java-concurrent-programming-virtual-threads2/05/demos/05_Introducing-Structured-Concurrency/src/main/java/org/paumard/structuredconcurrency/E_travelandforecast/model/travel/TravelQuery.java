package org.paumard.structuredconcurrency.E_travelandforecast.model.travel;


public record TravelQuery(String from, String to) {

    public Flight readFromGlobalAirlines() {
        return Flight.readFromGlobalAirlines(from, to);
    }

    public Flight readFromPlanetAirlines() {
        return Flight.readFromPlanetAirlines(from, to);
    }

    public Flight readFromAlphaAirlines() {
        return Flight.readFromAlphaAirlines(from, to);
    }
}
