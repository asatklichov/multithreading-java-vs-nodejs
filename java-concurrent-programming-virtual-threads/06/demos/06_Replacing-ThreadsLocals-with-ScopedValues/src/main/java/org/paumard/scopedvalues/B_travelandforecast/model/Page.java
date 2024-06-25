package org.paumard.scopedvalues.B_travelandforecast.model;

import org.paumard.scopedvalues.B_travelandforecast.model.travel.Travel;
import org.paumard.scopedvalues.B_travelandforecast.model.weather.Weather;

public record Page(Travel travel, Weather weather) {

}
