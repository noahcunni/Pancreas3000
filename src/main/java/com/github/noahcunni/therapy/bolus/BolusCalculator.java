package com.github.noahcunni.therapy.bolus;

import com.github.noahcunni.TherapySettings;

public class BolusCalculator {
    private final TherapySettings settings;
    public BolusCalculator(TherapySettings settings) {
        this.settings = settings;
    }

    public DoseProposal calculateBolus() {
        return new DoseProposal(-1, -1);
    }
}