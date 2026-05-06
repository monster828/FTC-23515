package org.firstinspires.ftc.teamcode.summerDriveTests;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;

@Autonomous(name = "pedroPathingAutoTest")
@Configurable // Panels
public class pedroPathingAutoTest extends OpMode {
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
        public PathChain ScoreSpikes;

        public Paths(Follower follower) {
            ScorePreloads = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(9.000, 9.000),
                                    new Pose(58.567, 16.626)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
                    .build();

            ScoreSpikes = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(58.567, 16.626),
                                    new Pose(39.431, 40.224),
                                    new Pose(14.058, 34.517)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierLine(
                                    new Pose(14.058, 34.517),
                                    new Pose(58.330, 16.724)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                    .addPath(
                            new BezierCurve(
                                    new Pose(58.330, 16.724),
                                    new Pose(51.889, 46.544),
                                    new Pose(55.983, 62.362),
                                    new Pose(10.472, 56.901)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierLine(
                                    new Pose(18.091, 83.052),
                                    new Pose(48.087, 108.819)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(135))
                    .addPath(
                            new BezierCurve(
                                    new Pose(44.996, 58.114),
                                    new Pose(53.652, 84.916),
                                    new Pose(18.091, 83.052)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .addPath(
                            new BezierLine(
                                    new Pose(10.472, 56.901),
                                    new Pose(44.996, 58.114)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();
        }
    }

    public int autonomousPathUpdate() {
        // Add your state machine Here
        // Access paths with paths.pathName
        // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine
        return 0;
    }
}