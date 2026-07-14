package pump.therapy;

import java.time.Instant;

import pump.TherapySettings;

public class InDiskTherapyLog implements TherapyLog {
    private TherapySettings settings;
    private double insulinOnBoard;
    public InDiskTherapyLog(TherapySettings settings) {
        this.settings = settings;
    }

    @Override
    public void append(DoseRecord record) {
        // Write to file.
    }

    @Override
    public double countIOB(Instant since) {
        // TODO: index IOB entries to relevant ones. 
        // TODO: Read IOB from txt file. 
        // Retunr IOB from since
        return -1.1;
    }

/* ----- ----- ----- LOG ACTIONS ----- ----- ----- */


    public void logNewBolus(Instant now, TherapySettings settings, 
            BolusRequest bolusReq) {

    }

    public void logBolusTick(Instant now) {

    }

    public void logBolusCompletion(Instant now) {

    }

    public void logBasal(Instant now, TherapySettings settings, double maxPerTick) {}

/* ----- ----- ----- ----------- ----- ----- ----- */   

    public void printLog() {

    }
}
