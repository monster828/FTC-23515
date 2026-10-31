package org.firstinspires.ftc.teamcode.Tests.Turret;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagsTurretTarget;

@TeleOp
public class TurretTest extends LinearOpMode2026 {

    double num = 0;

    double currentPosition = 0;

    @Override
    public void runOpMode(){
        config();

        AprilTagsTurretTarget aprilTagsTurretTarget = new AprilTagsTurretTarget(turret);

        waitForStart();

        while (opModeIsActive()){
            if (gamepad1.a){
                aprilTagsTurretTarget.Reset();

                double targetPosition = currentPosition + num;


                while (currentPosition != targetPosition){
                    SetCurrentPosition();
                    aprilTagsTurretTarget.ChangeAngle(currentPosition);
                }

                turret.setPower(0);
            }

            if (gamepad1.b){
                aprilTagsTurretTarget.Reset();

                while (gamepad1.b){
                    aprilTagsTurretTarget.UpdateAprilTagsResults(limelight);
                }
            }

            if (gamepad1.dpad_up){
                num += 0.05;
            }

            if (gamepad1.dpad_down){
                num += -0.05;
            }

            telemetry.addData("Text TX", num);
            telemetry.addLine("Controls: \nA: Run test\nDPad up and down: Changes test rotation amount\nB: Run april tag");
            telemetry.update();
        }
    }

    void SetCurrentPosition(){
        currentPosition = MiscUtils.getDegreesFromMotor(turret.getCurrentPosition(), 537.7) * 43/14;
    }

}
