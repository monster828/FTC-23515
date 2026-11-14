package org.firstinspires.ftc.teamcode.BioBuzz;

import org.firstinspires.ftc.teamcode.Utils.Controls.ControlsTrigger;
import org.firstinspires.ftc.teamcode.Utils.Controls.GamepadButton;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.TeleopMovementUtil;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagsTurretTarget;

public class BioBuzzTeleop extends LinearOpMode2026 {

    TeleopMovementUtil teleopMovementUtil;
    AprilTagsTurretTarget aprilTagsTurretTarget;

    long startShot;

    boolean shooting = false;


    @Override
    public void runOpMode(){
        config();
        teleopMovementUtil = new TeleopMovementUtil(gamepad1, mot);
        aprilTagsTurretTarget = new AprilTagsTurretTarget(turret, posGet, 0);

        ControlsTrigger shootTrigger = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.dpad_right));

        startShot = -1;

        waitForStart();

        flywheel.setVelocity(2310 * TICKS_PER_REV / 60.0);
        hood.setPosition(0.2);

        while (opModeIsActive()){
            teleopMovementUtil.UpdateDrive();
            DoTurret();

            if (shootTrigger.IsTriggered()){
                startShot = System.currentTimeMillis();
            }
            DoShoot();
        }
    }

    public void DoShoot(){
        if (startShot != -1 && System.currentTimeMillis() - startShot < 1500){
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
