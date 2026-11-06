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
                turret.setPower(0.05);
//
                while (gamepad1.a){
                    double tx = aprilTagsTurretTarget.UpdateTxValue(limelight);

                    if (gamepad1.right_trigger_pressed){
                        aprilTagsTurretTarget.ChangeAngleSetPosition(tx);
                    }

                    telemetry.addLine("tx: " + tx);
                    telemetry.update();
                }
//
//                sleep(1000);

                num = aprilTagsTurretTarget.UpdateTxValue(limelight);

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
                targetPosition += -0.05;
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
