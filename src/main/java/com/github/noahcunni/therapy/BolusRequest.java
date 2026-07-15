package com.github.noahcunni.therapy;

public class BolusRequest {
    public final int carbs;
    public final int bg;
    public final double insulin;

    public BolusRequest(int carbs, int bg, double insulin) {
        this.carbs = carbs;
        this.bg = bg;
        this.insulin = insulin;
    }
}
