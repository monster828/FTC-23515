package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.algorithm.Algorithm;
import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizer;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig mecanumConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("FL");
        c.backLeftName.set("BL");
        c.frontRightName.set("FR");
        c.backRightName.set("BR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    // xPodOffset/yPodOffset carried over from the old forwardPodY/strafePodX values.
    // Re-run the Pinpoint Tuner (AutoTune) to confirm these before relying on them -
    // axis/sign conventions are not guaranteed to match the old FollowerConstants 1:1.
    public static PinpointConfig pinpointConfig = new PinpointConfig(c -> {
        c.name.set("POC");
        c.xPodOffset.set(-6.078);
        c.yPodOffset.set(-5.222);
        c.offsetUnits.set(DistanceUnit.INCH);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
    });

    // Only the values that carry over safely from the old FollowerConstants are set here
    // (achievable velocities/decelerations). Everything else - controller gains, braking
    // coefficients, heading PID, etc. - has no v2 equivalent and MUST be filled in by
    // running the Foresight Tuner (AutoTune) on the robot before this is used in a match.
    public static ForesightConfig foresightConfig = new ForesightConfig(c -> {
        c.maxAchievableForwardVelocity.set(65.5);
        c.maxAchievableStrafeVelocity.set(52.39);
        c.naturalForwardDeceleration.set(39.107);
        c.naturalStrafeDeceleration.set(58.84);
    });

    public static Drivetrain drivetrain(HardwareMap hardwareMap) {
        return new Mecanum(hardwareMap, mecanumConfig);
    }

    public static Localizer localizer(HardwareMap hardwareMap) {
        return new PinpointLocalizer(hardwareMap, pinpointConfig);
    }

    public static Algorithm algorithm() {
        return new Foresight(foresightConfig);
    }

    public static Follower create(HardwareMap hardwareMap) {
        return new Follower(localizer(hardwareMap), drivetrain(hardwareMap), algorithm());
    }
}
