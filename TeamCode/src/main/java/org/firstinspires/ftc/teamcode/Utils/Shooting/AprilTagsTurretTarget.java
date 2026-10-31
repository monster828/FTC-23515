package org.firstinspires.ftc.teamcode.Utils.Shooting;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

public class AprilTagsTurretTarget {
    public double kp = 0.001; // Ramps the speed
    public double ki = 0.001; // Removes error
    public double kd = 0.005; // Damps movement

    double lastTime = 0;
    double lastTx = 0;
    double integralSum = 0;

    DcMotorEx dcMotorEx;

    public AprilTagsTurretTarget(DcMotorEx turretMotor, Limelight3A limelight3A){
        dcMotorEx = turretMotor;
    }

    /**
     * Run this about every loop
     * This will do calculations and move motor to get limelight's tx (the amount offset in degrees) and get it to 0
     *
     * @param limelight3A
     * Pass in the limelight
     * **/
    public void UpdateAprilTagsResults(Limelight3A limelight3A){
        LLResult limelightResult = limelight3A.getLatestResult();

        if (limelightResult != null && limelightResult.isValid()){
            double tx = limelightResult.getTx(); // Horizontal  0 is middle
            UpdateMotor(tx);
        }else {
            dcMotorEx.setPower(0);
            lastTime = System.currentTimeMillis() / 1000;
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

    private void UpdateMotor(double tx){
        double currentTime = System.currentTimeMillis() / 1000;
        double dt = currentTime - lastTime;

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

        motorPower = MiscUtils.Clamp(motorPower, -1, 1);

        dcMotorEx.setPower(motorPower);

        lastTx = tx;
        lastTime = currentTime;
    }
}
