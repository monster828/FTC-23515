package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.robotcore.hardware.Servo;

public class ColorDisplay {

    Servo servo;

    public ColorDisplay(Servo servo) {
        this.servo = servo;
    }

    public void setColor(float hue) {
        float hue2;
        if(hue == Math.round(hue)) {
            hue2 = hue;
        } else {
            hue2 = (float) (hue - Math.floor(hue));
        }
        servo.setPosition((hue2*0.443f)+0.278f);
    }
    public void turnOff() {
        servo.setPosition(0);
    }
    public void white() {
        servo.setPosition(1);
    }

}
