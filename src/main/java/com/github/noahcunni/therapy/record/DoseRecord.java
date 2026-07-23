package com.github.noahcunni.therapy.record;

import java.time.Instant;

public class DoseRecord {
    public Instant time;

    public PumpState pumpState;

    public DoseType doseType;
    public DoseState doseState;

    public double insulinRequested;
    public double amountOfCarbs; // Leave -1 if basal/correction

    public double insulinDelivered;

    public String reasonReject; // Leave null if accepted

    public DoseRecord(Instant time, PumpState pumpState, DoseType doseType,
        DoseState doseState, double insulinRequested, int amountOfCarbs,
        double insulinDelivered, String reasonReject) {
            this.time = time;
            this.pumpState = pumpState;
            this.doseType = doseType;
            this.doseState = doseState;
            this.insulinRequested = insulinRequested;
            this.amountOfCarbs = amountOfCarbs;
            this.insulinDelivered = insulinDelivered;
            this.reasonReject = reasonReject;
        }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(time)
        .append(" ").append(pumpState)
        .append(" ").append(doseType)
        .append(" ").append(doseState)
        .append(" req=").append(insulinRequested)
        .append(" del=").append(insulinDelivered);

        if (amountOfCarbs == -1)
            sb.append(" carb=" + amountOfCarbs);
        if (reasonReject != null) 
            sb.append(" rej=" + reasonReject);

        return sb.toString();
    }
}
