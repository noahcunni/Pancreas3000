package com.github.noahcunni.therapy.bolus;

public class BolusProposal {
    public final double insulinProposed;
    public final double IOB;

    public BolusProposal(double insulinProposed, double IOB) {
        this.insulinProposed = insulinProposed;
        this.IOB = IOB;
    }
}
