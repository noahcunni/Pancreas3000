package com.github.noahcunni.therapy.bolus;

import com.github.noahcunni.Pump;
import com.github.noahcunni.TherapySettings;
import com.github.noahcunni.therapy.BolusRequest;

public class BolusSafety {
    private final Pump pump;
    private final TherapySettings settings;
    public BolusSafety(Pump pump, TherapySettings settings) {
        this.pump = pump;
        this.settings = settings;
    }
   
    public SafetyDecision preview(BolusRequest req, double IOB) {
        return null; 
    }   

    public SafetyDecision review(BolusProposal proposal, double IOB) {
        return null;
    }
}
