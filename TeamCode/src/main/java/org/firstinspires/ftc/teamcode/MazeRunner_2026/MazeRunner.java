package org.firstinspires.ftc.teamcode.MazeRunner_2026;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@TeleOp(name = "Maze Runner")
public class MazeRunner extends OpMode {
    MazeRunnerMind _mind;

    DcMotor _left, _right;
    DistanceSensor _sensorFront, _sensorLeft, _sensorRight;
    GoBildaPinpointDriver _pinpoint;

    boolean _isMovingToGrid;
    boolean _isTurning;
    int _turnHeadingDelta;
    double _targetAngle; // radians to complete (PI/2 or PI)

    double _startX, _startY, _startHeading;

    static final double DRIVE_POWER = 0.5;
    static final double TURN_POWER  = 0.4;
    static final double TILE_SIZE   = 24.0; // inches per grid square — tune this

    @Override
    public void init() {
        _left  = hardwareMap.get(DcMotor.class, "left");
        _right = hardwareMap.get(DcMotor.class, "right");

        _left.setDirection(DcMotor.Direction.REVERSE);
        _right.setDirection(DcMotor.Direction.FORWARD);

        for (DcMotor m : new DcMotor[]{_left, _right}) {
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        _pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "POC");
        _pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        _pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                                       GoBildaPinpointDriver.EncoderDirection.REVERSED);
        _pinpoint.resetPosAndIMU();

        // TODO: fill in your sensor hardware map names
        _sensorFront = hardwareMap.get(DistanceSensor.class, "distFront");
        _sensorLeft  = hardwareMap.get(DistanceSensor.class, "distLeft");
        _sensorRight = hardwareMap.get(DistanceSensor.class, "distRight");

        _mind = new MazeRunnerMind(_sensorFront, _sensorLeft, _sensorRight, 0, 0, 7, 7);
    }

    @Override
    public void loop() {
        _pinpoint.update();

        if (_isTurning) {
            if (Math.abs(normalizeAngle(_pinpoint.getHeading(AngleUnit.RADIANS) - _startHeading)) >= _targetAngle) {
                stopMotors();
                _mind.currentHeading = (_mind.currentHeading + _turnHeadingDelta) % 4;
                _isTurning = false;
                driveOneCell();
            }
        } else if (_isMovingToGrid) {
            if (distanceTraveled() >= TILE_SIZE) {
                stopMotors();
                switch (_mind.currentHeading) {
                    case 0: _mind.currentY++; break; // North
                    case 1: _mind.currentX++; break; // East
                    case 2: _mind.currentY--; break; // South
                    case 3: _mind.currentX--; break; // West
                }
                _isMovingToGrid = false;
            }
        } else {
            int dir = _mind.DoMazeRunnerMind(_mind.currentX, _mind.currentY);

            if (dir == 0) {
                driveOneCell();
            } else {
                // dir=1 right (+1), dir=-1 left (+3 mod 4), dir=2 back (+2)
                _turnHeadingDelta = (dir == -1) ? 3 : dir;
                startTurn(dir);
                _isTurning = true;
            }
        }
    }

    /**This is to move the robot to the next square in the direction it previously decided**/
    void driveOneCell() {
        Pose2D pose2D = _pinpoint.getPosition();
        _startX = pose2D.getX(DistanceUnit.INCH);
        _startY = pose2D.getY(DistanceUnit.INCH);
        _left.setPower(DRIVE_POWER);
        _right.setPower(DRIVE_POWER);
        _isMovingToGrid = true;
    }

    /**Starts turning the robot to go in a better direction**/
    void startTurn(int dir) {
        _startHeading = _pinpoint.getHeading(AngleUnit.RADIANS);
        _targetAngle  = (dir == 2) ? Math.PI : Math.PI / 2;
        double leftPower  = (dir > 0) ?  TURN_POWER : -TURN_POWER;
        double rightPower = (dir > 0) ? -TURN_POWER :  TURN_POWER;
        _left.setPower(leftPower);
        _right.setPower(rightPower);
    }

    /**Stop the motors so we can make an educated guess where to move**/
    void stopMotors() {
        _left.setPower(0);
        _right.setPower(0);
    }

    /**Get the distance traveled**/
    double distanceTraveled() {
        Pose2D pose2D = _pinpoint.getPosition();
        double dx = pose2D.getX(DistanceUnit.INCH) - _startX;
        double dy = pose2D.getY(DistanceUnit.INCH) - _startY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**Converts the radians to -pie to pie**/
    double normalizeAngle(double angle) {
        while (angle >  Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }
}
