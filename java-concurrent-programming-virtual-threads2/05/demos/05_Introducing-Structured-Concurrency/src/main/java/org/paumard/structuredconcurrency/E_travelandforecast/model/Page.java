package org.paumard.structuredconcurrency.E_travelandforecast.model;

import org.paumard.structuredconcurrency.E_travelandforecast.model.travel.Travel;
import org.paumard.structuredconcurrency.E_travelandforecast.model.weather.Weather;

public record Page(Travel travel, Weather weather) {

}
