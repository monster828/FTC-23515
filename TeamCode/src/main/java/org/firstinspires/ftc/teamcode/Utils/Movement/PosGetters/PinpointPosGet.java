package org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.Utils.Movement.Testing.BestToleranceFinder;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class PinpointPosGet extends PositionGetter {
    private long _timeOfLastUpdate;
    private GoBildaPinpointDriver _pinpoint;
    private float _pinpointUpdateWait = 3;


    public PinpointPosGet(GoBildaPinpointDriver _pinpoint) {
        this._pinpoint = _pinpoint;
        _pinpoint.setEncoderResolution(Constants.pinpointConstants.encoderResolution);
        _pinpoint.setEncoderDirections(Constants.pinpointConstants.forwardEncoderDirection,
                Constants.pinpointConstants.strafeEncoderDirection);

        _pinpoint.setOffsets(Constants.pinpointConstants.strafePodX,
                Constants.pinpointConstants.forwardPodY,
                Constants.pinpointConstants.distanceUnit);

        _pinpoint.resetPosAndIMU();

        _timeOfLastUpdate = System.currentTimeMillis();

        // DELETE THIS
        BestToleranceFinder best = new BestToleranceFinder();
        best.SaveData();
    }

    @Override
    public float[] getPos() {
        UpdatePinpoint();
        return new float[] {
                -(float) _pinpoint.getPosY(DistanceUnit.INCH),
                (float) _pinpoint.getPosX(DistanceUnit.INCH),
                -(float) _pinpoint.getHeading(AngleUnit.DEGREES)
        };
    }

    @Override
    public Position getPosi() {
        UpdatePinpoint();
        return new Position(
                -(float) _pinpoint.getPosY(DistanceUnit.INCH),
                (float) _pinpoint.getPosX(DistanceUnit.INCH),
                -(float) _pinpoint.getHeading(AngleUnit.DEGREES)
        );
    }

    private void UpdatePinpoint(){
        if (System.currentTimeMillis() - _timeOfLastUpdate > _pinpointUpdateWait){
            _pinpoint.update();
            _timeOfLastUpdate = System.currentTimeMillis();
        }
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
