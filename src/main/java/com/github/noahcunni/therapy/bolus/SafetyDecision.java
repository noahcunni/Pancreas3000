package com.github.noahcunni.therapy.bolus;

public class SafetyDecision {
    public final boolean approved; 
    public final String reason;

    public final double carbs;
    public final int bg;
    public final double insulin;
    public SafetyDecision(boolean approved, String reason, 
        double carbs, int bg, double insulin) {
        this.approved = false;
        this.reason = reason;
        this.carbs = carbs;
        this.bg = bg;
        this.insulin = insulin;
    }
}
