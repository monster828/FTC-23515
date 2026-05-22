package org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters;

import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class PinpointPosGet extends PositionGetter {

    GoBildaPinpointDriver pinpoint;

    public PinpointPosGet(GoBildaPinpointDriver pinpoint) {
        this.pinpoint = pinpoint;
        pinpoint.setEncoderResolution(Constants.pinpointConstants.encoderResolution);
        pinpoint.setEncoderDirections(Constants.pinpointConstants.forwardEncoderDirection,
                Constants.pinpointConstants.strafeEncoderDirection);

        pinpoint.setOffsets(Constants.pinpointConstants.strafePodX,
                Constants.pinpointConstants.forwardPodY,
                Constants.pinpointConstants.distanceUnit);

        pinpoint.resetPosAndIMU();
    }

    @Override
    public float[] getPos() {
        return new float[] {
                (float) pinpoint.getPosY(DistanceUnit.INCH),
                (float) pinpoint.getPosX(DistanceUnit.INCH),
                (float) pinpoint.getHeading(AngleUnit.DEGREES)
        };
    }

    @Override
    public Position getPosi() {
        return new Position(
                (float) pinpoint.getPosY(DistanceUnit.INCH),
                (float) pinpoint.getPosX(DistanceUnit.INCH),
                (float) pinpoint.getHeading(AngleUnit.DEGREES)
        );
    }

    @Override
    public float[] getVxVy() {
        return new float[0];
    }

    @Override
    public float getV() {
        return 0;
    }
}
