package org.firstinspires.ftc.teamcode.DriveTests_2026;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "pedroPathingAutoTest_9_10_26")
@Configurable
public class pedroPathingAutoTest_9_10_26 extends OpMode {
    private TelemetryManager panelsTelemetry;
    public Follower follower;

    private PathChain MainChain;

    public void buildPaths() {          //Don't forget to set start position! (public void init)
        MainChain = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(71.000, 9.000),
                                new Pose(77.951, 62.843),
                                new Pose(14.734, 38.916),
                                new Pose(9.000, 71.000)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .addPath(
                        new BezierCurve(
                                new Pose(9.000, 71.000),
                                new Pose(23.383, 95.510),
                                new Pose(71.139, 85.865)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .addPath(
                        new BezierLine(
                                new Pose(71.139, 85.865),
                                new Pose(71.000, 9.000)
                        )
                )
                .setTangentHeadingInterpolation()
                .setReversed()
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
        follower.setStartingPose(new Pose(71, 9, Math.toRadians(90)));
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