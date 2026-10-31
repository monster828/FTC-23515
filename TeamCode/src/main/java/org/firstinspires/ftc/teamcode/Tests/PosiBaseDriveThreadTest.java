package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.Movement.Pathfinder;
import org.firstinspires.ftc.teamcode.Utils.Threads.PosiBasedDriveThread;

@Autonomous
public class PosiBaseDriveThreadTest extends LinearOpMode2026 {

    @Override
    public void runOpMode() throws InterruptedException {
        PosiBasedDriveThread dt = new PosiBasedDriveThread(this);
        Pathfinder.compileDefaultObstaclesLC(11);
        waitForStart();
        while(opModeIsActive()) {

        }
    }
}
