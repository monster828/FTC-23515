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
        config();
        DistSensorObstacleDetector obst = new DistSensorObstacleDetector(dsl,dsr, Pathfinder.obstacle,posGet);
        waitForStart();
        while(opModeIsActive()) {
            obst.update(0);
            Pathfinder.pathFrom(0,0);
            Pathfinder.getPathTo(1,1);
            Pathfinder.generatePathPoints(0);
            Pathfinder.pickRotations();
            String name = "ObstacleFeed";
            long start = System.currentTimeMillis();
            File f = new File(MiscUtils.dataFolder+"/"+name+".jpeg");
            if(f.exists())f.delete();
            Pathfinder.saveI(2,40,name);
            telemetry.update();
            while(System.currentTimeMillis()-start < 3000 && opModeIsActive());
        }
    }
}
