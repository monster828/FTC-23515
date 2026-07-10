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

@Autonomous(name = "Around the CONES! :)", group = "Autonomous")
@Configurable // Panels
public class AroundTheCones extends OpMode {

    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private PathChain avoid; // Current autonomous path state (state machine)

    public Command autoRoutine() {
        return sequential(
                follow(follower, avoid, true)
        );
    }


    @Override
    public void init(){
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Scheduler.reset();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(132, 9, Math.toRadians(90)));
        buildPaths();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void start(){
        schedule(autoRoutine());
    }

    @Override
    public void loop(){
        follower.update(); // Update Pedro Pathing
        Scheduler.execute();
        // Log values to Panels and Driver Station
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }

    public void buildPaths() {
        avoid = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Pose(132.000, 9.000),
                                new Pose(127.900, 68.188)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))
                .addPath(
                        new BezierLine(
                                new Pose(127.900, 68.188),
                                new Pose(70.933, 68.310)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(72))
                .addPath(
                        new BezierLine(
                                new Pose(70.933, 68.310),
                                new Pose(91.338, 129.586)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(72), Math.toRadians(-8))
                .addPath(
                        new BezierLine(
                                new Pose(91.338, 129.586),
                                new Pose(123.791, 124.966)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-8), Math.toRadians(-88))
                .addPath(
                        new BezierLine(
                                new Pose(123.791, 124.966),
                                new Pose(124.912, 85.488)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-88), Math.toRadians(80))
                .addPath(
                        new BezierLine(
                                new Pose(124.912, 85.488),
                                new Pose(49.658, 28.405)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(80), Math.toRadians(-50))
                .addPath(
                        new BezierLine(
                                new Pose(49.658, 28.405),
                                new Pose(23.983, 103.237)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-50), Math.toRadians(202))
                .addPath(
                        new BezierLine(
                                new Pose(23.983, 103.237),
                                new Pose(59.116, 117.654)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(202), Math.toRadians(0))
                .addPath(
                        new BezierLine(
                                new Pose(59.116, 117.654),
                                new Pose(84.231, 15.885)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                .addPath(
                        new BezierLine(
                                new Pose(84.231, 15.885),
                                new Pose(131.502, 9.168)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
                .build();
    }
}
