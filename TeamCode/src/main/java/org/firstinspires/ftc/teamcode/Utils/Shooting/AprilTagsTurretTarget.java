package org.firstinspires.ftc.teamcode.Utils.Shooting;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;

public class AprilTagsTurretTarget {
    // Limelight Pipelines
    // 3 Blue
    // 4 Red

    public double kp = 0.035; // Ramps the speed
    public double ki = 0.000; // Removes error
    public double kd = 0.0015; // Damps movement

    private ElapsedTime dtTimer = new ElapsedTime();
    double lastTx = 0;
    double integralSum = 0;

    DcMotorEx dcMotorEx;

    int limelightPipeline;

    public double maxPosition = MiscUtils.getPositionFromDegrees(185, 537.7); //185
    public double minPosition = MiscUtils.getPositionFromDegrees(-185,537.7);
    public boolean waitTillNotSee = false;
    public boolean canSee = false;

    double newPosition = 0;

    public AprilTagsTurretTarget(DcMotorEx turretMotor){
        dcMotorEx = turretMotor;

        limelightPipeline = LinearOpMode2026.isCurrentAllienceRed ? 4 : 3;
    }
//    public AprilTagsTurretTarget(DcMotorEx turretMotor, Limelight3A limelight3A){
//        dcMotorEx = turretMotor;
//    }

//    /**
//     * Run this about every loop
//     * This will do calculations and move motor to get limelight's tx (the amount offset in degrees) and get it to 0
//     *
//     * @param limelight3A
//     * Pass in the limelight
//     * **/
//    public double UpdateAprilTagsResults(Limelight3A limelight3A, PositionGetter positionGetter){
//        if (limelight3A.getStatus().getPipelineIndex() != limelightPipeline){
//            limelight3A.pipelineSwitch(limelightPipeline);
//        }
//        LLResult limelightResult = limelight3A.getLatestResult();
//
//        if (limelightResult != null && limelightResult.isValid()){
//            double tx = UpdateTxValue(limelight3A);
//            double ty = limelightResult.getTy();
//
//            Pose3D targetPos = limelightResult.getBotpose();
//            Position position = targetPos.getPosition();
//            double limelightLatency = limelightResult.getCaptureLatency() * 1000.0;
//
//            // Get the x and y of the robot (based from the hive)
//            double x = position.x;
//            double y = position.z;
//
//            // Get velocity x and y
//            float[] velocity = positionGetter.getVxVy();
//            double vx = velocity[0];
//            double vy = velocity[1];
//
//            // Subtract the x and y by (velocity x and y multiplied by the speed we are moving the turret at)
//            double speedOfTurret = 120;  // Degrees per second
//            double currentTurretAngle = MiscUtils.getDegreesFromMotor(dcMotorEx.getCurrentPosition(), 537.7);
//            double desiredTurretAngle = tx + currentTurretAngle;
//            double angleError = Math.abs(desiredTurretAngle - currentTurretAngle);
//
//            double physicalMoveTime = 0;
//            if (speedOfTurret > 0){
//                physicalMoveTime = angleError / speedOfTurret;
//            }
//
//            double totalTimeDelay = limelightLatency + physicalMoveTime;
//
//            double nx = x - (vx * totalTimeDelay);
//            double ny = y - (vy * totalTimeDelay);
//
//            // Get distance (Pythagorean theorem)
//            double distance = Math.sqrt(nx * nx + ny * ny);
//
//            // Get the new angle
//            double newTx = Math.atan2(ny, nx);
//
//            // Run update motor based on new tx value
//            ChangeAngleSetPosition(newTx);
//
//            return newTx;
//        }else {
//            dcMotorEx.setPower(0);
//            dtTimer.reset();
//            lastTx = 0;
//        }
//
//        return 0;
//    }

    public double UpdateTxValue(Limelight3A limelight3A){
        if (limelight3A.getStatus().getPipelineIndex() != limelightPipeline){
            limelight3A.pipelineSwitch(limelightPipeline);
        }
        LLResult limelightResult = limelight3A.getLatestResult();

        if (limelightResult != null && limelightResult.isValid()){
            double tx = limelightResult.getTx(); // Horizontal  0 is middle
            canSee = true;
            return tx;
        }else {
            waitTillNotSee = false;
            canSee = false;
            return 0;
        }
    }

    /**
     * This is for PID tuning
     * Run this about every loop
     * This will do calculations and move motor to get tx (the amount offset in degrees) to 0
     *
     * @param tx
     * This value is for the amount the turret is off-set in degrees
     * **/
    public void ChangeAngle(double tx){
        UpdateMotor(tx);
    }

    public void ChangeAngleSetPosition(double tx, double rotationV){
        SetTargetPosition (dcMotorEx.getCurrentPosition() - MiscUtils.getPositionFromDegrees(tx * 1.04 + rotationV * 1, 537.7));
    }


    public void SetTargetPosition(double position){
        if (waitTillNotSee || !canSee){
            if (Math.abs(dcMotorEx.getCurrentPosition() - newPosition) < 400){
                waitTillNotSee = false;
            }
        }else {
            double positionFromAllAround = MiscUtils.getPositionFromDegrees(360, 537.7);
            newPosition = position;

            double range = maxPosition - minPosition;

            if (newPosition > maxPosition || newPosition < minPosition){
                waitTillNotSee = true;
                newPosition = minPosition + ((newPosition - minPosition) % range + range) % range;
            }

//            if (newPosition > maxPosition){
//                newPosition -= positionFromAllAround * (Math.floor((newPosition - maxPosition) / positionFromAllAround) + 1);
//                waitTillNotSee = true;
//            } else if (newPosition < minPosition) {
//                newPosition += positionFromAllAround * (Math.floor((newPosition - minPosition) / positionFromAllAround) + 1);
//                waitTillNotSee = true;
//            }

            dcMotorEx.setTargetPosition((int) newPosition);
        }
    }

    private void SetPositionBasedOnPosition(double target){
        dcMotorEx.setTargetPosition((int) target);
    }

    public void Reset(){
        dtTimer.reset();
        lastTx = 0;

    }

    private void UpdateMotor(double tx){
        double dt = dtTimer.seconds();
        dtTimer.reset();

        double targetVelocity = 0;
        if (dt > 0){
            targetVelocity = (tx - lastTx) / dt;
        }

        integralSum += tx * dt;

        integralSum = Math.max(-0.2 / ki, Math.min(-0.2 / ki, integralSum));

        double pTerm = kp * tx;
        double iTerm = ki * integralSum;
        double dTerm = kd * targetVelocity;

        double motorPower = pTerm + iTerm + dTerm;


        double kf = 0.03;
        if (Math.abs(tx) > 0.5){
            motorPower += Math.signum(tx) * kf;
        }

        motorPower = MiscUtils.Clamp(motorPower, -0.05, 0.05);

        dcMotorEx.setPower(motorPower * -1);

        lastTx = tx;
    }
}
