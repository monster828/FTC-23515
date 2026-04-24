package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static FollowerConstants followerConstants;

    static {
        followerConstants = new FollowerConstants();

        followerConstants.leftFrontMotorName = "FL";
        followerConstants.leftRearMotorName = "BL";
        followerConstants.rightFrontMotorName = "FR";
        followerConstants.rightRearMotorName = "BR";

        // Standard mecanum wiring — swap FORWARD/REVERSE on a side if the robot drives wrong
        followerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        followerConstants.leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        followerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        followerConstants.rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;

        followerConstants.mass = 4.5;
    }

    // These will be dialed in during tuning; leave as-is until you reach that step
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                // "pinpoint" must match the I2C device name you set in Driver Hub
                // xOffset = Y-pod offset left/right of center (inches, right = positive)
                // yOffset = X-pod offset forward/back of center (inches, forward = positive)
                .pinpointLocalizer("pinpoint", 2.25, 1.0)
                .pathConstraints(pathConstraints)
                .build();
    }
}
