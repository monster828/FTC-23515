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

@Autonomous(name = "PPChallenge_1_9_10_26")
@Configurable
public class PPChallenge_1_9_10_26 extends OpMode {
    private TelemetryManager panelsTelemetry;
    public Follower follower;

    private PathChain MainChain;

    public void buildPaths() {          //Don't forget to set start position! (public void init)
        MainChain = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(9.000, 61.750),
                                new Pose(10.131, 109.103),
                                new Pose(15.202, 130.756),
                                new Pose(70.218, 126.352)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierCurve(
                                new Pose(70.218, 126.352),
                                new Pose(139.718, 138.419),
                                new Pose(126.040, 69.756)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierCurve(
                                new Pose(126.040, 69.756),
                                new Pose(140.901, 0.776),
                                new Pose(71.057, 13.773)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierCurve(
                                new Pose(71.057, 13.773),
                                new Pose(16.752, 4.309),
                                new Pose(5.501, 26.187),
                                new Pose(12.000, 85.000)
                        )
                )
                .setTangentHeadingInterpolation()
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