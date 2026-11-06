package org.firstinspires.ftc.teamcode.DriveTests_2026;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.CompoundPath;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Around the CONES! :)", group = "Autonomous")
@Configurable // Panels
public class AroundTheCones extends OpMode {

    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private Path avoid; // Current autonomous path state (state machine)

    public Command autoRoutine() {
        return sequential(
                follow(follower, avoid)
        );
    }


    @Override
    public void init(){
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(new Pose(132, 9, Math.toRadians(90)));
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
        panelsTelemetry.debug("X", follower.pose().x());
        panelsTelemetry.debug("Y", follower.pose().y());
        panelsTelemetry.debug("Heading", follower.pose().heading());
        panelsTelemetry.update(telemetry);
    }

    public void buildPaths() {
        avoid = new CompoundPath(
                line(new Pose(132.000, 9.000), new Pose(127.900, 68.188))
                        .linear(Math.toRadians(90), Math.toRadians(180)),
                line(new Pose(127.900, 68.188), new Pose(70.933, 68.310))
                        .linear(Math.toRadians(180), Math.toRadians(72)),
                line(new Pose(70.933, 68.310), new Pose(91.338, 129.586))
                        .linear(Math.toRadians(72), Math.toRadians(-8)),
                line(new Pose(91.338, 129.586), new Pose(123.791, 124.966))
                        .linear(Math.toRadians(-8), Math.toRadians(-88)),
                line(new Pose(123.791, 124.966), new Pose(124.912, 85.488))
                        .linear(Math.toRadians(-88), Math.toRadians(80)),
                line(new Pose(124.912, 85.488), new Pose(49.658, 28.405))
                        .linear(Math.toRadians(80), Math.toRadians(-50)),
                line(new Pose(49.658, 28.405), new Pose(23.983, 103.237))
                        .linear(Math.toRadians(-50), Math.toRadians(202)),
                line(new Pose(23.983, 103.237), new Pose(59.116, 117.654))
                        .linear(Math.toRadians(202), Math.toRadians(0)),
                line(new Pose(59.116, 117.654), new Pose(84.231, 15.885))
                        .linear(Math.toRadians(0), Math.toRadians(90)),
                line(new Pose(84.231, 15.885), new Pose(131.502, 9.168))
                        .linear(Math.toRadians(90), Math.toRadians(90))
        );
    }
}
