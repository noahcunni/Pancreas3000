package pump;
import java.time.Instant;
import pump.therapy.BolusRequest;
import pump.therapy.PumpState;
import pump.therapy.TherapyLog;
import pump.therapy.bolus.SafetyDecision;
/**
 * TODO: FIX ANY FLOATING POINT DRIFT.
 */
public class Pump {
    private static final double MAX_PER_TICK = 0.05;

    private final TherapySettings therapySettings;
    private final TherapyLog therapyLog;

    private Instant now;

    private double basalAccum;
    private double basalOwed;

    private double bolusOwed;
    private boolean activeBolus;

    private PumpState state; 


    public Pump(TherapySettings therapySettings, TherapyLog therapyLog) {
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

/* ----- ----- ----- BASAL / BOLUS INCREMENT ----- ----- ---- */
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

    public void requestBolus(double unitsRequested) {
        BolusRequest bolusReq = new BolusRequest(unitsRequested, 4.05, null);
        therapyLog.logNewBolus(now, therapySettings, bolusReq);
        bolusOwed += unitsRequested; // Add gaurds and http later
        activeBolus = true;
    }

    public void Bolus(SafetyDecision decision) {
       /* therapyLog.logNewBolus(decision);
        if (decision.approved) {
            bolusOwed += decision.insulinApproved; // Add gaurds and http later
            activeBolus = true;
        }*/
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
}
