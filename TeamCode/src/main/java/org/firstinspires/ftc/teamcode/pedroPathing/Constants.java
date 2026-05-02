package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants;

    static {
        followerConstants = new FollowerConstants();
        followerConstants.mass = 12.7; //Kg
        followerConstants.centripetalScaling(0);
        followerConstants.forwardZeroPowerAcceleration(-39.107);
        followerConstants.lateralZeroPowerAcceleration(-58.84);
        followerConstants.predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(0.1, 0.065, 0.00158));
    }

    public static MecanumConstants mecanumConstants = new MecanumConstants()
            .xVelocity(65.5)
            .yVelocity(52.39)
            .maxPower(1)
            .leftFrontMotorName("FL")
            .leftRearMotorName("BL")
            .rightFrontMotorName("FR")
            .rightRearMotorName("BR")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);

    public static PinpointConstants pinpointConstants = new PinpointConstants()
            //Other Robot.hardwareMapName("pinpoint")
            .hardwareMapName("POC")
            // forwardPodY: forward/back position of the X (strafe-tracking) pod, forward = positive
            .forwardPodY(-6.078)
            // strafePodX: left/right position of the Y (forward-tracking) pod, left = positive
            .strafePodX(-5.222)
            .distanceUnit(DistanceUnit.INCH)
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(mecanumConstants)
                .pinpointLocalizer(pinpointConstants)
                .pathConstraints(pathConstraints)
                .build();
    }
}
