package com.github.noahcunni.therapy.bolus;

public class BolusProposal {
    public final int carbs;
    public final int bg;
    public final double insulin;

    public BolusProposal(int carbs, int bg, double insulin) {
        this.carbs = carbs;
        this.bg = bg;
        this.insulin = insulin;
    }
}
