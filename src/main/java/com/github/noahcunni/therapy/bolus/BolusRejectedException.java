package com.github.noahcunni.therapy.bolus;

public class BolusRejectedException extends Exception {
    public BolusRejectedException(String reason) {
        super(reason);
    }
}
