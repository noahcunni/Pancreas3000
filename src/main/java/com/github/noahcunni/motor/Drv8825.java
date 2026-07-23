package com.github.noahcunni.motor;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;

public class Drv8825 implements MotorInterface{
    private final DigitalOutput dir;
    private final DigitalOutput step;
    private final DigitalOutput enable;

    private final int STEPS_PER_MIN_DOSE;
    private final int STEP_DELAY_MICROS = 2000;

    public Drv8825(Context pi4j, int dirPin, int stepPin, int enablePin, int STEPS_PER_MIN_DOSE) {
        // Claim each gpio pin on run
        this.dir = pi4j.digitalOutput().create(dirPin);
        this.step = pi4j.digitalOutput().create(stepPin);
        this.enable = pi4j.digitalOutput().create(enablePin);
        this.STEPS_PER_MIN_DOSE = STEPS_PER_MIN_DOSE;
    }

    public void administerDose() {
        turnSteps(true, STEP_DELAY_MICROS);
    }

    public void close() {
        stop();
    }

    private void turnSteps(boolean forward, long stepDelayMicros) {
        enable.high(); // energize motor coils.

        if (forward) // Set direction
            dir.low();
        else
            dir.high();

        for (int i = 0; i < STEPS_PER_MIN_DOSE; i++) {
            stepOnce(stepDelayMicros);         
        }

        stop();
    }

    private void stepOnce(long stepDelayMicros) {
        step.high(); // Step here 
        sleepMicros(stepDelayMicros);
        step.low(); // Chip ignores transition from high to low.
        sleepMicros(stepDelayMicros);   
    }

    public void stop() {
        enable.low(); // De energize motor coils
    }

    private static void sleepMicros(long micros) {
        long end = System.nanoTime() + micros * 1_000;
        while (System.nanoTime() < end) {}
    }
}
