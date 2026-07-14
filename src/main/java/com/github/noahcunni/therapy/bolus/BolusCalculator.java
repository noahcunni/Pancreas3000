package com.github.noahcunni.therapy.bolus;

import com.github.noahcunni.TherapySettings;
import com.github.noahcunni.therapy.BolusRequest;

public class BolusCalculator {
    private final TherapySettings settings;
    public BolusCalculator(TherapySettings settings) {
        this.settings = settings;
    }

    public BolusProposal preview(BolusRequest req, double IOB) {
        return null;
    }
}