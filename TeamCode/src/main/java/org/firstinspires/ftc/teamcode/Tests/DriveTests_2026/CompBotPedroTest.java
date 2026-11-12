package org.firstinspires.ftc.teamcode.Tests.DriveTests_2026;

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

@Autonomous(name = "CompBotPedroTest", group = "Autonomous")
public class CompBotPedroTest extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(59, 9, 180);
    private final Pose point1 = poseFactory.of(45.4636, 115.1071, -159.7389);
    private final Pose point1Control1 = poseFactory.of(13.6647, 36.6561, 0);
    private final Pose point1Control2 = poseFactory.of(23.2246, 106.8978, 0);
    private final Pose point1Segment2Start = poseFactory.of(45.4636, 115.1071, 180);
    // 295.8 deg = path1's reverse-tangent heading at 30% completion, so the hand-off to
    // tangent.reverse() is seamless. Recompute this if start or the path1 control points move.
    private final Pose point1Segment2End = poseFactory.of(45.4636, 115.1071, 295.8);
    // Must match where path1 ends (position AND heading) so there's no snap at the handoff.
    // path1 ends facing reverse-tangent = -159.7389 deg, i.e. the same as point1.
    private final Pose point2Start = point1;
    private final Pose point2 = poseFactory.of(61.7562, 132.5, 270);
    // Placed on the line from point1Control2 through point1 so the curve is tangent-continuous at point1.
    private final Pose point2Control1 = poseFactory.of(57.1256, 119.4122, 0);
    private final Pose point2Control2 = poseFactory.of(61.2997, 130.677, 0);


    // Autonomous routine: each "Path" is a stretch between two points, movement heading determined per "Path"
    public Path fieldCross() {
        return Paths.path(Paths.curve(start, point1Control1, point1Control2, point1).heading(Interpolator.piecewise().until(0.3, Interpolator.linear(point1Segment2Start, point1Segment2End)).until(1, Interpolator.tangent.reverse())), Paths.curve(point2Start, point2Control1, point2Control2, point2).linear(point2Start, point2));
    }.

    public Command autoRoutine() {
        return sequential(
                follow(follower, fieldCross())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        schedule(autoRoutine()); //Runs the sequential auto routine made above

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
}