package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.BallsTargetingSystem;
import org.firstinspires.ftc.teamcode.Utils.Controls.ControlsTrigger;
import org.firstinspires.ftc.teamcode.Utils.Controls.GamepadButton;
import org.firstinspires.ftc.teamcode.Utils.Detection.LimelightBallDetection;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;


@TeleOp
public class THE_ULTIMATE_BALL_DETECTION extends LinearOpMode2026 {

    @Override
    public void runOpMode() throws InterruptedException {
        config();
        LimelightBallDetection limelightBallDetection = new LimelightBallDetection(limelight);
        BallsTargetingSystem.setup();
        GamepadButton gamepadButton = new GamepadButton(gamepad1, GamepadButton.GButton.a);
        ControlsTrigger controlsTrigger = new ControlsTrigger(gamepadButton);
        GamepadButton secondGamepadButton = new GamepadButton(gamepad1, GamepadButton.GButton.b);
        ControlsTrigger switchBallDetection = new ControlsTrigger(secondGamepadButton);
        GamepadButton thirdGamepadButton = new GamepadButton(gamepad1, GamepadButton.GButton.y);
        ControlsTrigger switchAlliences = new ControlsTrigger(thirdGamepadButton);
        boolean isDetectingPollen = true;
        boolean isBlueAllience = true;
        limelightBallDetection.SwitchToPollen();

        telemetry.addLine("CONTROLS - \n -Detect Balls  (a)\n -Switch Ball Detection (Pollen/Nector)  (b) \n -Switch Allience  (y)");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){
            if (switchAlliences.IsTriggered()){
                isBlueAllience = !isBlueAllience;

                DoTelemetry(isDetectingPollen, isBlueAllience);
                telemetry.update();
            }

            if (switchBallDetection.IsTriggered()){
                if (isDetectingPollen){
                    limelightBallDetection.SwitchToNector(isBlueAllience);
                    isDetectingPollen = false;
                }else {
                    limelightBallDetection.SwitchToPollen();
                    isDetectingPollen = true;
                }

                DoTelemetry(isDetectingPollen, isBlueAllience);
                telemetry.update();
            }

            if (controlsTrigger.IsTriggered()){
                limelightBallDetection.DetectBalls(telemetry);

                DoTelemetry(isDetectingPollen, isBlueAllience);
                telemetry.update();
            }
        }
    }

    void DoTelemetry(boolean detectingPollen, boolean isBlueAllience){
        telemetry.addLine(detectingPollen ? "Current Detection: Pollen" : "Current Detection: Nector");
        telemetry.addLine(isBlueAllience ? "Current Allience: Blue" : "Current Allience: Red");
    }
}
