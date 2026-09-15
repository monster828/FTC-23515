package org.firstinspires.ftc.teamcode.BioBuzz;

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

@Autonomous(name = "BioBuzz_Auto_Outline", group = "Autonomous")
public class BioBuzz_Auto_Outline extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 9, 180);
    private final Pose path1 = poseFactory.of(10, 9, 180);
    private final Pose path2 = poseFactory.of(16.6473, 16.777, 180);
    private final Pose point3 = poseFactory.of(59.1314, 123.1929, 270);
    private final Pose point3Control1 = poseFactory.of(39.7317, 51.0588, 0);
    private final Pose point3Control2 = poseFactory.of(18.3237, 112.1079, 0);
    private final Pose point4 = poseFactory.of(61.0456, 131, 270);
    private final Pose point5 = poseFactory.of(46.592, 125.2898, 90);
    private final Pose point5Control1 = poseFactory.of(61.5443, 120.4827, 0);
    private final Pose point6 = poseFactory.of(46.5705, 128.7324, 90.3568);
    private final Pose point7Start = poseFactory.of(46.5705, 128.7324, 90);
    private final Pose point7 = poseFactory.of(12.7365, 119.4378, 180);
    private final Pose point7Control1 = poseFactory.of(34.4073, 111.5498, 0);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, path2()),
                follow(follower, path3())
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

    public Path path1() {
        return path(line(start, path1).tangent(), line(path1, path2).constant(path2), curve(path2, point3Control1, point3Control2, point3).constant(point3), line(point3, point4).constant(point4));
    }

    public Path path2() {
        return path(curve(point4, point5Control1, point5).linear(point4, point5), line(point5, point6).tangent());
    }

    public Path path3() {
        return curve(point7Start, point7Control1, point7).linear(point7Start, point7);
    }
}

