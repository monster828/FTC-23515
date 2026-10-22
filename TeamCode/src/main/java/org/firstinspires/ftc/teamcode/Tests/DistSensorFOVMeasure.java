package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

@Autonomous
public class DistSensorFOVMeasure extends LinearOpMode2026 {
    @Override
    public void runOpMode() throws InterruptedException {
        config();
        DistanceSensor sens = hardwareMap.get(DistanceSensor.class,"sens");
        Servo s = hardwareMap.get(Servo.class,"s");
        s.setPosition(0);
        waitForStart();
        float deg = 0;
        while(sens.getDistance(DistanceUnit.INCH) > 24) {
            s.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.FiveTurn,deg));
            deg += 0.5f;
            sleep(100);
        }
        telemetry.addData("Angle",deg);
        telemetry.update();
        sleep(10000);
    }
}
