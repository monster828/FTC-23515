package org.firstinspires.ftc.teamcode.Utils.Shooting;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class AprilTagsTurretTarget {

    public double kp = 0.001; // Ramps the speed
    public double ki = 0.001; // Removes error
    public double kd = 0.005; // Damps movement

    double lastTime = 0;
    double lastTx = 0;
    double integralSum = 0;

    public void UpdateAprilTagsResults(Limelight3A limelight3A){
        LLResult limelightResult = limelight3A.getLatestResult();

        if (limelightResult != null && limelightResult.isValid()){
//            Pose3D botpose = limelightResult.getBotpose();

            double tx = limelightResult.getTx(); // Horizontal  0 is middle
            double currentTime = System.currentTimeMillis() / 1000;
            double dt = currentTime - lastTime;
//            double ta = limelightResult.getTa(); // Distance
//            double ty = limelightResult.getTy();

//            Position position = botpose.getPosition();

            double targetVelocity = 0;
            if (dt > 0){
                targetVelocity = (tx - lastTx) / dt;
            }

            integralSum += tx * dt;




        }
    }
}
