package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.algorithm.Algorithm;
import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
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
        c.xPodOffset.set(5.9019067719226745);
        c.yPodOffset.set(-6.673296230045829);
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
        Controller primaryTranslationalForward = Controller.proportional(0.28798671276586635);
        Controller secondaryTranslationalForward = Controller.proportional(0.10640339522029899);
        Controller primaryTranslationalLateral = Controller.proportional(0.43426538582288027);
        Controller secondaryTranslationalLateral = Controller.proportional(0.1604494562767351);

        c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
        c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

        c.coast.set(Controller.proportionalFeedforward(0.016573449182176143));
        c.brake.set(Controller.proportionalFeedforward(0.01408743180484972));

        c.headingFeedback.set(Controller.proportional(5.165971605158433));
        c.headingBrakeCoefficients.set(Vector2D.cartesian(0.049982865096007516, 0.00543194867400201));

        c.linearBrakeCoefficients.set(Matrix.diag(0.09196465507892648, 0.06187442160397041));
        c.quadraticBrakeCoefficients.set(Matrix.diag(8.446463888852603E-4, 0.0014219030404775122));

        c.maxAchievableForwardVelocity.set(62.51359819493623);
        c.maxAchievableStrafeVelocity.set(49.271228681464436);
        c.naturalForwardDeceleration.set(32.273723212837346);
        c.naturalStrafeDeceleration.set(73.08765219721323);
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
