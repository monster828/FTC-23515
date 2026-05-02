package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads.FTC2025to2026;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SORTAthread extends Thread {

    int Ps = 0;

    Servo servo;
    ColorSensor color;
    Telemetry tem;
    Servo chainP;
    Servo chainG;
    //ColorSensor Rccs;
    //DistanceSensor Lcds;

    public SORTAthread(Servo servo, ColorSensor sensor,Servo chainLeft,
                       Servo chainRight, Telemetry tem) {
        this.servo = servo;
        this.color = sensor;
        this.tem = tem;
        chainP = chainRight;
        chainG = chainLeft;
        //this.Rccs = Rccs;
        //this.Lcds = Lcds;
    }

    public SORTAthread(Servo servo, ColorSensor sensor, Telemetry tem) {
        this.servo = servo;
        this.color = sensor;
        this.tem = tem;
    }

    @Override
    public void run() {
        try {
            long delaystart = System.currentTimeMillis();
            while (System.currentTimeMillis()-delaystart<30000) {
                servo.setPosition(0.5);
                /*tem.addData("R ", color.red());
                tem.addData("G ", color.green());
                tem.addData("B ", color.blue());
                tem.addData("Alpha ", color.alpha());*/
                if (color.alpha() > 120) {
                    if (color.blue() - color.green() > 40) {
                        //tem.addData("color: ", "purplers");
                        servo.setPosition(0.6);
                        sleep(500);
                        servo.setPosition(0.5);
                        sleep(500);
                        if (chainP != null && Math.floor((double) Ps /2) == (double) Ps /2) {
                            chainP.setPosition(0.88);
                        }
                    } else {
                        //tem.addData("color: ", "gren");
                        servo.setPosition(0.4);
                        sleep(500);
                        servo.setPosition(0.5);
                        sleep(500);
                        if (chainG != null) {
                            chainG.setPosition(0.83);
                        }
                    }
                } else {
                    //tem.addData("color: ", "nothing");
                    servo.setPosition(0.5);
                }
                tem.update();
            }
        } catch (Exception ignored) {

        }
    }

}
