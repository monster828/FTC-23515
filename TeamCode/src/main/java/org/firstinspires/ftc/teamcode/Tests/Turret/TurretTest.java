package org.firstinspires.ftc.teamcode.Tests.Turret;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.Controls.ControlsTrigger;
import org.firstinspires.ftc.teamcode.Utils.Controls.GamepadButton;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.TeleopMovementUtil;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagsTurretTarget;

@TeleOp
public class TurretTest extends LinearOpMode2026 {

    double num = 0;

    double currentPosition = 0;

    double targetPosition = 0;

    double tolerance = 0.1;

    ControlsTrigger triggerDrivingTracking;

    boolean isDrivingTracking = false;

    @Override
    public void runOpMode(){
        config();

        AprilTagsTurretTarget aprilTagsTurretTarget = new AprilTagsTurretTarget(turret, posGet);

        triggerDrivingTracking = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.a));

        TeleopMovementUtil teleopMovementUtil = new TeleopMovementUtil(gamepad1, mot);

        waitForStart();
        aprilTagsTurretTarget.Reset(); // This isn't really needed any more unless if we do PID tuning for this

        while (opModeIsActive()){
            boolean trig = triggerDrivingTracking.IsTriggered();

            teleopMovementUtil.UpdateDrive();

            if (trig){
                isDrivingTracking = !isDrivingTracking;
            }

            if (isDrivingTracking){
                double tx = aprilTagsTurretTarget.UpdateTxValue(limelight);

                aprilTagsTurretTarget.ChangeAngleSetPosition(tx, posGet.getVr());

                double positionChange = MiscUtils.getPositionFromDegrees(tx, 537.7);

                telemetry.addLine("tx: " + tx);
                telemetry.addLine("Amount of Position changes: " + positionChange);
            }else {
                //teleopMovementUtil.DeadDrive();
            }

            if (turret.getTargetPosition() > aprilTagsTurretTarget.maxPosition || turret.getTargetPosition() < aprilTagsTurretTarget.minPosition){
                turret.setPower(0);
                telemetry.addLine("WENT OVER!");
            }else {
                turret.setPower(0.7);  // LLA max .69  min .43
            }

            if (gamepad1.dpad_up){
                targetPosition += 10;
                aprilTagsTurretTarget.SetTargetPosition(targetPosition);
                sleep(100);
            }

            if (gamepad1.dpad_down){
                targetPosition -= 10;
                aprilTagsTurretTarget.SetTargetPosition(targetPosition);
                sleep(100);
            }

            if (gamepad1.x){
                aprilTagsTurretTarget.Reset();

                turret.setPower(0.05);

                aprilTagsTurretTarget.ChangeAngleSetPosition(targetPosition, posGet.getVr());
            }else {
                targetPosition = turret.getTargetPosition();
            }

            telemetry.addLine("CURRENTLY DOING: " + (isDrivingTracking ? "Driving Tracking" : "NONE"));
            telemetry.addData("Rotation V", posGet.getVr());
            telemetry.addData("Current Position", turret.getCurrentPosition());
            telemetry.addData("Text TX", num);
            telemetry.addData("Target Position", targetPosition);
            telemetry.addData("Max", aprilTagsTurretTarget.maxPosition);
            telemetry.addData("Min", aprilTagsTurretTarget.minPosition);
            telemetry.addLine("Controls: \nA: Run test\nDPad up and down: Changes test rotation amount\nB: Run april tag");
            telemetry.update();
        }
    }

    //                num = aprilTagsTurretTarget.UpdateTxValue(limelight);

    // aprilTagsTurretTarget.ChangeAngleSetPosition(turret.getCurrentPosition());


//                aprilTagsTurretTarget.Reset();
//
//                double targetPosition = currentPosition + num;
//
//                while (Math.abs(num) > tolerance){
//                    SetCurrentPosition();
//                    num = targetPosition - currentPosition;
//                    aprilTagsTurretTarget.ChangeAngle(num);
//                }
//
//                turret.setPower(0);

    void SetCurrentPosition(){
        currentPosition = MiscUtils.getDegreesFromMotor(turret.getCurrentPosition(), 537.7) * 14 / 43;
    }

}
