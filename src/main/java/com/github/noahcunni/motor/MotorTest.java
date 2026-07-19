package com.github.noahcunni.motor;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class MotorTest {
    public static void main(String[] args) {
        Context pi4j = Pi4J.newAutoContext();
        try {
            Drv8825 motor = new Drv8825(pi4j, 13, 19, 12);   // dir, step, enable (BCM)

            motor.turnSteps(false, 800, 2000);   // 200 steps, 2000 µs half-period
            motor.stop();
        } finally {
            pi4j.shutdown();
        }
    }
}