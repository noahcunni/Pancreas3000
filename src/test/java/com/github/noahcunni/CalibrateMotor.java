package com.github.noahcunni;

import com.github.noahcunni.motor.Drv8825;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class CalibrateMotor {
    public static void main(String[] args) {
        Context pi4j = Pi4J.newAutoContext();
        Drv8825 motor = new Drv8825(pi4j, 13, 19, 12,
            30);   // dir, step, enable


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            motor.close();
            pi4j.shutdown();
           // motor.printTickCount();
        }));

        for (int i = 0; i < 300 * 20; i++) {
            motor.administerDose();
        }
        // motor.printTickCount();

    }
}
