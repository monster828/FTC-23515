package org.firstinspires.ftc.teamcode.BioBuzz;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.Controls.ControlsTrigger;
import org.firstinspires.ftc.teamcode.Utils.Controls.GamepadButton;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.TeleopMovementUtil;
import org.firstinspires.ftc.teamcode.Utils.Positioning.DistanceToHive;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagsTurretTarget;
import org.firstinspires.ftc.teamcode.Utils.Shooting.LimelightAngle;

@TeleOp
public class BioBuzzTeleop extends LinearOpMode2026 {

    TeleopMovementUtil teleopMovementUtil;
    AprilTagsTurretTarget aprilTagsTurretTarget;

    long startShot;

    boolean shooting = false;

    boolean isIntakeReversed = false;

    DistanceToHive distanceToHive;


    @Override
    public void runOpMode(){
        config();
        teleopMovementUtil = new TeleopMovementUtil(gamepad1, mot);
        aprilTagsTurretTarget = new AprilTagsTurretTarget(turret, posGet, 0);
        distanceToHive = new DistanceToHive(posGet);

        ControlsTrigger shootTrigger = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.right_bumper));
        ControlsTrigger intakeTrigger = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.a));
        ControlsTrigger inverseLiftTrigger = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.left_bumper));

        startShot = -1;

        waitForStart();

        flywheel.setVelocity(2200 * TICKS_PER_REV / 60.0);
        hood.setPosition(0.2);

        while (opModeIsActive()){
            teleopMovementUtil.UpdateDrive();
            DoTurret();

            if (shootTrigger.IsButtonPressed()){
                startShot = System.currentTimeMillis();
            }
            if (inverseLiftTrigger.IsButtonPressed()){
                lift.setPower(-0.2);
            }else {
                lift.setPower(0);
            }
            DoShoot();

            if (intakeTrigger.IsTriggered()){
                if (isIntakeReversed){
                    isIntakeReversed = false;
                    intake.setPower(-INTAKE_POWER);
                }else {
                    isIntakeReversed = true;
                    intake.setPower(INTAKE_POWER);
                }
            }

            lla.setPosition(LimelightAngle.getAngle(distanceToHive.GetDistance()));
        }
    }

    public void DoShoot(){
        if (startShot != -1 && System.currentTimeMillis() - startShot < 500){ // This should be 1500 if button is trigger
            if (!shooting){
                shooting = true;
                lift.setPower(LIFT_POWER);
            }
        }else {
            if (shooting){
                shooting = false;
                lift.setPower(0);
            }
        }
    }


    public void DoTurret(){
        // This will turn the turret to the april tags (if it sees it)

        double tx = aprilTagsTurretTarget.UpdateTxValue(limelight);

        aprilTagsTurretTarget.ChangeAngleSetPosition(tx, posGet.getVr());

        double positionChange = MiscUtils.getPositionFromDegrees(tx, 537.7);

        telemetry.addLine("tx: " + tx);
        telemetry.addLine("Amount of Position changes: " + positionChange);

        aprilTagsTurretTarget.CheckPositionAndSetPower(telemetry);
    }
}
