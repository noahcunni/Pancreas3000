package com.github.noahcunni.therapy.record;

import java.time.Instant;
import java.util.ArrayList;

import com.github.noahcunni.TherapySettings;
import com.github.noahcunni.therapy.bolus.BolusProposal;

public class InMemoryTherapyLog {
    private TherapySettings settings;
    private ArrayList<DoseRecord> record;
    private int index;
    private double insulinOnBoard;

    public InMemoryTherapyLog(TherapySettings settings) {
        this.settings = settings;
        record = new ArrayList<>();
        index = 0;
    }

    public void append(DoseRecord r) {
        record.add(r);
    }

    public double countIOB(Instant since) {
        // TODO: index IOB entries to relevant ones. 
        // TODO: implement a curvilinear IOB model, insulin peaks around 60 mins not 3 hours
        
        insulinOnBoard = 0;
        for (DoseRecord r : record) {
            if (r.time.isAfter(since)) {
                insulinOnBoard += r.insulinDelivered;
            }
        }
        return insulinOnBoard;
    }

/* ----- ----- ----- LOG ACTIONS ----- ----- ----- */
    public void logNewBolus(Instant now, TherapySettings settings, BolusProposal proposal) {
            DoseRecord r = new DoseRecord(now, settings.PUMP_STATE, 
                DoseType.BOLUS, DoseState.ACTIVE, proposal.insulin, proposal.carbs, 0.0, null);
            record.add(r);
    }

    public void logBolusTick(Instant now) {
        DoseRecord r = record.get(record.size() - 1);
        r.insulinDelivered += 0.05;
        r.time = now;
    }

    public void logBolusCompletion(Instant now) {
        DoseRecord r = record.get(record.size() - 1);
        r.doseState = DoseState.COMPLETE;
        r.time = now;
    }

    public void logBasal(Instant now, TherapySettings settings, double maxPerTick) {
        DoseRecord r = new DoseRecord(now, settings.PUMP_STATE, DoseType.BASAL,
            DoseState.COMPLETE, maxPerTick, -1, 
            0.05, null);
        record.add(r);
    }
/* ----- ----- ----- ----------- ----- ----- ----- */

    public void printLog() {
        for (DoseRecord r : record) {
            System.out.println(r.toString());
        }
    }
}
