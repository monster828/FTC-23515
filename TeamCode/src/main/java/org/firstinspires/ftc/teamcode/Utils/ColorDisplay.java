package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.robotcore.hardware.Servo;

public class ColorDisplay {

    Servo servo;

    /**
     * Just one of those PWM controlled LED indicators
     * @param servo the "indicator"
     */
    public ColorDisplay(Servo servo) {
        this.servo = servo;
    }

    /**
     * Set the color of the light
     * @param hue maybe is between 0 and 1?
     */
    public void setColor(float hue) {
        float hue2;
        if(hue == Math.round(hue)) {
            hue2 = hue;
        } else {
            hue2 = (float) (hue - Math.floor(hue));
        }
        servo.setPosition((hue2*0.443f)+0.278f);
    }

    /**
     * turns it off.
     */
    public void turnOff() {
        servo.setPosition(0);
    }

    /**
     * sets it to white
     */
    public void white() {
        servo.setPosition(1);
    }

}
