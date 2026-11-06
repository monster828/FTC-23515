package org.firstinspires.ftc.teamcode.Old_Pedro_Pathing;

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
                                new Pose(69.017, 130.818),
                                new Pose(106.909, 112.813),
                                new Pose(67.361, 65.666)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierCurve(
                                new Pose(67.361, 65.666),
                                new Pose(64.011, 44.042),
                                new Pose(24.104, 1.006),
                                new Pose(95.283, 12.511)
                        )
                )
                .setTangentHeadingInterpolation()
                .addPath(
                        new BezierCurve(
                                new Pose(95.283, 12.511),
                                new Pose(140.861, 21.664),
                                new Pose(112.690, 44.966)
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
        follower.setStartingPose(new Pose(133, 133, Math.toRadians(180)));
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