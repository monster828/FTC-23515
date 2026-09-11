package org.firstinspires.ftc.teamcode.DriveTests_2026;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "PPChallenge_2_9_10_26")
@Configurable
public class PPChallenge_2_9_10_26 extends OpMode {
    private TelemetryManager panelsTelemetry;
    public Follower follower;

    private PathChain MainChain;

    public void buildPaths() {          //Don't forget to set start position! (public void init)
        MainChain = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(133.000, 133.000),
                                new Pose(81.515, 129.124),
                                new Pose(83.185, 95.867),
                                new Pose(67.361, 65.666)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierCurve(
                                new Pose(67.361, 65.666),
                                new Pose(64.011, 44.042),
                                new Pose(23.680, 5.243),
                                new Pose(96.766, 14.841)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierCurve(
                                new Pose(96.766, 14.841),
                                new Pose(136.413, 15.521),
                                new Pose(117.926, 47.178)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(135))
                .build();
    }

    public Command autoRoutine() {
        return sequential(
                follow(follower, MainChain, true)
        );
    }

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Scheduler.reset();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(9, 61.75, Math.toRadians(90)));
        buildPaths();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }
}