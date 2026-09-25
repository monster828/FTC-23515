package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;

@TeleOp
public class LimelightPipelineSwitchSpeedTest extends LinearOpMode2026 {

    public void runOpMode(){
        config();

        limelight.pipelineSwitch(1);
        while (limelight.getStatus().getPipelineIndex() != 1){

        }

        sleep(100);
        long startTime = System.currentTimeMillis();
        limelight.pipelineSwitch(0);

        while (limelight.getStatus().getPipelineIndex() == 1){

        }

        long timeElapsed = System.currentTimeMillis() - startTime;

        telemetry.addData("Time Elapsed m", timeElapsed);

        telemetry.update();
        sleep(1000000);
    }
}
