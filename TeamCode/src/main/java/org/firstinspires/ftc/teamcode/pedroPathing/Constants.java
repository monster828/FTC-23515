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

        // TODO: update these to match your Driver Hub motor configuration names
        followerConstants.leftFrontMotorName = "leftFront";
        followerConstants.leftRearMotorName = "leftRear";
        followerConstants.rightFrontMotorName = "rightFront";
        followerConstants.rightRearMotorName = "rightRear";

        // Standard mecanum wiring — swap FORWARD/REVERSE on a side if the robot drives wrong
        followerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        followerConstants.leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        followerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        followerConstants.rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;

        // TODO: weigh your fully-assembled robot and update this value (kg)
        followerConstants.mass = 10;
    }

    // These will be dialed in during tuning; leave as-is until you reach that step
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                // "pinpoint" must match the I2C device name you set in Driver Hub
                // xOffset = distance (inches) the Y-pod is left/right of robot center (+ = right)
                // yOffset = distance (inches) the X-pod is forward/back of robot center (+ = forward)
                // TODO: measure your pod offsets and replace the two 0.0 values
                .pinpointLocalizer("pinpoint", 0.0, 0.0)
                .pathConstraints(pathConstraints)
                .build();
    }
}
