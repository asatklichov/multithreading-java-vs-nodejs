package org.paumard.structuredconcurrency.A_structuredtaskscope;

import org.paumard.structuredconcurrency.A_structuredtaskscope.model.Weather;

import java.util.concurrent.StructuredTaskScope;

public class A_Weather {

    // --enable-preview
    public static void main(String[] args) {

        var weather = Weather.readWeather();
        System.out.println("weather = " + weather);
    }
}













