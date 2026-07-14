package com.github.noahcunni.therapy.bolus;

import com.github.noahcunni.Pump;
import com.github.noahcunni.TherapySettings;

public class BolusService {
    private final Pump pump;
    private final TherapySettings settings;
    
    public BolusService(Pump pump, TherapySettings settings) {
        this.pump = pump;
        this.settings = settings;
    }
}
