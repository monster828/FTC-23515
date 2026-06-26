package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Arrays;

@TeleOp
public class LogReader extends LinearOpMode2026 {

    @Override
    public void runOpMode() throws InterruptedException {
        Object[][] dat = log.read();
        for(Object[] d : dat) {
            telemetry.addData((String)d[0], Arrays.toString((Byte[])d[1]));
        }
        telemetry.update();
    }
}
