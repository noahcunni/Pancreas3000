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
import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class App {
    public static void main(String[] args) throws IOException {
        TherapySettings therapySettings = new TherapySettings();
        InMemoryTherapyLog therapyLog = new InMemoryTherapyLog(therapySettings);

        Context pi4j = Pi4J.newAutoContext();
        Drv8825 motor = new Drv8825(pi4j, 13, 19, 12);   // dir, step, enable (BCM)

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            motor.stop();
            pi4j.shutdown();
        }));
        
        Pump pump = new Pump(therapySettings, therapyLog, motor);

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