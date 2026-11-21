package org.firstinspires.ftc.teamcode.BioBuzz;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utils.Controls.ControlsTrigger;
import org.firstinspires.ftc.teamcode.Utils.Controls.GamepadButton;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.TeleopMovementUtil;
import org.firstinspires.ftc.teamcode.Utils.Positioning.DistanceToHive;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagClusterTargeting;
import org.firstinspires.ftc.teamcode.Utils.Shooting.Turret;
import org.firstinspires.ftc.teamcode.Utils.Shooting.LimelightAngle;
import org.firstinspires.ftc.teamcode.Utils.Shooting.TurretConfigurator;

@TeleOp
public class BioBuzzTeleop extends LinearOpMode2026 {

    TeleopMovementUtil teleopMovementUtil;
    Turret turretAimer;

    AprilTagClusterTargeting hiveTargeting;

    long startShot;

    boolean shooting = false;

    boolean isIntakeReversed = false;

    DistanceToHive distanceToHive;

    TurretConfigurator turretConfigurator;
    GoBildaPinpointDriver odo;

    ControlsTrigger toggleClusterTelem; // created in runOpMode, gamepad1 is still null here
    boolean showClusterTelem = false;
    boolean liftHighReverse = false;

    double tx = 0;

    @Override
    public void runOpMode(){
        config();
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "POC");
        teleopMovementUtil = new TeleopMovementUtil(gamepad1, mot);
        turretAimer = new Turret(turret, posGet, 0);
        distanceToHive = new DistanceToHive(posGet);
        turretConfigurator = new TurretConfigurator(this);

        ControlsTrigger shootTrigger = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.right_bumper));
        ControlsTrigger intakeTrigger = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.cross));
        ControlsTrigger inverseLiftTrigger = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.left_bumper));
        ControlsTrigger resetPositon = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.left_stick_button));
        ControlsTrigger controlTurret = new ControlsTrigger(new GamepadButton(gamepad2, GamepadButton.GButton.cross));
        ControlsTrigger opSquare = new ControlsTrigger(new GamepadButton(gamepad2, GamepadButton.GButton.square));
        boolean doesControlTurret = false;

        toggleClusterTelem = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.triangle));


// cluster code
        hiveTargeting = new AprilTagClusterTargeting();      // picks pipeline 4 red / 3 blue
        hiveTargeting.setTiltServo(lla);
        hiveTargeting.setDistanceToHive(distanceToHive);
        hiveTargeting.allowedHives.add(isCurrentAllienceRed
                ? AprilTagClusterTargeting.RED_SCORING
                : AprilTagClusterTargeting.BLUE_SCORING);

        startShot = -1;

        waitForStart();

        while (opModeIsActive()){
            double goalDistance = distanceToHive.GetDistance(); // replaced by Limelight distance below when it sees the hive
            try {
                teleopMovementUtil.UpdateDrive();
                telemetry.addLine("--- BioBuzz Teleop ---");

                if (controlTurret.IsTriggered()){
                    doesControlTurret = !doesControlTurret;
                }

                if (doesControlTurret){
                    turret.setTargetPosition(((gamepad2.dpad_right ? 1 : 0) - (gamepad2.dpad_left ? 1 : 0)) * 50);
                    turret.setPower(0.5);

                    if (opSquare.IsTriggered()){
                        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    }
                }else {
                    DoTurret(); // also runs hiveTargeting.update()
                }

                goalDistance = getGoalDistance();
                telemetry.addLine("--- Turret Configurator ---");
                turretConfigurator.runTurret(goalDistance);

                if (gamepad1.right_bumper){
//                    startShot = System.currentTimeMillis();
                    lift.setPower(.6);
                }
                if (inverseLiftTrigger.IsButtonPressed()){
                    lift.setPower(-0.2);
                } else if (!shooting) {

                    if (isIntakeReversed){
                        lift.setPower(-0.1);
                        liftHighReverse = true;
                    }else {
                        if (liftHighReverse){
                            lift.setPower(-1.0);
                        }
                    }

                }
                DoShoot();

                if (intakeTrigger.IsTriggered()){
                    if (isIntakeReversed){
                        isIntakeReversed = false;
                        intake.setPower(-INTAKE_POWER);
                        lift.setPower(-1.0);
                    }else {
                        isIntakeReversed = true;
                        intake.setPower(INTAKE_POWER);
                    }
                }

                if (resetPositon.IsTriggered()){
                    odo.resetPosAndIMU();
                    il.setPosition(.277);
                    wait(500);
                    il.setPosition(.611);
                }

                lla.setPosition(LimelightAngle.getAngle(goalDistance));

            } catch (Exception e) {
                // Show what crashed instead of freezing on the init screen
                telemetry.addData("Loop error", e.toString());
                StackTraceElement[] trace = e.getStackTrace();
                for (int i = 0; i < Math.min(5, trace.length); i++) {
                    telemetry.addLine("  at " + trace[i]);
                }
            }

            telemetry.addData("Distance From Goal: ", goalDistance);
            telemetry.addData("Distance source: ", lastDistanceFromLimelight ? "Limelight" : "Odometry");
            telemetry.addData("Odometry distance: ", distanceToHive.GetDistance());

            telemetry.update();
            lla.setPosition(LimelightAngle.getAngle(goalDistance));
        }
    }

    boolean lastDistanceFromLimelight = false;

    /**
     * Distance to the hive opening from the Limelight (April tag cluster) when it can see the hive,
     * otherwise the odometry distance. Call after DoTurret() so hiveTargeting is up to date.
     */
    public double getGoalDistance(){
        double limelightDistance = -1;
        try {
            limelightDistance = hiveTargeting.getDistanceInches(); // -1 if no target / no 3D / unbelievable
        } catch (Exception e) {
            telemetry.addData("Cluster distance error", e.toString());
        }

        lastDistanceFromLimelight = limelightDistance > 0;
        return lastDistanceFromLimelight ? limelightDistance : distanceToHive.GetDistance();
    }

    public void DoShoot(){
        if (startShot != -1 && System.currentTimeMillis() - startShot < 10){ // This should be 1500 if button is trigger
            if (!shooting){
                shooting = true;
                lift.setPower(LIFT_POWER);
            }
        }else {
            if (shooting){
                shooting = false;
//                lift.setPower(0);
            }
        }
    }


    public void DoTurret(){



// cluster code (wrapped so a bug here can't stop the turret / shooter)
        try {
            if (hiveTargeting.update(limelight)) {
                tx = hiveTargeting.getTx();
            }
        } catch (Exception e) {
            telemetry.addData("Cluster error", e.toString());
        }
        // This will turn the turret to the april tags (if it sees it)

         tx = turretAimer.UpdateTxValue(limelight);



        turretAimer.ChangeAngleSetPosition(tx + (gamepad2.right_trigger > 0.1 ? gamepad2.right_trigger * 10 : 0) - (gamepad2.left_trigger > 0.1 ? gamepad2.left_trigger * 10 : 0), posGet.getVr());

        double positionChange = MiscUtils.getMotorPositionFromDegrees(tx, 537.7);

        telemetry.addLine("tx: " + tx);
        telemetry.addLine("Amount of Position changes: " + positionChange);

        turretAimer.CheckPositionAndSetPower(telemetry);
// cluster code
        try {
            if (toggleClusterTelem.IsTriggered()) {
                showClusterTelem = !showClusterTelem; // triangle turns cluster telemetry on/off
            }
            if (showClusterTelem) {
                hiveTargeting.addTelemetry(telemetry);
            }
        } catch (Exception e) {
            telemetry.addData("Cluster telemetry error", e.toString());
        }
    }
}
