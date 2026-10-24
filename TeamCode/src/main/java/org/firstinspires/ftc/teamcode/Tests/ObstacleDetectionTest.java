package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.Detection.DistSensorObstacleDetector;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.Pathfinder;

import java.io.File;

@Autonomous
public class ObstacleDetectionTest extends LinearOpMode2026 {
    @Override
    public void runOpMode() throws InterruptedException {
        Pathfinder.obstacle = new boolean[141][141];
        config();
        DistSensorObstacleDetector obst = new DistSensorObstacleDetector(dsl,dsr, Pathfinder.obstacle,posGet);
        waitForStart();
        while(opModeIsActive()) {
            long start = System.currentTimeMillis();
            obst.update(0);
            telemetry.update();
            while(System.currentTimeMillis()-start < 50 && opModeIsActive());
        }
        Pathfinder.pathFrom(0,0);
        Pathfinder.getPathTo(1,1);
        Pathfinder.generatePathPoints(0);
        Pathfinder.pickRotations();
        String name = "ObstacleFeed";
        File f = new File(MiscUtils.dataFolder+"/"+name+".jpeg");
        if(f.exists())f.delete();
        Pathfinder.saveI(1,50,name);
        telemetry.update();
        sleep(7000);
    }
}
