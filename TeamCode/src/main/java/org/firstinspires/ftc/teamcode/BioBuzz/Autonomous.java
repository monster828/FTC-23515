package org.firstinspires.ftc.teamcode.BioBuzz;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.BallsTargetingSystem;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagsTurretTarget;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends LinearOpMode2026 {

    static final double INTAKE_POWER = 0.8;
    static final double LIFT_POWER = 1.0;
    AprilTagsTurretTarget aprilTagsTurretTarget;



    @Override
    public void runOpMode() throws InterruptedException {
        config();

        aprilTagsTurretTarget = new AprilTagsTurretTarget(turret, posGet, MiscUtils.getPositionFromDegrees(90, 537.7));

        waitForStart();


        while (opModeIsActive()){
            DoLoop(); // Do this every frame during auto
        }
    }

    public void ShootAll(){
        flywheel.setVelocity(2170);
        hood.setPosition(0.24);
        intake.setPower(INTAKE_POWER);
        //Lift only runs while left bumper is held
        lift.setPower(LIFT_POWER);
    }

    public void DoLoop(){
        DoTurret();

        telemetry.update();
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
