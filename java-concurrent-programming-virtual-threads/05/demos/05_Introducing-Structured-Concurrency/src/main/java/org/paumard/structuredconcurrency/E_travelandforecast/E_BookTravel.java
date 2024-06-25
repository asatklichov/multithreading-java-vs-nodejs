package org.paumard.structuredconcurrency.E_travelandforecast;


import org.paumard.structuredconcurrency.E_travelandforecast.model.Page;
import org.paumard.structuredconcurrency.E_travelandforecast.model.travel.Travel;
import org.paumard.structuredconcurrency.E_travelandforecast.model.travel.TravelQuery;
import org.paumard.structuredconcurrency.E_travelandforecast.model.weather.Weather;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.StructuredTaskScope;

public class E_BookTravel {

    public static void main(String[] args) {

        String from = "New York";
        String via = "Atlanta";
        String to = "San Francisco";

        TravelQuery travelQuery0 = new TravelQuery(from, to);
        TravelQuery travelQuery1 = new TravelQuery(from, via);
        TravelQuery travelQuery2 = new TravelQuery(via, to);

        var begin = Instant.now();

        try (var scope = new StructuredTaskScope<>()) {

            var task1 = scope.fork(
                  () -> Weather.readWeather());
            var task2 = scope.fork(
                  () -> Travel.readTravel(travelQuery0, travelQuery1, travelQuery2));

            scope.join();

            var weather = task1.get();
            var travel = task2.get();

            Page page = new Page(travel, weather);
            System.out.println("page = " + page);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        var end = Instant.now();
        System.out.println("Done in " + Duration.between(begin, end).toMillis() + " ms");
    }
}
