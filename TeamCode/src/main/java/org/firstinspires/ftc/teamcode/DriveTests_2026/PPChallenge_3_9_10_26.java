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
import static com.pedropathing.ivy.commands.Commands.*;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "PPChallenge_3_9_10_26")
@Configurable
public class PPChallenge_3_9_10_26 extends OpMode {
    private TelemetryManager panelsTelemetry;
    public Follower follower;

    private PathChain MainChain;
    private PathChain Chain2;

    public void buildPaths() {          //Don't forget to set start position! (public void init)
        MainChain = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Pose(9.000, 79.750),
                                new Pose(58.542, 80.272)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(270))
                .addPath(
                        new BezierCurve(
                                new Pose(58.542, 80.272),
                                new Pose(69.526, 51.270),
                                new Pose(12.326, 59.432)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(270))
                .build();

        Chain2 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(12.326, 59.432),
                                new Pose(6.576, 7.454),
                                new Pose(70.467, 12.458)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierCurve(
                                new Pose(70.467, 12.458),
                                new Pose(99.657, 9.381),
                                new Pose(70.954, 62.537),
                                new Pose(105.913, 48.296),
                                new Pose(131.201, 60.601)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .addPath(
                        new BezierCurve(
                                new Pose(131.201, 60.601),
                                new Pose(133.900, 104.361),
                                new Pose(71.657, 70.621),
                                new Pose(97.500, 101.055),
                                new Pose(86.183, 127.268)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .addPath(
                        new BezierLine(
                                new Pose(86.183, 127.268),
                                new Pose(52.106, 128.560)
                        )
                )
                .setTangentHeadingInterpolation()
                .build();
    }

    public Command autoRoutine() {
        return sequential(
                follow(follower, MainChain, true),
                waitMs(2000),
                follow(follower, Chain2, true)
        );
    }

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Scheduler.reset();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(9, 79.750, Math.toRadians(270)));
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