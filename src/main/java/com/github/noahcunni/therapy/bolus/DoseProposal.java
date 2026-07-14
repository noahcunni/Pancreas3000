package com.github.noahcunni.therapy.bolus;

public class DoseProposal {
    public final double insulinProposed;
    public final double IOB;

    public DoseProposal(double insulinProposed, double IOB) {
        this.insulinProposed = insulinProposed;
        this.IOB = IOB;
    }
}
