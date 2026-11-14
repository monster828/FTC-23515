package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
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
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("FL");
        c.backLeftName.set("BL");
        c.frontRightName.set("FR");
        c.backRightName.set("BR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
    });

    // xPodOffset/yPodOffset carried over from the old forwardPodY/strafePodX values.
    // Re-run the Pinpoint Tuner (AutoTune) to confirm these before relying on them -
    // axis/sign conventions are not guaranteed to match the old FollowerConstants 1:1.
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("POC");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(6.806982445904589);
        c.yPodOffset.set(-2.203244186761811);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    // Only the values that carry over safely from the old FollowerConstants are set here
    // (achievable velocities/decelerations). Everything else - controller gains, braking
    // coefficients, heading PID, etc. - has no v2 equivalent and MUST be filled in by
    // running the Foresight Tuner (AutoTune) on the robot before this is used in a match.
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.3370655043235006);
                Controller secondaryTranslationalForward = Controller.proportional(0.12453669729138182);
                Controller primaryTranslationalLateral = Controller.proportional(0.5053001160591044);
                Controller secondaryTranslationalLateral = Controller.proportional(0.18669489101607054);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01078474459498377));
                c.brake.set(Controller.proportionalFeedforward(0.009167032905736203));

                c.headingFeedback.set(Controller.proportional(5.248784375753949));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06847692487636643, 0.006441950704961584));

                c.linearBrakeCoefficients.set(Matrix.diag(0.13751313960879838, 0.06587407699462379));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0013667651661652088, 0.0019026154661783833));

                c.maxAchievableForwardVelocity.set(86.22548838149157);
                c.maxAchievableStrafeVelocity.set(66.22672181501261);
                c.naturalForwardDeceleration.set(34.92962865407743);
                c.naturalStrafeDeceleration.set(74.73422705806588);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}
