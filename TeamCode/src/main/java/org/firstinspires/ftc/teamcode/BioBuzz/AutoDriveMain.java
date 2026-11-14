package org.firstinspires.ftc.teamcode.BioBuzz;
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
import com.pedropathing.paths.interpolator.Interpolator;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagsTurretTarget;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "AutoDriveMain", group = "Autonomous")
public class AutoDriveMain extends LinearOpMode2026 {
    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(57, 9, 180);
    private final Pose point1 = poseFactory.of(9.75, 9.5, 180);
    private final Pose point2 = poseFactory.of(64.9433, 131.1091, -90.8781);
    private final Pose point2Control1 = poseFactory.of(30.0, 29.9094, 0);
    private final Pose point2Control2 = poseFactory.of(0, 125.7339, 0);
    private final Pose point2Control3 = poseFactory.of(64.5333, 104.3615, 0);
    private final Pose point2Segment1Heading = poseFactory.of(64.9433, 131.1091, 180);
    private final Pose point3 = poseFactory.of(10, 106.6598, -178.837);
    private final Pose point3Control1 = poseFactory.of(64.8796, 98.922, 0);
    private final Pose point3Control2 = poseFactory.of(42.9879, 107.3295, 0);
    private final Pose path4 = poseFactory.of(57, 10, -178.9092);
    private final Pose path4Control1 = poseFactory.of(28.8077, 106.8628, 0);
    private final Pose path4Control2 = poseFactory.of(44.3732, 62.4997, 0);
    private final Pose path4Control3 = poseFactory.of(7.1052, 9.05, 0);
//    private static final double HIVE_TY_THRESHOLD = 5.0;

    public AprilTagsTurretTarget aprilTagsTurretTarget;

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                waitMs(500),
                shoot(),
                follow(follower, corner()),
                waitMs(500),
                goBackIfHiveIsNotDown(),
                turnIntakeOff(),
                waitMs(500),
                follow(follower, crossField()),
//                waitForLimelight(),
                waitMs(100),
                waitForLimelightTx(),
                shoot(),
                follow(follower, park()),
                conditional( () -> isCompetition, //when thing is set in config to return home
                        follow(follower, toStart()),
                        instant(() -> {}) // runs if the condition is false (does nothing)
                )
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
        intake.setPower(0);

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
            telemetry.addData("ty", limelight.getLatestResult().getTy());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            DoTurret();

            telemetry.update();
        }
    }

    public Command turnIntakeOff(){
        return instant(() -> intake.setPower(0));
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
    public Path toStart() {
        return Paths.curve(point3, path4Control1, path4Control2, path4Control3, path4).reverseTangent();
    }

    public Command shoot(){
        return  sequential(
            instant(() -> intake.setPower(INTAKE_POWER)),
            instant(() -> lift.setPower(LIFT_POWER)),
            waitMs(1500),
            instant(() -> lift.setPower(0))
        );
    }

    public Path backToStart(){
        return Paths.path(Paths.line(point1, start).constant(start));
    }

//    public Command waitForLimelight() {
//        return run(() -> {
//            DoTurret();
//        }).until(() -> {
//            LLResult result = limelight.getLatestResult();
//            return result != null && result.isValid();
//        });
//    }

    public void DoTurret(){
        // This will turn the turret to the april tags (if it sees it)

        double tx = aprilTagsTurretTarget.UpdateTxValue(limelight);

        aprilTagsTurretTarget.ChangeAngleSetPosition(tx, posGet.getVr());

        double positionChange = MiscUtils.getPositionFromDegrees(tx, 537.7);

        telemetry.addLine("tx: " + tx);
        telemetry.addLine("Amount of Position changes: " + positionChange);

        aprilTagsTurretTarget.CheckPositionAndSetPower(telemetry);
    }

    public Command waitForLimelightTx() {
        // Keeps updating the turret position while waiting
        return infinite(this::DoTurret).until(() -> {
            double tx = aprilTagsTurretTarget.UpdateTxValue(limelight);
            LLResult result = limelight.getLatestResult();

            // Ensure the result is valid and tx is within ±0.5 degrees of center
            return result != null && result.isValid() && Math.abs(tx) < 0.5;
        });
    }

//    public Command goBackIfHiveIsNotDown() {
//        return infinite(() -> {
//            LLResult result = limelight.getLatestResult();
//            if (result != null && result.isValid()) {
//                double currentTy = result.getTy();
//                telemetry.addData("Checked ty", currentTy);
//
//                schedule(sequential(
//                        follow(follower, backToStart()),
//                        shoot()
//                ));
//
////                if (currentTy > HIVE_TY_THRESHOLD) {
////                    // If ty is higher than expected, schedule the return path and shooting sequence
////
////                }
//            }
//        }).until(() -> true); // Runs once to evaluate the condition and trigger the fallback if needed
//    }

    public Command goBackIfHiveIsNotDown() {
        long[] startTime = {System.currentTimeMillis()};
        int[] validFrames = {0};
        double timeoutMs = 1000.0; // Check for up to 1 second
        int requiredValidFrames = 5; // Must see the tag valid for at least 5 frames

        return infinite(() -> {
            LLResult result = limelight.getLatestResult();
            if (result != null && result.isValid()) {
                validFrames[0]++;
                telemetry.addData("Valid Tag Frames", validFrames[0]);
            }
            DoTurret(); // Keep updating the turret while waiting/checking
        }).until(() -> {
            // If we've accumulated enough valid frames, trigger the return and shoot
            if (validFrames[0] >= requiredValidFrames) {
                schedule(sequential(
                        follow(follower, backToStart()),
                        shoot()
                ));
                return true; // Finish command
            }

            // If time expires without stable detection, move on
            if (System.currentTimeMillis() - startTime[0] > timeoutMs) {
                return true; // Finish command
            }

            return false; // Keep checking
        });
    }
}
