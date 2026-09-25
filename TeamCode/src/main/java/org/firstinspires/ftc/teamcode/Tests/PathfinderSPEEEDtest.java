package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.Pathfinder;

@Autonomous
public class PathfinderSPEEEDtest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Pathfinder.compileCircleMoveT(3);
        waitForStart();
        long start = System.currentTimeMillis();
        Pathfinder.pathFrom(3,3);
        Pathfinder.getPathTo(135,135);
        telemetry.addData("Memory usage", MiscUtils.getMemoryUsageP()+"%");
        long time = System.currentTimeMillis()-start;
        telemetry.addData("Pathfind time",time+"ms");
        telemetry.update();
        sleep(10000);
    }
}
