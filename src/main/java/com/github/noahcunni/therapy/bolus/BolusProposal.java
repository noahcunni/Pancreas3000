package com.github.noahcunni.therapy.bolus;

import java.util.Random;

public class BolusProposal {
    public final int carbs;
    public final int bg;
    public final double insulin;

    public final int randomId;

    public BolusProposal(int carbs, int bg, double insulin) {
        this.carbs = carbs;
        this.bg = bg;
        this.insulin = insulin;

        Random rand = new Random();
        this.randomId = rand.nextInt(999999999);
    }

    public BolusProposal(int carbs, int bg, double insulin, int randomId) {
        this.carbs = carbs;
        this.bg = bg;
        this.insulin = insulin;
        this.randomId = randomId;
    }
}
