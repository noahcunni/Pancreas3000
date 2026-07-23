package com.github.noahcunni;
import java.io.IOException;

import com.github.noahcunni.motor.Drv8825;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class App {
    public static void main(String[] args) throws IOException {
        int stepsPerPointFive = 10;
        Context pi4j = Pi4J.newAutoContext();
        Drv8825 motor = new Drv8825(pi4j, 13, 19, 12,
            stepsPerPointFive);   // dir, step, enable


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            motor.close();
            pi4j.shutdown();
            motor.printTickCount();
        }));

        for (int i = 0; i < 300 * 20; i++) {
            motor.administerDose();
        }
        motor.printTickCount();


        
        /*
        TherapySettings therapySettings = new TherapySettings();
        InMemoryTherapyLog therapyLog = new InMemoryTherapyLog(therapySettings);

        Context pi4j = Pi4J.newAutoContext();
        MotorInterface motor = new Drv8825(pi4j, 13, 19, 12,
            therapySettings.STEPS_PER_MIN_DOSE);   // dir, step, enable


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            motor.close();
            pi4j.shutdown();
        }));
        

        //Drv8825 motor = null;

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
        */
    }
}