package com.github.noahcunni.therapy.bolus;

import com.github.noahcunni.TherapySettings;

public class BolusSafety {
    private final TherapySettings settings;

    public BolusSafety(TherapySettings settings) {
        this.settings = settings;
    }
   
    public SafetyDecision preview(BolusRequest req) {
        String reason = ""; 
        boolean approved = true;

        // Is request under carb limit?
        if (req.carbs > settings.MAX_CARBS_PER_MEAL || req.carbs < 0) {
            approved = false;
            reason = "CARBS MUST BE BETWEEN 0 AND " +
                settings.MAX_CARBS_PER_MEAL;
        }

        // Is request under bg limit?
        if (req.bg <= settings.MIN_GLUCOSE_FOR_CALCULATION) {
            approved = false;
            reason = "USER BLOOD GLUCOSE IS BELOW " + 
                settings.MIN_GLUCOSE_FOR_CALCULATION;
        }

        // Is request under insulin limit?
        if (req.insulin > settings.MAX_BOLUS_PER_MEAL) {
            approved = false; 
            reason = "INSULIN MUST BE BETWEEN 0.05 AND " +
                settings.MAX_BOLUS_PER_MEAL;
        }
        return new SafetyDecision(approved, reason, req.carbs, req.bg, req.insulin); 
    }   
}
