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

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){
            if (controlsTrigger.IsTriggered()){
                limelightBallDetection.DetectBalls(telemetry);
            }
        }
    }
}
