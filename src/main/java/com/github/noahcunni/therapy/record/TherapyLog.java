package com.github.noahcunni.therapy.record;

import java.time.Instant;

import com.github.noahcunni.TherapySettings;
import com.github.noahcunni.therapy.bolus.BolusRequest;

/**
 * Used to keep track of IOB and carbs eaten.
 */

public interface TherapyLog {
    public void append(DoseRecord record);
    public double countIOB(Instant since);
    
    
    public void logBolusTick(Instant now);
    public void logBolusCompletion(Instant now);

    public void logNewBolus(Instant now, TherapySettings settings, BolusRequest bolusReq);
    public void logBasal(Instant now, TherapySettings settings, double maxPerTick);

    public void printLog();
}