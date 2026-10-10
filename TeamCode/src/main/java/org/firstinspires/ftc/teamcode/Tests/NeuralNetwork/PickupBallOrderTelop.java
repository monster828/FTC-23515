package org.firstinspires.ftc.teamcode.Tests.NeuralNetwork;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.BallsTargetingSystem;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;

import java.util.ArrayList;

@TeleOp
public class PickupBallOrderTelop extends LinearOpMode2026 {
    @Override
    public void runOpMode() {

        config();

        BallsTargetingSystem.Ball[] balls = new BallsTargetingSystem.Ball[5];

        telemetry.addLine("ARE THESE POSITIONS CORRECT? A: CONTINUE  B: EXIT");


        while (true){
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


        waitForStart();

        // Move and pick up
    }
}
