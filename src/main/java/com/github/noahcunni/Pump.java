package com.github.noahcunni;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.github.noahcunni.therapy.InMemoryTherapyLog;
import com.github.noahcunni.therapy.PumpState;
import com.github.noahcunni.therapy.bolus.BolusProposal;
/**
 * TODO: FIX ANY FLOATING POINT DRIFT.
 */
public class Pump {
    private static final double MAX_PER_TICK = 0.05;

    private final TherapySettings therapySettings;
    private final InMemoryTherapyLog therapyLog;

    private Instant now;

    private double basalAccum;
    private double basalOwed;

    private double bolusOwed;
    private boolean activeBolus;

    private PumpState state; 


    public Pump(TherapySettings therapySettings, InMemoryTherapyLog therapyLog) {
        this.therapySettings = therapySettings;
        this.therapyLog = therapyLog;

        bolusOwed = 0; // CHECK THE THERAPY LOG FOR UNITS OWED
        basalAccum = 0;
        basalOwed = 0;

        state = therapySettings.PUMP_STATE;

        activeBolus = false;
        // unitsOwed = therapyLog.checkUnitsOwed();
    }

    public void tick(Instant now) {
        this.now = now;
        basal(); // Increment basal
        checkDose(); // Check if due for insulin
        // TODO: Listen for cgm readings, bolus requests
    }

/* ----- ----- ----- TICK DOSING ----- ----- ----- */
    private void checkDose() {
        if (activeBolus && bolusOwed >= MAX_PER_TICK) {
            therapyLog.logBolusTick(now);
            dose();
            if (bolusOwed < MAX_PER_TICK) {
                activeBolus = false;
                therapyLog.logBolusCompletion(now);
                bolusOwed = 0; // Wipe any remaining bolus value
            }   
        } else if (basalOwed >= MAX_PER_TICK) {
            therapyLog.logBasal(now, therapySettings, MAX_PER_TICK);
            dose();
        } 
    }

    private void dose() {
        // SAFETY CHECK 2: IS EVERYTHING STILL GOOD?
        if (state != PumpState.OFF) {
            if (activeBolus) {
                bolusOwed -= 0.05;
            } else {
                basalOwed -= 0.05;
            }
        // Hand off to motor.
            System.out.println("0.05 Injected. BasalOwed: " + basalOwed + " BolusOwed: " + bolusOwed);
        }
    }
/* ----- ----- ----- ----------- ----- ----- ----- */

/* ----- ----- ----- BOLUS / BASAL INCREMENT ----- ----- ---- */
    // Increments basal insulin by the second
    private void basal() { 
        if (state == PumpState.MANUAL) {
            if (basalAccum >= 0.05) {
                basalAccum -= 0.05;
                basalOwed += 0.05;
            }
            basalAccum += (therapySettings.MAX_BASAL_RATE_PER_HOUR / 60000.0);
        }
    }

    public void bolus(BolusProposal request) {
        if (activeBolus) {
            // Log rejection
        } else {
            therapyLog.logNewBolus(now, therapySettings, request);
            activeBolus = true;
            bolusOwed += request.insulin;
            // Log bolus
        }
    }
/* ----- ----- ----- ----- ----- ----- ----- ----- */

    public void enterFault(Exception e) {
        state = PumpState.OFF;
    }

    public String status() {
        StringBuilder sb = new StringBuilder();
        sb.append(now)
        .append(" ").append(state)
        .append(" BolusOwed: ").append(bolusOwed)
        .append(" BasalOwed: ").append(basalOwed);

        return sb.toString();
    }

    public double getIOB() {
        long durationMins = (long) (60 * therapySettings.DURRATION_OF_INSULIN_ACTION_HOURS);
        return therapyLog.countIOB(now.minus(durationMins, ChronoUnit.MINUTES));
    }
}
