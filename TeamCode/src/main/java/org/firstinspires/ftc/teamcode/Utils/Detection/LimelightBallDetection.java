package org.firstinspires.ftc.teamcode.Utils.Detection;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.BallsTargetingSystem;

import java.util.List;

public class LimelightBallDetection {
    private Limelight3A limelight;

    public LimelightBallDetection(Limelight3A limelight3A){
        this.limelight = limelight3A;
        limelight.pipelineSwitch(0);
    }

    /**
     * DetectBalls() will detect balls that it finds in its current pipeline
     * **/
    public BallsTargetingSystem.Ball[] DetectBalls(Telemetry telemetry){
        LLResult result = limelight.getLatestResult();
        BallsTargetingSystem.Ball[] balls = new BallsTargetingSystem.Ball[0];
        if (result != null && result.isValid()) {
            double tx = result.getTx(); // How far left or right the target is (degrees)
            double ty = result.getTy(); // How far up or down the target is (degrees)
            double ta = result.getTa(); // How big the target looks (0%-100% of the image)

            telemetry.addData("Target X", tx);
            telemetry.addData("Target Y", ty);
            telemetry.addData("Target Area", ta);

            List<LLResultTypes.ColorResult> colorTargets = result.getColorResults();

            balls = new BallsTargetingSystem.Ball[colorTargets.size()];

            // Pollen 25   Nector 41
            int type = limelight.getStatus().getPipelineIndex() == 0 ? 25 : 41;

            for (int i = 0; i < colorTargets.size(); i++) {
                LLResultTypes.ColorResult colorTarget = colorTargets.get(i);
                double x = colorTarget.getTargetXDegrees(); // Where it is (left-right)
                double y = colorTarget.getTargetYDegrees(); // Where it is (up-down)
                double area = colorTarget.getTargetArea(); // size (0-100)
                telemetry.addData("Color Target", "takes up " + area + "% of the image");

                BallsTargetingSystem.Ball ball = balls[i];
                ball.type = type;


                // TO DO: PROPERLY SET X AND Y
//                ball.x = (float) x;
//                ball.y = (float) y;
            }
        } else {
            telemetry.addData("Limelight", "No Targets");
        }

        return balls;
    }

    /**
     * Switch the pipeline to detecting pollen
     * **/
    public void SwitchToPollen(){
        if (limelight.getStatus().getPipelineIndex() != 0){
            limelight.pipelineSwitch(0);
        }
    }

    /**
     * Switch the pipeline to detecting nector
     * **/
    public void SwitchToNector(boolean isBlue){
        if (isBlue){
            if (limelight.getStatus().getPipelineIndex() != 1){
                limelight.pipelineSwitch(1);
            }
        }else {
            if (limelight.getStatus().getPipelineIndex() != 2){
                limelight.pipelineSwitch(2);
            }
        }
    }
}
