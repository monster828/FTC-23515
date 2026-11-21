package org.firstinspires.ftc.teamcode.pedropathPathsBiobuzz;


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

import org.firstinspires.ftc.teamcode.BioBuzz.AutoDriveMain;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.Movement.MovementUtils;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "other_opt", group = "Autonomous")
public class other_opt extends LinearOpMode2026 {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(60, 132, 270);
    private final Pose point1 = poseFactory.of(17.7869, 124.0861, 180);
    private final Pose point1Control1 = poseFactory.of(46.2072, 113.3212, 0);

    // Bailout: at BAIL_TIME_MS after start, drop the routine and drive straight to park
    private static final long BAIL_TIME_MS = 25000;
    boolean notBailed = true;

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                shoot(),
                follow(follower, path1())
        );
    }

    @Override
    public void runOpMode() {
        config();
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        long startTime = System.currentTimeMillis();
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

            if (System.currentTimeMillis() - startTime > BAIL_TIME_MS && notBailed) {
                bail();
            }

            telemetry.update();
        }
    }

    public Path path1() {

        return Paths.curve(start, point1Control1, point1).constant(point1);
    }
    public Command shoot(){
        return  sequential(
                instant(() -> intake.setPower(INTAKE_POWER)),
                instant(() -> lift.setPower(LIFT_POWER)),
                waitMs(1500),
                instant(() -> lift.setPower(-0.1))
        );
    }

    public void bail() {
        Scheduler.reset();
        follower.stop();
        Scheduler.schedule(
                follow(follower, path1()),
                instant(() -> {
                    MovementUtils.autoEndPose = follower.pose();
                })
        );
        notBailed = false;
    }
}

