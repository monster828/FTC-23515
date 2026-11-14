package org.firstinspires.ftc.teamcode.Tests.DriveTests_2026;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous
public class PPPoseTest extends LinearOpMode2026 {
    public Follower follower;
    @Override
    public void runOpMode() throws InterruptedException {
        follower = Constants.create(hardwareMap);
        follower.setPose(new Pose(9,9,Math.toRadians(90)));
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("X",follower.pose().x());
            telemetry.addData("Y",follower.pose().y());
            telemetry.addData("R",90-Math.toDegrees(follower.pose().heading()));
            telemetry.update();
            follower.update();
        }
    }
}
