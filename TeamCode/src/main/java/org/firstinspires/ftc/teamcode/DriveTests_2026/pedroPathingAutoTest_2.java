package org.firstinspires.ftc.teamcode.DriveTests_2026;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "pedroPathingAutoTest2")
@Configurable // Panels
public class pedroPathingAutoTest_2 extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(9, 9, Math.toRadians(90)));

        paths = new Paths(follower); // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        pathState = autonomousPathUpdate(); // Update autonomous state machine

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }

    public static class Paths {
        public PathChain ScorePreloads;

        public Paths(Follower follower) {
            ScorePreloads = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(58.600, 16.600),
                                    new Pose(49.559, 39.842),
                                    new Pose(11.705, 35.532)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierCurve(
                                    new Pose(11.705, 35.532),
                                    new Pose(45.426, 30.659),
                                    new Pose(58.755, 22.081)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .addPath(
                            new BezierCurve(
                                    new Pose(58.755, 22.081),
                                    new Pose(49.893, 70.759),
                                    new Pose(8.274, 55.631)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierCurve(
                                    new Pose(8.274, 55.631),
                                    new Pose(38.030, 44.791),
                                    new Pose(58.690, 18.902)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierCurve(
                                    new Pose(58.690, 18.902),
                                    new Pose(61.487, 86.562),
                                    new Pose(14.778, 83.248)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierCurve(
                                    new Pose(14.778, 83.248),
                                    new Pose(44.415, 52.581),
                                    new Pose(59.159, 17.775)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierLine(
                                    new Pose(9.000, 9.000),
                                    new Pose(58.600, 16.600)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
                    .build();
        }
    }

    public int autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(paths.ScorePreloads);
                setPathState(1);
                break;
            case 1:

            /* You could check for
            - Follower State: "if(!follower.isBusy()) {}"
            - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
            - Robot Position: "if(follower.getPose().getX() > 36) {}"
            */

                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if(!follower.isBusy()) {
                    /* Score Preload */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if(!follower.isBusy()) {

                }
                break;
        }
        return pathState;
    }

    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
    }
}