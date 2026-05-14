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

@Autonomous(name = "far_Side_Start_PedroPathing", group = "Autonomous")
@Configurable // Panels
public class far_Side_Start_PedroPathing extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private PathChain scorePreloads; // Current autonomous path state (state machine)
    private PathChain getLines; // Paths defined in the Paths class

    public Command autoRoutine() {
        return sequential(
                follow(follower, scorePreloads, true),
                follow(follower, getLines, true)
        );
    }

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Scheduler.reset();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(23.4, 123.6, Math.toRadians(323)));
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
        follower.update(); // Update Pedro Pathing
        Scheduler.execute();
        // Log values to Panels and Driver Station
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }

    public void buildPaths() {

            scorePreloads = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(23.404, 123.598),
                                    new Pose(53.807, 106.913)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(323), Math.toRadians(90))
                    .build();

            getLines = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(53.807, 106.913),
                                    new Pose(44.695, 79.647),
                                    new Pose(20.027, 82.341)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierLine(
                                    new Pose(20.027, 82.341),
                                    new Pose(48.625, 82.991)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(143))
                    .addPath(
                            new BezierCurve(
                                    new Pose(48.625, 82.991),
                                    new Pose(49.389, 55.897),
                                    new Pose(18.804, 54.907)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierCurve(
                                    new Pose(18.804, 54.907),
                                    new Pose(62.499, 38.856),
                                    new Pose(58.178, 100.569)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(143))
                    .build();
        }
    }
