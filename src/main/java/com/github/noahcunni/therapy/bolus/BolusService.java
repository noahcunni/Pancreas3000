package com.github.noahcunni.therapy.bolus;

import com.github.noahcunni.Pump;
import com.github.noahcunni.TherapySettings;

public class BolusService {
    private final Pump pump;
    private final TherapySettings settings;

    private final BolusSafety safety;
    private final BolusCalculator calculator;

    private BolusProposal loggedProposal;
    
    public BolusService(Pump pump, TherapySettings settings, BolusSafety safety) {
        this.pump = pump;
        this.settings = settings;
        this.safety = safety;

        this.calculator = new BolusCalculator(settings);
    }

    public BolusProposal preview(BolusRequest req) throws BolusRejectedException {
        SafetyDecision preview = safety.preview(req);
        
        if (!preview.approved)
            throw new BolusRejectedException(preview.reason);

        double IOB = pump.getIOB();
        BolusProposal proposal = calculator.calculate(req, IOB);

        // Log with ID 
        logProposal(proposal);
        return proposal;
    }

    private void logProposal(BolusProposal proposal) {
        loggedProposal = proposal;
    }

    public void review(int bolusId) throws BolusRejectedException {
        if (loggedProposal == null)
            throw new BolusRejectedException("No bolus proposal to confirm");

        if (bolusId != loggedProposal.randomId)
            throw new BolusRejectedException("Bolus proposal id does not match logged proposals id");

        pump.bolus(loggedProposal);
        loggedProposal = null; // Reset proposal status
    }
}
