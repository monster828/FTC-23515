package org.firstinspires.ftc.teamcode.Tests.NeuralNetwork;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.BallsTargetingSystem;
import org.firstinspires.ftc.teamcode.Utils.Detection.LimelightBallDetection;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.Movement.Pathfinder;

import java.util.ArrayList;

@TeleOp
public class PickupBallOrderTelop extends LinearOpMode2026 {

    LimelightBallDetection limelightBallDetection;

    @Override
    public void runOpMode() {

        config();

        BallsTargetingSystem.Ball[] balls = new BallsTargetingSystem.Ball[5];

        telemetry.addLine("ARE THESE POSITIONS CORRECT? A: CONTINUE  B: EXIT");


        while (opModeIsActive() || opModeInInit()){
            if (gamepad1.a){
                while (gamepad1.a){}
                break;
            }else if (gamepad1.b){
                return;
            }
        }

        // Ball Targeting Setup
        BallsTargetingSystem.setup();
        ArrayList<BallsTargetingSystem.Ball> ballsInOrder = BallsTargetingSystem.orderBallsByPickup(balls);

        // Pathfinding Setup
        Pathfinder.compileCircleMoveT(3);

        float robotX = 70;
        float robotY = 70;
        float robotRotation = 0;
        Pathfinder.pathFrom((int) robotX, (int) robotY);
        Pathfinder.getPathTo((int) ballsInOrder.get(0).toFieldCoords(robotX,robotY,robotRotation)[0], (int) ballsInOrder.get(0).toFieldCoords(robotX,robotY,robotRotation)[1]);
        Pathfinder.generatePathPoints((int) robotRotation);
        Pathfinder.pickRotations();
        Pathfinder.saveI(4);

        limelightBallDetection = new LimelightBallDetection(limelight);


        waitForStart();

        limelightBallDetection.SwitchToPollen();
        limelightBallDetection.DetectBalls(telemetry);

        // Move and pick up
    }
}
