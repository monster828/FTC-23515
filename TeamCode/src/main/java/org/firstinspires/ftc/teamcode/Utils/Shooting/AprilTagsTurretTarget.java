package org.firstinspires.ftc.teamcode.Utils.Shooting;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class AprilTagsTurretTarget {
    public void UpdateAprilTagsResults(Limelight3A limelight3A){
        LLResult limelightResult = limelight3A.getLatestResult();

        if (limelightResult != null && limelightResult.isValid()){
            // Pose3D botpose = limelightResult.getBotpose();


        }
    }
}
