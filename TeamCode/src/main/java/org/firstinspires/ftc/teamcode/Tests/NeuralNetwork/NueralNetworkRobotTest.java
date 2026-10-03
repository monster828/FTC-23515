package org.firstinspires.ftc.teamcode.Tests.NeuralNetwork;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.Network;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;

@TeleOp
public class NueralNetworkRobotTest extends LinearOpMode2026 {
    public double positionX = 0;
    public double positionY = 0;
    public int robotBallCount = 0;
    public float GAME_MAP_SIZE_X = 12;
    public float GAME_MAP_SIZE_Y = 12;

    Network network;



    public void runOpMode(){

        config();

        // Inputs
            // 1. Robot Position X 0-1
            // 2. Robot Position Y 0-1
            // 3. Robot Ball Count 0-5

        // Actions
            // 1. Target Robot Position X 0-1
            // 2. Target Robot Position Y 0-1
            // 3. Shoot > 0.5

        network = new Network(3, 2, 256, 3);

        telemetry.addLine("--  READY TO START  --");
        telemetry.update();

        waitForStart();

        boolean clickedPredict = false;

        double[] output = null;
        double positionXDisplayed = positionX * GAME_MAP_SIZE_X;
        double positionYDisplayed = positionY * GAME_MAP_SIZE_Y;

        while (true){

            if (clickedPredict){

            }
            output = network.predict(new double[] {positionX, positionY, robotBallCount}, telemetry);
            positionX = output[0];
            positionY = output[1];


            if (isStopRequested()){
                break;
            }

            positionXDisplayed = positionX * GAME_MAP_SIZE_X;
            positionYDisplayed = positionY * GAME_MAP_SIZE_Y;
            telemetry.addLine("Robot Position  X: " + String.format("%.2f", positionXDisplayed) + "  Y: " + String.format("%.2f", positionYDisplayed));
            telemetry.addLine("Balls in robot: " + robotBallCount);
            telemetry.update();
        }
    }
}
