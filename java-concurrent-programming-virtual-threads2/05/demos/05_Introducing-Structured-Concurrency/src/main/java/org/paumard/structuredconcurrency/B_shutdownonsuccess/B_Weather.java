package org.paumard.structuredconcurrency.B_shutdownonsuccess;


import org.paumard.structuredconcurrency.B_shutdownonsuccess.model.Weather;

public class B_Weather {

    // --enable-preview
    public static void main(String[] args) {

        var weather = Weather.readWeather();
        System.out.println("weather = " + weather);
    }
}













