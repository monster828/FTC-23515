package org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class PinpointPosGet extends PositionGetter {
    private long _timeOfLastUpdate;
    private GoBildaPinpointDriver _pinpoint;
    private float _pinpointUpdateWait = 3;
    private RobotOdoSetup setup = RobotOdoSetup.PINPOINT2026;

    public PinpointPosGet(GoBildaPinpointDriver _pinpoint) {
        this._pinpoint = _pinpoint;
        _pinpoint.setEncoderResolution(Constants.pinpointConfig.podType.get());
        _pinpoint.setEncoderDirections(Constants.pinpointConfig.xPodDirection.get(),
                Constants.pinpointConfig.yPodDirection.get());

        _pinpoint.setOffsets(Constants.pinpointConfig.yPodOffset.get(),
                Constants.pinpointConfig.xPodOffset.get(),
                Constants.pinpointConfig.offsetUnits.get());

        _pinpoint.resetPosAndIMU();

        _timeOfLastUpdate = System.currentTimeMillis();
    }

    public PinpointPosGet(GoBildaPinpointDriver _pinpoint, RobotOdoSetup setup) {
        this._pinpoint = _pinpoint;
        _pinpoint.setEncoderResolution(Constants.pinpointConfig.podType.get());
        _pinpoint.setEncoderDirections(Constants.pinpointConfig.xPodDirection.get(),
                Constants.pinpointConfig.yPodDirection.get());

        _pinpoint.setOffsets(Constants.pinpointConfig.yPodOffset.get(),
                Constants.pinpointConfig.xPodOffset.get(),
                Constants.pinpointConfig.offsetUnits.get());

        _pinpoint.resetPosAndIMU();

        _timeOfLastUpdate = System.currentTimeMillis();
        this.setup = setup;
    }

    @Override
    public float[] getPos() {
        UpdatePinpoint();
        setup.given((float) _pinpoint.getPosX(DistanceUnit.INCH), (float) _pinpoint.getPosY(DistanceUnit.INCH), (float) _pinpoint.getHeading(AngleUnit.DEGREES));
        //2025 robot: -Y,X,-R
        //2026 test bot: Y,-X,-R
        return new float[] {
                setup.getX()+xOffset,
                setup.getY()+yOffset,
                setup.getR()
        };
    }

    @Override
    public Position getPosi() {
        UpdatePinpoint();
        setup.given((float) _pinpoint.getPosX(DistanceUnit.INCH), (float) _pinpoint.getPosY(DistanceUnit.INCH), (float) _pinpoint.getHeading(AngleUnit.DEGREES));
        return new Position(
                setup.getX()+xOffset,
                setup.getY()+yOffset,
                setup.getR()
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
        return new float[] {
                -(float) _pinpoint.getVelX(DistanceUnit.INCH),
                (float) _pinpoint.getVelY(DistanceUnit.INCH)
        };
    }

    @Override
    public float getV() {
        return 0;
    }
}
