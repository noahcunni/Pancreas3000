package com.github.noahcunni.therapy.bolus;

import com.github.noahcunni.Pump;
import com.github.noahcunni.TherapySettings;
import com.github.noahcunni.therapy.BolusRequest;

public class BolusService {
    private final Pump pump;
    private final TherapySettings settings;

    private final BolusSafety safety;
    private final BolusCalculator calculator;
    
    public BolusService(Pump pump, TherapySettings settings, BolusSafety safety) {
        this.pump = pump;
        this.settings = settings;
        this.safety = safety;

        this.calculator = new BolusCalculator(settings);
    }

    public SafetyDecision preview(BolusRequest req) {
        double IOB = pump.getIOB();

        // Check cgm vlaues, carbs, and insulin input.
        SafetyDecision preview = safety.preview(req);
        
        if (!preview.approved) {
            return preview;
        } 

        BolusProposal proposal = calculator.calculate(req, IOB);
        
        SafetyDecision finalDecision = safety.review(proposal, IOB);

        // Log with ID 
        return finalDecision;
    }
}
