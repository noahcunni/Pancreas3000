package com.github.noahcunni.therapy;

public class BolusRequest {
    public double amountOfCarbs;
    public double insulinRequested;
    public String reasonReject;

    public double insulinCalculated;
    public double insulinApproved;

    public BolusRequest(double insulinRequested, double amountOfCarbs, 
        String reasonReject) {
            this.insulinRequested = insulinRequested;
            this.amountOfCarbs = amountOfCarbs;
            this.reasonReject = reasonReject;
    }
}
