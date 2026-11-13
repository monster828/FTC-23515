package org.firstinspires.ftc.teamcode.BioBuzz;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.BallsTargetingSystem;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagsTurretTarget;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous
public class Autonomous extends LinearOpMode2026 {
    AprilTagsTurretTarget aprilTagsTurretTarget;



    @Override
    public void runOpMode() throws InterruptedException {
        config();

        aprilTagsTurretTarget = new AprilTagsTurretTarget(turret, posGet);


        waitForStart();

        while (opModeIsActive()){
            DoLoop(); // Do this every frame during auto
        }
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
