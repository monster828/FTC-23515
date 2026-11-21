package org.firstinspires.ftc.teamcode.pedropathPathsBiobuzz;


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

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.MovementUtils;
import org.firstinspires.ftc.teamcode.Utils.Positioning.DistanceToHive;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagClusterTargeting;
import org.firstinspires.ftc.teamcode.Utils.Shooting.LimelightAngle;
import org.firstinspires.ftc.teamcode.Utils.Shooting.LimelightMotionCompensator;
import org.firstinspires.ftc.teamcode.Utils.Shooting.LimelightMotionCompensator.ShotSolution;
import org.firstinspires.ftc.teamcode.Utils.Shooting.Turret;
import org.firstinspires.ftc.teamcode.Utils.Shooting.TurretConfigurator;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "other_opt", group = "Autonomous")
public class other_opt extends LinearOpMode2026 {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(60, 132, 270);
    private final Pose point1 = poseFactory.of(12.0, 120.0, 180);
    private final Pose point1Control1 = poseFactory.of(46.2072, 113.3212, 0);

    // Bailout: at BAIL_TIME_MS after start, drop the routine and drive straight to park
    private static final long BAIL_TIME_MS = 25000;
    boolean notBailed = true;

    // Shooting (same setup as AutoDriveMain)
    public Turret turretAimer;
    DistanceToHive distanceToHive;
    AprilTagClusterTargeting hiveTargeting;
    LimelightMotionCompensator motionCompensator;
    ShotSolution currentSolution;
    TurretConfigurator turretConfigurator;
    double goalDistance;

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                waitForLimelightTx(1500),
                shoot(),
                follow(follower, path1())
        );
    }

    @Override
    public void runOpMode() {
        config();
        // Turret starting angle copied from AutoDriveMain; check it matches how the turret sits at this start pose
        turretAimer = new Turret(turret, posGet, MiscUtils.getMotorPositionFromDegrees(0, 537.7));
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
        turretConfigurator = new TurretConfigurator(this);
        distanceToHive = new DistanceToHive(posGet);
        motionCompensator = new LimelightMotionCompensator(posGet, distanceToHive);

        hiveTargeting = new AprilTagClusterTargeting();
        hiveTargeting.setTiltServo(lla);
        hiveTargeting.setDistanceToHive(distanceToHive);
        hiveTargeting.allowedHives.add(isCurrentAllienceRed
                ? AprilTagClusterTargeting.RED_SCORING
                : AprilTagClusterTargeting.BLUE_SCORING);

        waitForStart();
        long startTime = System.currentTimeMillis();
        intake.setPower(0);
        lift.setPower(-0.1);

        // Spin the flywheel up (and aim) before starting the routine
        double wantedVelocity = 1000;
        while (opModeIsActive() && Math.abs(flywheel.getVelocity() - wantedVelocity) > 80) {
            goalDistance = getGoalDistance();
            wantedVelocity = turretConfigurator.runTurret(goalDistance);

            telemetry.addData("Flywheel velocity", flywheel.getVelocity());
            telemetry.addData("Wanted velocity", wantedVelocity);
            if (notBailed) DoTurret();

            telemetry.update();
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

            if (System.currentTimeMillis() - startTime > BAIL_TIME_MS && notBailed) {
                bail();
            }

            double shotDist = (currentSolution != null) ? currentSolution.virtualDistance : distanceToHive.GetDistance();
            lla.setPosition(LimelightAngle.getAngle(shotDist));
            turretConfigurator.runTurret(shotDist);

            if (notBailed) {
                DoTurret();
            }

            telemetry.update();
        }
    }

    public Path path1() {

        return Paths.curve(start, point1Control1, point1).constant(point1);
    }

    public double getGoalDistance() {
        double limelightDistance = -1;
        try {
            limelightDistance = hiveTargeting.getDistanceInches(); // -1 if no target / no 3D / unbelievable
        } catch (Exception e) {
            telemetry.addData("Cluster distance error", e.toString());
        }
        return limelightDistance > 0 ? limelightDistance : distanceToHive.GetDistance();
    }

    public Command shoot(){
        return  sequential(
                instant(() -> {
                    goalDistance = getGoalDistance();
                    turretConfigurator.runTurret(goalDistance);
                    intake.setPower(INTAKE_POWER);
                    lift.setPower(LIFT_POWER);
                }),
                waitMs(1500),
                instant(() -> lift.setPower(-0.1))
        );
    }

    public void DoTurret() {
        currentSolution = motionCompensator.calculateSolution(hiveTargeting, turretAimer, limelight);

        turretAimer.ChangeAngleSetPosition(currentSolution.compensatedTx, (posGet != null) ? posGet.getVr() : 0);

        telemetry.addLine("tx: " + currentSolution.compensatedTx);

        turretAimer.CheckPositionAndSetPower(telemetry);
    }

    /** Aims the turret until tx < 0.5 deg, or gives up after timeoutMs. */
    public Command waitForLimelightTx(long timeoutMs) {
        final long[] startMs = {0};
        return sequential(
                instant(() -> startMs[0] = System.currentTimeMillis()),
                infinite(this::DoTurret).until(() -> {
                    LLResult result = limelight.getLatestResult();
                    boolean aimed = result != null && result.isValid()
                            && currentSolution != null && Math.abs(currentSolution.compensatedTx) < 0.5;
                    return aimed || System.currentTimeMillis() - startMs[0] > timeoutMs;
                })
        );
    }

    public void bail() {
        Scheduler.reset();
        follower.stop();
        Scheduler.schedule(
                instant(() -> {
                    turretAimer.SetTargetPosition(MiscUtils.getMotorPositionFromDegrees(-90, 537.7));
                }),
                follow(follower, path1()),
                instant(() -> {
                    MovementUtils.autoEndPose = follower.pose();
                })
        );
        notBailed = false;
    }
}
