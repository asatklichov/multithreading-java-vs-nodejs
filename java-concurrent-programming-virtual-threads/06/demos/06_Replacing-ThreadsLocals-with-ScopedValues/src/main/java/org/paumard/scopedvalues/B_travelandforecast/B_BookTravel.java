package org.paumard.scopedvalues.B_travelandforecast;


import org.paumard.scopedvalues.B_travelandforecast.model.Page;
import org.paumard.scopedvalues.B_travelandforecast.model.travel.Travel;
import org.paumard.scopedvalues.B_travelandforecast.model.travel.TravelQuery;
import org.paumard.scopedvalues.B_travelandforecast.model.weather.Weather;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.StructuredTaskScope;

public class B_BookTravel {

    public static final ScopedValue<String> licenceKey = ScopedValue.newInstance();

    public static void main(String[] args) {

        String from = "New York";
        String via = "Atlanta";
        String to = "San Francisco";

        TravelQuery travelQuery0 = new TravelQuery(from, to);
        TravelQuery travelQuery1 = new TravelQuery(from, via);
        TravelQuery travelQuery2 = new TravelQuery(via, to);

        var begin = Instant.now();

        ScopedValue.where(licenceKey, "KEY_1")
              .run(() -> {
                  Page page = readPage(travelQuery0, travelQuery1, travelQuery2);
                  System.out.println("page = " + page);
              });

//        Page page = readPage(travelQuery0, travelQuery1, travelQuery2);
//        System.out.println("page = " + page);

        var end = Instant.now();
        System.out.println("Done in " + Duration.between(begin, end).toMillis() + " ms");
    }

    private static Page readPage(TravelQuery travelQuery0, TravelQuery travelQuery1, TravelQuery travelQuery2) {
        try (var scope = new StructuredTaskScope<>()) {

            var task1 = scope.fork(
                  () -> Weather.readWeather());
            var task2 = scope.fork(
                  () -> Travel.readTravel(travelQuery0, travelQuery1, travelQuery2));

            scope.join();

            var weather = task1.get();
            var travel = task2.get();

            Page page = new Page(travel, weather);
            return page;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
