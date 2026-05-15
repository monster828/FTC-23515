/*package org.firstinspires.ftc.teamcode.summerDriveTests;

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

/* Copy this into a new opmode to begin using Ivy */
/*@Autonomous(name = "ivyCodeBase", group = "Autonomous")
@Configurable // Panels
public class ivyCodeBase extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private PathChain //Line name here//; // Current autonomous path state (state machine)
    private PathChain //Line name here//; // Paths defined in the Paths class

    public Command autoRoutine() {
        return sequential(
                follow(follower, //Line name here//, true),
                follow(follower, //Line name here//, true)
        );
    }

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Scheduler.reset();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(23.4, 123.6, Math.toRadians(323))); //Start Pose, right it 0
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

        //Line name here// = follower.pathBuilder()


        //Line name here// = follower.pathBuilder()

    }
}*/