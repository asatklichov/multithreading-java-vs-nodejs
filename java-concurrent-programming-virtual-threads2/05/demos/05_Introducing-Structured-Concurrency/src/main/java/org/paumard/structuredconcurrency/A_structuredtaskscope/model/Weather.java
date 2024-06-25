package org.paumard.structuredconcurrency.A_structuredtaskscope.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.StructuredTaskScope;

public record Weather(String weather, String server) {

    private static Random random = new Random();

    private enum WeatherForecast {
        Sunny, Rainy, Cloudy
    }

    public static Weather readWeather() {

        try (var scope = new StructuredTaskScope<Weather>()) {

            var f1 = scope.fork(Weather::readFromInternationalWF);
            var f2 = scope.fork(Weather::readFromGlobalWF);
            var f3 = scope.fork(Weather::readFromPlanetEarthWeatherForecast);

            scope.join();

            System.out.println("F1: " + f1.state());
            if (f1.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
                System.out.println(" " + f1.get());
            }
            System.out.println("F2: " + f2.state());
            if (f2.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
                System.out.println(" " + f2.get());
            }
            System.out.println("F3: " + f3.state());
            if (f3.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
                System.out.println(" " + f3.get());
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return readFromInternationalWF();
    }

    public static Weather readFromInternationalWF() {
        sleepFor(random.nextInt(70, 120), ChronoUnit.MILLIS);
        return new Weather(
                WeatherForecast.values()[random.nextInt(0, 3)].name(),
                "International Weather Forecast");
    }

    public static Weather readFromGlobalWF() {
        // throw null;
        sleepFor(random.nextInt(80, 100), ChronoUnit.MILLIS);
        return new Weather(
                WeatherForecast.values()[random.nextInt(0, 3)].name(),
                "Global Weather Forecast");
    }

    public static Weather readFromPlanetEarthWeatherForecast() {
        sleepFor(random.nextInt(80, 110), ChronoUnit.MILLIS);
        return new Weather(
                WeatherForecast.values()[random.nextInt(0, 3)].name(),
                "Planet Earth Weather Forecast");
    }

    private static void sleepFor(int amount, ChronoUnit unit) {
        try {
            Thread.sleep(Duration.of(amount, unit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
