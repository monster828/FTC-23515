package org.firstinspires.ftc.teamcode.Tests.NeuralNetwork;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.BallsTargetingSystem;
import org.firstinspires.ftc.teamcode.Utils.Detection.DistSensorObstacleDetector;
import org.firstinspires.ftc.teamcode.Utils.Detection.LimelightBallDetection;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.Movement.Pathfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@TeleOp
public class PickupBallOrderTelop extends LinearOpMode2026 {

    LimelightBallDetection limelightBallDetection;

    @Override
    public void runOpMode() {

        config();

        Random rand = new Random();
        BallsTargetingSystem.Ball[] balls = new BallsTargetingSystem.Ball[5];
        for(int i = 0; i < balls.length; i++) {
            balls[i] = new BallsTargetingSystem.Ball((rand.nextFloat() * 100)-50, (rand.nextFloat() * 100)-50, rand.nextBoolean());
            for(int n = 0; n < 5; n++) rand.nextFloat();
        }

        telemetry.addLine(Arrays.toString(balls));
        telemetry.addLine("ARE THESE POSITIONS CORRECT? A: CONTINUE  B: EXIT");
        telemetry.update();


        while (opModeIsActive() || opModeInInit()){
            if (gamepad1.a){
                while (gamepad1.a){}
                break;
            }else if (gamepad1.b){
                return;
            }
        }
        long start = System.currentTimeMillis();

        // Ball Targeting Setup
        BallsTargetingSystem.setup();
        ArrayList<BallsTargetingSystem.Ball> ballsInOrder = BallsTargetingSystem.orderBallsByPickup(balls);

        // Pathfinding Setup
        DistSensorObstacleDetector obst = new DistSensorObstacleDetector(dsl,dsr,Pathfinder.obstacle,posGet);
        obst.update(0);

        Pathfinder.compileCircleMoveT(3);

        float sRX = 70; float sRY = 70; float sRR = 0;
        float robotX = sRX;
        float robotY = sRY;
        float robotRotation = sRR;
        for(int i = 0; i < ballsInOrder.size(); i++) {
            Pathfinder.pathFrom((int) robotX, (int) robotY);
            Pathfinder.getPathTo((int) ballsInOrder.get(i).toFieldCoords(sRX, sRY, sRR)[0], (int) ballsInOrder.get(0).toFieldCoords(sRX, sRY, sRR)[1]);
            Pathfinder.generatePathPoints((int) robotRotation);
            Pathfinder.pickRotations();
            Pathfinder.saveTxT("PathOfBall"+i);
            Pathfinder.saveI(4, "PathOfBall"+i);
            int[][] points = Pathfinder.pathPoints;
            int[] pointRots = Pathfinder.pointAngle;
            robotX = points[points.length-1][0];
            robotY = points[points.length-1][1];
            robotRotation = pointRots[pointRots.length-1];
            sleep(1000);
        }

        //limelightBallDetection = new LimelightBallDetection(limelight);

        telemetry.addData("Time in ms",System.currentTimeMillis()-start);
        telemetry.update();


        waitForStart();

        limelightBallDetection.SwitchToPollen();
        limelightBallDetection.DetectBalls(telemetry, false);

        // Move and pick up YAY!
    }
}
