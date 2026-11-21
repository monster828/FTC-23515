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

import org.firstinspires.ftc.teamcode.Utils.Shooting.AprilTagClusterTargeting;
import org.firstinspires.ftc.teamcode.Utils.Shooting.LimelightMotionCompensator;
import org.firstinspires.ftc.teamcode.Utils.Shooting.LimelightMotionCompensator.ShotSolution;
import org.firstinspires.ftc.teamcode.Utils.Shooting.Turret;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.MovementUtils;
import org.firstinspires.ftc.teamcode.Utils.Positioning.DistanceToHive;
import org.firstinspires.ftc.teamcode.Utils.Shooting.TurretConfigurator;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "AutoDriveMain", group = "Autonomous")
public class AutoDriveMain extends LinearOpMode2026 {
    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(57, 9, 180);
    private final Pose point1 = poseFactory.of(9.5, 9.5, 180);
    private final Pose point2 = poseFactory.of(64.9433, 131.1091, -90.8781);
    private final Pose point2Control1 = poseFactory.of(30.0, 29.9094, 0);
    private final Pose point2Control2 = poseFactory.of(0, 125.7339, 0);
    private final Pose point2Control3 = poseFactory.of(64.5333, 104.3615, 0);
    private final Pose point3 = poseFactory.of(10.5, 120.6598, -178.837);
    private final Pose point3Control1 = poseFactory.of(64.8796, 98.922, 0);
    private final Pose point3Control2 = poseFactory.of(42.9879, 107.3295, 0);
    private final Pose path4 = poseFactory.of(57, 10, -178.9092);
    private final Pose path4Control1 = poseFactory.of(28.8077, 106.8628, 0);
    private final Pose path4Control2 = poseFactory.of(44.3732, 62.4997, 0);
    private final Pose path4Control3 = poseFactory.of(7.1052, 9.05, 0);
    private final Pose path5 = poseFactory.of(60.5, 132, -90);
    private final Pose path5Control1 = poseFactory.of(60.8783, 65.7508, 0);
    private final Pose path5Segment1Heading = poseFactory.of(60.5, 132, 180);
    private final Pose path5Segment3Start = poseFactory.of(60.5, 132, -89.8912);
    private final Pose path5Segment3End = poseFactory.of(60.5, 132, 270);

    public Turret turretAimer;
    DistanceToHive distanceToHive;
    AprilTagClusterTargeting hiveTargeting;
    LimelightMotionCompensator motionCompensator;
    ShotSolution currentSolution;

    boolean isBackAtStart = false;
    boolean notBailed = true;

    TurretConfigurator turretConfigurator;

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                waitMs(500),
                shoot(),
                follow(follower, corner()),
                waitMs(2000),
                goBackIfHiveIsNotDown(),
                waitMs(500),
                conditional( () -> isBackAtStart,
                        follow(follower, startToFlower()),
                        follow(follower, crossField())
                ),
                turnIntakeOff(),
                waitMs(100),
                waitForLimelightTx(),
                shoot(),
                follow(follower, park()),
                conditional( () -> !isCompetition,
                        follow(follower, toStart()),
                        instant(() -> {})
                ),
                instant(() -> {
                    MovementUtils.autoEndPose = follower.pose();
                })
        );
    }

    @Override
    public void runOpMode() {
        config();
        turretAimer = new Turret(turret, posGet, MiscUtils.getMotorPositionFromDegrees(90, 537.7));
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
        long start = System.currentTimeMillis();
        flywheel.setVelocity(2200 * TICKS_PER_REV / 60.0);
        hood.setPosition(0.2);
        intake.setPower(0);
        lift.setPower(-0.1);

        while (Math.abs(flywheel.getVelocity() - (2100 * TICKS_PER_REV / 60.0)) > 80){
            telemetry.addData("Flywheel velocity", flywheel.getVelocity());
            telemetry.addData("Wanted velocity", (2100 * TICKS_PER_REV / 60.0));
            if(notBailed) DoTurret();

            telemetry.update();
        }

        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());
            LLResult latestResult = limelight.getLatestResult();
            if (latestResult != null) {
                telemetry.addData("ty", latestResult.getTy());
            }

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }
            if(System.currentTimeMillis()-start > 25000 && notBailed) {
                bail();
            }

            double shotDist = (currentSolution != null) ? currentSolution.virtualDistance : distanceToHive.GetDistance();
            turretConfigurator.runTurret(shotDist);

            if (notBailed){
                DoTurret();
            }

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
    public Path startToFlower() {
        return Paths.curve(start, path5Control1, path5).heading(Interpolator.piecewise().until(0.01, Interpolator.constant(path5Segment1Heading)).until(0.9361, Interpolator.tangent.reverse()).until(1, Interpolator.linear(path5Segment3Start, path5Segment3End)));
    }
    public Command shoot(){
        return  sequential(
            instant(() -> intake.setPower(INTAKE_POWER)),
            instant(() -> lift.setPower(LIFT_POWER)),
            waitMs(1500),
            instant(() -> lift.setPower(-0.1))
        );
    }

    public Path backToStart(){
        return Paths.path(Paths.line(point1, start).constant(start));
    }

    public void DoTurret(){
        currentSolution = motionCompensator.calculateSolution(hiveTargeting, turretAimer, limelight);

        turretAimer.ChangeAngleSetPosition(currentSolution.compensatedTx, (posGet != null) ? posGet.getVr() : 0);

        double positionChange = MiscUtils.getMotorPositionFromDegrees(currentSolution.compensatedTx, 537.7);

        telemetry.addLine("tx: " + currentSolution.compensatedTx);
        telemetry.addLine("Amount of Position changes: " + positionChange);

        turretAimer.CheckPositionAndSetPower(telemetry);
    }

    public Command waitForLimelightTx() {
        final long[] startMS = {0};
        return infinite(this::DoTurret).until(() -> {
            DoTurret();
            LLResult result = limelight.getLatestResult();
            double tx = (currentSolution != null) ? currentSolution.compensatedTx : turretAimer.UpdateTxValue(limelight);

            return result != null && result.isValid() && Math.abs(tx) < 0.5;
        });
    }

    /** Same as waitForLimelightTx(), but gives up after timeoutMs so a hive that isn't visible can't stall the routine. */
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
                    MovementUtils.autoEndPose = follower.pose();
                }),
                follow(follower,park()),
                instant(() -> {
                    turretAimer.SetTargetPosition( MiscUtils.getMotorPositionFromDegrees(-180, 537.7));
                })
                );
        notBailed = false;
    }

    /**
     * Checks the hive from the corner (point1).
     * Hive still up  -> drive back to start, shoot, isBackAtStart = true  (then startToFlower).
     * Hive tipped    -> stay at the corner, isBackAtStart stays false     (then crossField, which starts at point1).
     */
    public Command goBackIfHiveIsNotDown() {
        return sequential(
                waitForLimelightTx(1500),
                conditional(this::hiveVisible,
                        sequential(
                                follow(follower, backToStart()),
                                waitForLimelightTx(),
                                shoot(),
                                instant(() -> {isBackAtStart = true;})
                        ),
                        instant(() -> {})
                )
        );
    }

    private boolean hiveVisible() {
        LLResult result = limelight.getLatestResult();
        return result != null && result.isValid();
    }
}
