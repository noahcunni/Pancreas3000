package com.github.noahcunni;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.noahcunni.motor.Drv8825;
import com.github.noahcunni.therapy.InMemoryTherapyLog;
import com.github.noahcunni.therapy.bolus.BolusSafety;
import com.github.noahcunni.therapy.bolus.BolusService;

public class App {
    public static void main(String[] args) throws IOException {
        TherapySettings therapySettings = new TherapySettings();
        InMemoryTherapyLog therapyLog = new InMemoryTherapyLog(therapySettings);

        Drv8825 hat = new Drv8825(null, 0, 0, 0);
        
        Pump pump = new Pump(therapySettings, therapyLog);

        BolusSafety safety = new BolusSafety(therapySettings);
        BolusService bolusService = new BolusService(pump, therapySettings, safety);
        
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        new PumpHttpServer(8080, bolusService, pump, scheduler).start();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                pump.tick(Instant.now());
            } catch (Exception e) {
                e.printStackTrace();
                // log.error("tick failed", e);
                // pump.enterFault(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

    }
}