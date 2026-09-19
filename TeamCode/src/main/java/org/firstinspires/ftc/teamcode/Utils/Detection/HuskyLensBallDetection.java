package org.firstinspires.ftc.teamcode.Utils.Detection;


import com.qualcomm.hardware.dfrobot.HuskyLens;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class HuskyLensBallDetection {
    private HuskyLens _myHuskyLens;

    private int _lastClosestDistance = 100000;

    public HuskyLensBallDetection(HuskyLens _huskyLens){
        _myHuskyLens = _huskyLens;
    }

    /**
     * This will detect balls from _myHuskyLens. It will see color and distance, along with position.
     * **/
    public void Detect(Telemetry telemetry){
        HuskyLens.Block[] blocks = _myHuskyLens.blocks();

        HuskyLens.Block closestBlock = null;
        int closestDistance = 1000000;

        telemetry.addLine("Husky Lens Detection");

        for (HuskyLens.Block block : blocks) {
            // Read the parameters
            int id = block.id;
            int centerX = block.x;
            int centerY = block.y;
            int blockWidth = block.width;
            int blockHeight = block.height;

            // If positive, the ball is on the right.
            // If negative, the ball is on the left.
            int horizontalAllignment = 160 - centerX;

            int verticalAllignment = 120 - centerY;

            // Might be correct, will prioritize nector
            int sizeIndicator = (blockWidth + blockHeight) / 2;
            if (sizeIndicator == 0) sizeIndicator = 1;

            double K_SCALING = 25.0;
            double realWorldHorizontalOffset = (double) horizontalAllignment / sizeIndicator * K_SCALING;

            if(closestDistance > sizeIndicator){
                // New closest Distance
                closestBlock = block;
                closestDistance = sizeIndicator;
            }

            // 1. Calculate the actual physical distance forward (in inches or cm)
            // You need to calibrate these numbers based on testing!
            // Example: Formula mapping 'sizeIndicator' to real world forward inches
            //double forwardDistanceInches = 1200.0 / ((blockWidth + blockHeight) / 2.0);

            // 2. Calculate the exact angle of the ball from the center line
            // 160 pixels = 30 degrees max. 30.0 / 160.0 = 0.1875 degrees per pixel.
            //int horizontalPixelError = centerX - 160;
            //double angleDegrees = horizontalPixelError * 0.1875;

            // 3. Use trigonometry to find the actual horizontal offset in inches
            // double realWorldHorizontalOffset = forwardDistanceInches * Math.tan(Math.toRadians(angleDegrees));


            // Send to telemetry or process for robot control
            telemetry.addData("Ball ID", id);
            telemetry.addData("Center X", centerX);
            telemetry.addData("Width", blockWidth);
            telemetry.addData("Height", blockHeight);
            telemetry.addData("Horizontal Allignement", horizontalAllignment);
            telemetry.addData("Vertical Allignement", verticalAllignment);
            telemetry.addData("Distance", sizeIndicator);
        }

        // Plus 5?
        if (_lastClosestDistance + 5 < closestDistance){
            // If it is going up in distance either we are not going after the ball or the ball is
                // too close to be seen
            telemetry.addLine("New ball?");
        }else {
            _lastClosestDistance = closestDistance;
        }
    }
}
