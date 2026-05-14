package org.firstinspires.ftc.teamcode.DriveTests_2026;

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

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous(name = "pedroPathingAutoTest_Ivy")
@Configurable
public class pedroPathingAutoTest_Ivy extends OpMode {
    private TelemetryManager panelsTelemetry;
    public Follower follower;

    private PathChain scorePreloads;
    private PathChain scoreSpikes;

    public void buildPaths() {
        scorePreloads = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(9.000, 9.000),
                        new Pose(58.567, 16.626)))
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
                .build();

        scoreSpikes = follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(58.567, 16.626),
                        new Pose(39.431, 40.224),
                        new Pose(14.058, 34.517)))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(
                        new Pose(14.058, 34.517),
                        new Pose(58.330, 16.724)))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                .addPath(new BezierCurve(
                        new Pose(58.330, 16.724),
                        new Pose(51.889, 46.544),
                        new Pose(55.983, 62.362),
                        new Pose(10.472, 56.901)))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(
                        new Pose(18.091, 83.052),
                        new Pose(48.087, 108.819)))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                .addPath(new BezierCurve(
                        new Pose(44.996, 58.114),
                        new Pose(53.652, 84.916),
                        new Pose(18.091, 83.052)))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(
                        new Pose(10.472, 56.901),
                        new Pose(44.996, 58.114)))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }

    public Command autoRoutine() {
        return sequential(
                follow(follower, scorePreloads, true),
                follow(follower, scoreSpikes, true)
        );
    }

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Scheduler.reset();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(9, 9, Math.toRadians(90)));
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