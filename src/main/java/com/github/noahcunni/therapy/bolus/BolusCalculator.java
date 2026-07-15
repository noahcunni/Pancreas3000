package com.github.noahcunni.therapy.bolus;

import com.github.noahcunni.TherapySettings;
import com.github.noahcunni.therapy.BolusRequest;

public class BolusCalculator {
    private final TherapySettings settings;
    public BolusCalculator(TherapySettings settings) {
        this.settings = settings;
    }

    public BolusProposal calculate(BolusRequest req, double IOB) {
        if (req.carbs != -1) {
            double carbDose = req.carbs / settings.INSULIN_TO_CARB_RATIO;
            double correction = 
                req.bg > settings.CORRECT_ABOVE_GLUCOSE_IN_CALCULATOR 
                ? (req.bg - settings.TARGET_GLUCOSE_IN_CALCULATOR) / settings.CORRECTION_FACTOR
                : 0.0;
            double proposed = carbDose + Math.max(0.0, correction - IOB);
            return new BolusProposal(req.carbs, req.bg, proposed);
        }

        // Manual insulin injection
        if (req.carbs == -1 && req.insulin >= 0.05) {
            System.out.println("MANUAL INJECTION");

            return new BolusProposal(req.carbs, req.bg, req.insulin);
        }

        return null;
    }
}