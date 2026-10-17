package org.firstinspires.ftc.teamcode.Utils.Detection;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.BallsTargetingSystem;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class LimelightBallDetection {
    private Limelight3A limelight;

    public static double CAMERA_HEIGHT = 10;
    public static double LIMELIGHT_ANGLE = 30;

    public LimelightBallDetection(Limelight3A limelight3A){
        this.limelight = limelight3A;
        limelight.pipelineSwitch(0);
    }

    /**
     * DetectBalls() will detect balls that it finds in its current pipeline
     * **/
    public BallsTargetingSystem.Ball[] DetectBalls(Telemetry telemetry, boolean saveToFile){
        LLResult result = limelight.getLatestResult();
        BallsTargetingSystem.Ball[] balls = new BallsTargetingSystem.Ball[0];

        StringBuilder saveFileTXT = new StringBuilder("Balls: ");

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
            saveFileTXT.append("Ball Type: ").append(limelight.getStatus().getPipelineIndex() == 0 ? "Pollen" : limelight.getStatus().getPipelineIndex() == 1 ? "Nector_Blue" : "Nector_Red");

            for (int i = 0; i < colorTargets.size(); i++) {

                LLResultTypes.ColorResult colorTarget = colorTargets.get(i);
                double x = colorTarget.getTargetXDegrees(); // Where it is (left-right)
                double y = colorTarget.getTargetYDegrees(); // Where it is (up-down)
                double area = colorTarget.getTargetArea(); // size (0-100)
                double distanceToBall = DistanceToBall(y);
                double distanceToTheSide = DistanceToTheSide(x, distanceToBall);
                telemetry.addData("Color Target", "takes up " + area + "% of the image");

                balls[i] = new BallsTargetingSystem.Ball((float)x,(float)y, type == 25);

                saveFileTXT.append("Ball_").append(i);
                saveFileTXT.append("\nPostion: (").append(x).append(", ").append(y).append(")\nSize: ").append(area).append(" Distance To Ball: ").append(distanceToBall).append(" Distance To Side: ").append(distanceToTheSide);
                telemetry.addData("Distance To Ball: ", distanceToBall);
                telemetry.addData("Distance To The Side: ", distanceToTheSide);

                // TO DO: PROPERLY SET X AND Y
//                ball.x = (float) x;
//                ball.y = (float) y;
            }
        } else {
            telemetry.addData("Limelight", "No Targets");
        }

        if (saveToFile){
            int i = 0;

            while (MiscUtils.checkFile(MiscUtils.dataFolder + "/ballDetection_"+i+".txt")){
                i++;
            }

            MiscUtils.writeFile(MiscUtils.dataFolder + "/ballDetection_"+i+".txt", saveFileTXT.toString().getBytes(StandardCharsets.UTF_8));
        }

        return balls;
    }

    public double DistanceToBall(double ty){
        return (0 /*Target Height*/ - CAMERA_HEIGHT) / Math.tan(LIMELIGHT_ANGLE + ty);
    }

    public double DistanceToTheSide(double tx, double distance){
        return distance * Math.tan(tx);
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
