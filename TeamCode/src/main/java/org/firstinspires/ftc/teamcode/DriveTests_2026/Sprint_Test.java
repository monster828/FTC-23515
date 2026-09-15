package org.firstinspires.ftc.teamcode.DriveTests_2026;

import static com.pedropathing.api.Paths.*;

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

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Sprint_Test", group = "Autonomous")
public class Sprint_Test extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees(); //Set points for movement

    private final Pose start = poseFactory.of(33, 9, 90);
    private final Pose path1 = poseFactory.of(33, 132, 90);
    private final Pose point2 = poseFactory.of(33, 9, 90);

    // Autonomous routine: each "Path" is a stretch between two points, movement heading determined per "Path"
    public Path path1() {
        return line(start, path1).tangent();
    }
    public Path path2() {
        return line(path1,point2).reverseTangent();
    }
    public Command autoRoutine() { //Calls paths in a sequence, able to add pauses between each
        return sequential(
                follow(follower, path1()),
                follow(follower, path2())
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