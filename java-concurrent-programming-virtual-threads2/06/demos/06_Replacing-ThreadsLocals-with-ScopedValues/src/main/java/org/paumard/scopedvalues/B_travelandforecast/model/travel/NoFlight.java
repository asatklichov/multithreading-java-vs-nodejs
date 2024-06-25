package org.paumard.scopedvalues.B_travelandforecast.model.travel;

public class NoFlight implements Travel {
    public int price() {
        return 0;
    }

    public String from() {
        return null;
    }

    public String to() {
        return null;
    }

    public String airline() {
        return null;
    }
}
