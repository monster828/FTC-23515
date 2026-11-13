package org.firstinspires.ftc.teamcode.BioBuzz;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.pedropathing.paths.interpolator.Interpolator;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagsTurretTarget;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.BooleanSupplier;

@Autonomous(name = "AutoDriveMain", group = "Autonomous")
public class AutoDriveMain extends LinearOpMode2026 {
    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(59, 9, 180);
    private final Pose point1 = poseFactory.of(10, 9.5, 180);
    private final Pose point2 = poseFactory.of(64.9433, 131.1091, -90.8781);
    private final Pose point2Control1 = poseFactory.of(21.6987, 29.9094, 0);
    private final Pose point2Control2 = poseFactory.of(0, 125.7339, 0);
    private final Pose point2Control3 = poseFactory.of(64.5333, 104.3615, 0);
    private final Pose point2Segment1Heading = poseFactory.of(64.9433, 131.1091, 180);
    private final Pose point3 = poseFactory.of(10, 106.6598, -178.837);
    private final Pose point3Control1 = poseFactory.of(64.8796, 98.922, 0);
    private final Pose point3Control2 = poseFactory.of(42.9879, 107.3295, 0);

    public AprilTagsTurretTarget aprilTagsTurretTarget;

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                shoot(),
                follow(follower, corner()),
                waitMs(500),
                follow(follower, crossField()),
                shoot(),
                follow(follower, park())
        );
    }

    @Override
    public void runOpMode() {
        config();
        aprilTagsTurretTarget = new AprilTagsTurretTarget(turret, posGet, MiscUtils.getPositionFromDegrees(90, 537.7));
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        flywheel.setVelocity(2310 * TICKS_PER_REV / 60.0);
        hood.setPosition(0.2);
        intake.setPower(INTAKE_POWER);

        while (Math.abs(flywheel.getVelocity() - (2170 * TICKS_PER_REV / 60.0)) > 40){
            DoTurret();
        }

        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            DoTurret();

            telemetry.update();
        }
    }

    public Path corner() {
        return Paths.path(Paths.line(start, point1).constant(point1))
                .with(Constants.foresightConfig.maxPathSpeed.at(.5))
                .with(Constants.foresightConfig.maxDecelerationConstraint.at(.5));
    }
    public Path crossField() {
        return Paths.path(Paths.curve(point1, point2Control1, point2Control2, point2Control3, point2).heading(Interpolator.tangent.reverse()));
    }
    public Path park() {
        return Paths.path(Paths.curve(point2, point3Control1, point3Control2, point3).tangent());
    }

    public Command shoot(){
        return  sequential(
            waitUntil(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    return limelight.getLatestResult().getBarcodeResults().toArray().length > 0;
                }
            }),
            instant(() -> lift.setPower(LIFT_POWER)),
            waitMs(1500),
            instant(() -> lift.setPower(0))
        );
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
