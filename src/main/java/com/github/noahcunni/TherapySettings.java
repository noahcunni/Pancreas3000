package com.github.noahcunni;

import com.github.noahcunni.therapy.PumpState;

public class TherapySettings {
    // Basal Settings
    public final double BASAL_PER_HOUR = 1.5;
    public final double MAX_BASAL_RATE_PER_HOUR = 3;

    // Bolus settings
    public final double MAX_BOLUS_PER_MEAL = 30;
    public final int MAX_CARBS_PER_MEAL = 255;
    public final int TARGET_GLUCOSE_IN_CALCULATOR = 110;
    public final int CORRECT_ABOVE_GLUCOSE_IN_CALCULATOR = 150;
    public final int MIN_GLUCOSE_FOR_CALCULATION = 70;
    public final double INSULIN_TO_CARB_RATIO = 7;
    public final int CORRECTION_FACTOR = 55;
    public final double DURRATION_OF_INSULIN_ACTION_HOURS = 3; 

    // Closed - Open
    public final PumpState PUMP_STATE = PumpState.MANUAL;
}
