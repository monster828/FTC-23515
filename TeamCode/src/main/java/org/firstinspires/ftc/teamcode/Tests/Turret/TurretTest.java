package org.firstinspires.ftc.teamcode.Tests.Turret;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagsTurretTarget;

@TeleOp
public class TurretTest extends LinearOpMode2026 {

    double num = 0;

    double currentPosition = 0;

    double targetPosition = 0;

    double tolerance = 0.1;

    @Override
    public void runOpMode(){
        config();

        AprilTagsTurretTarget aprilTagsTurretTarget = new AprilTagsTurretTarget(turret);

        waitForStart();

        while (opModeIsActive()){
            if (gamepad1.a){
//                aprilTagsTurretTarget.Reset();
                turret.setPower(0.1);

                aprilTagsTurretTarget.SetTargetPosition(targetPosition);

//
//                while (gamepad1.a){
//                    double tx = aprilTagsTurretTarget.UpdateTxValue(limelight);
//
//                    // Fixed physical constants (measure these on your robot/field in inches or cm)
//                    final double h1_limelightHeight = 12.5;
//                    final double h2_targetHeight = 20.0;
//                    final double theta1_mountAngleDegrees = 15.0;
//
//                    // 1. Get 'ty' from the Limelight
//                    double theta2_ty = limelight.getLatestResult().getTy();
//
//                    // 2. Convert total angle to radians for Java's Math.tan()
//                    double totalAngleRadians = Math.toRadians(theta1_mountAngleDegrees + theta2_ty);
//
//                    // 3. Calculate distance
//                    double distance = (h2_targetHeight - h1_limelightHeight) / Math.tan(totalAngleRadians);
//
//
//                    aprilTagsTurretTarget.ChangeAngleSetPosition(tx);
//
//                    telemetry.addLine("tx: " + tx);
//                    telemetry.addLine("Amount of Position changes: " +MiscUtils.getPositionFromDegrees(tx, 537.7));
//
//                    telemetry.update();
//                }
////
////                sleep(1000);
//
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
            }

            if (gamepad1.b){
                aprilTagsTurretTarget.Reset();

                while (gamepad1.b){
                    telemetry.addData("", aprilTagsTurretTarget.UpdateAprilTagsResults(limelight));
                }

                turret.setPower(0);
            }

            if (gamepad1.dpad_up){
                targetPosition += 0.05;
            }

            if (gamepad1.dpad_down){
                targetPosition -= 0.05;
            }

            if (gamepad1.x){
                aprilTagsTurretTarget.Reset();

                turret.setPower(0.05);

                aprilTagsTurretTarget.ChangeAngleSetPosition(targetPosition);


            }

            turret.setPower(0);

            telemetry.addData("Text TX", num);
            telemetry.addData("Target Position", targetPosition);
            telemetry.addLine("Controls: \nA: Run test\nDPad up and down: Changes test rotation amount\nB: Run april tag");
            telemetry.update();
        }
    }

    void SetCurrentPosition(){
        currentPosition = MiscUtils.getDegreesFromMotor(turret.getCurrentPosition(), 537.7) * 14 / 43;
    }

}
