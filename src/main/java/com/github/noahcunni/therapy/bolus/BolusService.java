package pump.therapy.bolus;

import pump.Pump;
import pump.TherapySettings;

public class BolusService {
    private final Pump pump;
    private final TherapySettings settings;
    
    public BolusService(Pump pump, TherapySettings settings) {
        this.pump = pump;
        this.settings = settings;
    }
}
