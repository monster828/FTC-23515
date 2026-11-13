package org.firstinspires.ftc.teamcode.BioBuzz;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.pedropathing.paths.interpolator.Interpolator;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
@Autonomous(name = "AutoDriveMain", group = "Autonomous")
public class AutoDriveMain extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(59, 9, 180);
    private final Pose point1 = poseFactory.of(10, 10, 180);
    private final Pose point2 = poseFactory.of(64.9433, 131.1091, -90.8781);
    private final Pose point2Control1 = poseFactory.of(21.6987, 29.9094, 0);
    private final Pose point2Control2 = poseFactory.of(0, 125.7339, 0);
    private final Pose point2Control3 = poseFactory.of(64.5333, 104.3615, 0);
    private final Pose point2Segment1Heading = poseFactory.of(64.9433, 131.1091, 180);
    private final Pose point3 = poseFactory.of(10, 106.6598, -178.837);
    private final Pose point3Control1 = poseFactory.of(64.8796, 98.922, 0);
    private final Pose point3Control2 = poseFactory.of(42.9879, 107.3295, 0);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, corner()),
                waitMs(500),
                follow(follower, crossField()),
                waitMs(2000),
                follow(follower, park())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path corner() {
        return Paths.path(Paths.line(start, point1).constant(point1));
    }
    public Path crossField() {
        return Paths.path(Paths.curve(point1, point2Control1, point2Control2, point2Control3, point2).heading(Interpolator.tangent.reverse()));
    }
    public Path park() {
        return Paths.path(Paths.curve(point2, point3Control1, point3Control2, point3).tangent());
    }
}
