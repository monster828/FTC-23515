package org.firstinspires.ftc.teamcode.Prototype;

public class servoConvertFromDegrees {
    public static float conversion(int servo, float deg) {
        if (servo == 300) {
            return deg/300;
        } else {
            return deg/1800;
        }
    }
}
