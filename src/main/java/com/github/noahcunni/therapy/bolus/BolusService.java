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

    public BolusProposal preview(BolusRequest req) {
        double IOB = pump.getIOB();

        // Check bounds: remove safety, stupid class.

        // calculation,

        // Log calculation with id in proposal, with current time stamp

        // Check cgm vlaues, carbs, and insulin input.
        SafetyDecision preview = safety.preview(req);
        
        if (!preview.approved) {
            throw new IllegalArgumentException();
        } 

        BolusProposal proposal = calculator.calculate(req, IOB);

        // Log with ID 
        return proposal;
    }

    public void review(BolusProposal proposal) {
        // Checked logged id with post.

        // If info matches, send in confirmation to pump to pump.

        // if messed up throw in an exception.
    }
}
