package org.firstinspires.ftc.teamcode.Utils.Threads;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Utils.Controls.ControlsTrigger;
import org.firstinspires.ftc.teamcode.Utils.Detection.CheckedDistanceSensor;

public class SensingThread extends Thread {

    public LinearOpMode opMode;
    public Object[] sensors;

    public SensingThread(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void add(Object o) {
        Object[] sens2 = new Object[sensors.length+1];
        System.arraycopy(sensors,0,sens2,0,sensors.length);
        sens2[sensors.length] = o;
    }

    @Override
    public void run() {
        super.run();
        while(opMode.opModeIsActive() || opMode.opModeInInit()) {
            for (Object o : sensors) {
                switch (o.getClass().getSimpleName()) {
                    case ("CheckedDistanceSensor"): {
                        CheckedDistanceSensor c = (CheckedDistanceSensor) o;
                        c.update();
                        break;
                    }
                    case ("ControlsTrigger"): {
                        ControlsTrigger c = (ControlsTrigger) o;
                        c.UpdateTrigger();
                        break;
                    }
                    //PUT OTHER SENSOR CLASSES HERE.
                }
            }
        }
    }
}
