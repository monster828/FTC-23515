package org.firstinspires.ftc.teamcode.Utils.Shooting;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

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

    public AprilTagsTurretTarget(DcMotorEx turretMotor){
        dcMotorEx = turretMotor;

        limelightPipeline = LinearOpMode2026.isCurrentAllienceRed ? 4 : 3;
    }
//    public AprilTagsTurretTarget(DcMotorEx turretMotor, Limelight3A limelight3A){
//        dcMotorEx = turretMotor;
//    }

    /**
     * Run this about every loop
     * This will do calculations and move motor to get limelight's tx (the amount offset in degrees) and get it to 0
     *
     * @param limelight3A
     * Pass in the limelight
     * **/
    public void UpdateAprilTagsResults(Limelight3A limelight3A){
        if (limelight3A.getStatus().getPipelineIndex() != limelightPipeline){
            limelight3A.pipelineSwitch(limelightPipeline);
        }
        LLResult limelightResult = limelight3A.getLatestResult();

        if (limelightResult != null && limelightResult.isValid()){
            double tx = limelightResult.getTx(); // Horizontal  0 is middle
            UpdateMotor(tx);
        }else {
            dcMotorEx.setPower(0);
            dtTimer.reset();
            lastTx = 0;
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

        motorPower = MiscUtils.Clamp(motorPower, -1, 1);

        dcMotorEx.setPower(motorPower);

        lastTx = tx;
    }
}
