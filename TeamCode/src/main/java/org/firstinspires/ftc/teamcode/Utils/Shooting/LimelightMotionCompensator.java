package org.firstinspires.ftc.teamcode.Utils.Shooting;

import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;
import org.firstinspires.ftc.teamcode.Utils.Positioning.DistanceToHive;

/**
 * Motion compensation class that uses Limelight distance and tx directly
 * to calculate lead aiming and virtual shot distance while driving.
 */
public class LimelightMotionCompensator {

    public double ballVelocityInchesPerSec = 280.0; // Horizontal ball speed (~23 ft/s)
    public double latencySeconds = 0.040;            // Total latency (~40ms)
    public double leadMultiplier = 1.0;              // Lead tuning scalar
    public boolean enabled = true;                   // Motion comp toggle

    private final PositionGetter positionGetter;
    private final DistanceToHive distanceToHive;

    // Velocity filtering
    private double lastX = 0;
    private double lastY = 0;
    private long lastTimeMs = -1;
    private double vxFiltered = 0;
    private double vyFiltered = 0;
    private static final double ALPHA = 0.4;

    public static class ShotSolution {
        public double staticDistance;    // Distance from Limelight (or fallback odometry)
        public double virtualDistance;   // Distance to aim/power for (inches)
        public double staticTx;          // Raw tx from Limelight (or fallback odometry)
        public double compensatedTx;     // Final tx angle offset for turret (degrees)
        public double leadAngleDeg;      // Lead offset added to tx (degrees)
        public double timeOfFlight;      // Calculated flight time (seconds)
        public double robotVx;           // Robot field velocity X (in/s)
        public double robotVy;           // Robot field velocity Y (in/s)
        public double robotSpeed;        // Total linear speed (in/s)
        public boolean isFromLimelight;  // True if distance came from Limelight
        public boolean isCompensating;   // True if motion compensation was applied
    }

    public LimelightMotionCompensator(PositionGetter positionGetter, DistanceToHive distanceToHive) {
        this.positionGetter = positionGetter;
        this.distanceToHive = distanceToHive;
    }

    public void updateVelocity() {
        if (positionGetter == null) return;

        long now = System.currentTimeMillis();
        double currentX = positionGetter.x();
        double currentY = positionGetter.y();

        if (lastTimeMs > 0) {
            double dt = (now - lastTimeMs) / 1000.0;
            if (dt > 0.001 && dt < 0.2) {
                double rawVxDiff = (currentX - lastX) / dt;
                double rawVyDiff = (currentY - lastY) / dt;

                float[] vxvyOdo = positionGetter.getVxVy();
                double blendVx = (Math.abs(vxvyOdo[0]) < 150) ? vxvyOdo[0] : rawVxDiff;
                double blendVy = (Math.abs(vxvyOdo[1]) < 150) ? vxvyOdo[1] : rawVyDiff;

                double rawVx = 0.5 * rawVxDiff + 0.5 * blendVx;
                double rawVy = 0.5 * rawVyDiff + 0.5 * blendVy;

                vxFiltered = ALPHA * rawVx + (1.0 - ALPHA) * vxFiltered;
                vyFiltered = ALPHA * rawVy + (1.0 - ALPHA) * vyFiltered;
            }
        }

        lastX = currentX;
        lastY = currentY;
        lastTimeMs = now;
    }

    /**
     * Calculates motion compensation using Limelight cluster targeting.
     *
     * @param hiveTargeting AprilTagClusterTargeting instance
     * @param turretAimer Turret instance for fallback
     * @param limelight Limelight3A device
     * @return ShotSolution with motion-compensated distance and tx
     */
    public ShotSolution calculateSolution(AprilTagClusterTargeting hiveTargeting, Turret turretAimer, Limelight3A limelight) {
        updateVelocity();

        ShotSolution solution = new ShotSolution();
        solution.robotVx = vxFiltered;
        solution.robotVy = vyFiltered;
        solution.robotSpeed = Math.hypot(vxFiltered, vyFiltered);

        double rawDistance = -1;
        double rawTx = 0;
        boolean fromLimelight = false;

        // 1. Get raw values directly from Limelight
        if (hiveTargeting != null && hiveTargeting.update(limelight) && hiveTargeting.hasTarget()) {
            rawTx = hiveTargeting.getTx();
            rawDistance = hiveTargeting.getDistanceInches();
            fromLimelight = rawDistance > 0;
        }

        // Fallback to turretAimer / odometry if Limelight distance unavailable
        if (!fromLimelight) {
            if (turretAimer != null) {
                rawTx = turretAimer.UpdateTxValue(limelight);
            }
            if (distanceToHive != null) {
                rawDistance = distanceToHive.GetDistance();
            } else {
                rawDistance = 60.0;
            }
        }

        solution.staticDistance = rawDistance;
        solution.staticTx = rawTx;
        solution.isFromLimelight = fromLimelight;

        // If motion compensation is disabled or robot is still
        if (!enabled || (solution.robotSpeed < 1.0 && (positionGetter == null || Math.abs(positionGetter.getVr()) < 1.0))) {
            solution.virtualDistance = rawDistance;
            solution.compensatedTx = rawTx;
            solution.leadAngleDeg = 0;
            solution.timeOfFlight = rawDistance / Math.max(100.0, ballVelocityInchesPerSec);
            solution.isCompensating = false;
            return solution;
        }

        // 2. Convert raw distance + raw tx into relative target vector in Robot Frame
        // Robot Frame: X_robot = right, Y_robot = forward
        double txRad = Math.toRadians(rawTx);
        double dxTargetRobot = rawDistance * Math.sin(txRad);
        double dyTargetRobot = rawDistance * Math.cos(txRad);

        // 3. Convert Field Velocity (vxFiltered, vyFiltered) into Robot Frame Velocity (vxRobot, vyRobot)
        double robotHeadingRad = Math.toRadians((positionGetter != null) ? positionGetter.r() : 0);
        double cosR = Math.cos(robotHeadingRad);
        double sinR = Math.sin(robotHeadingRad);

        // Field -> Robot frame transformation
        double vxRobot =  cosR * vxFiltered + sinR * vyFiltered;
        double vyRobot = -sinR * vxFiltered + cosR * vyFiltered;

        // 4. Calculate Time of Flight & Prediction Time
        double tFly = rawDistance / Math.max(100.0, ballVelocityInchesPerSec);
        double totalPredictionTime = (tFly + latencySeconds) * leadMultiplier;

        // 5. Virtual Target Vector in Robot Frame
        double dxVirtualRobot = dxTargetRobot - vxRobot * totalPredictionTime;
        double dyVirtualRobot = dyTargetRobot - vyRobot * totalPredictionTime;

        // 6. Virtual Distance & Refinement
        double vDist = Math.hypot(dxVirtualRobot, dyVirtualRobot);

        // Refine with virtual distance (2nd order iteration)
        tFly = vDist / Math.max(100.0, ballVelocityInchesPerSec);
        totalPredictionTime = (tFly + latencySeconds) * leadMultiplier;

        dxVirtualRobot = dxTargetRobot - vxRobot * totalPredictionTime;
        dyVirtualRobot = dyTargetRobot - vyRobot * totalPredictionTime;
        vDist = Math.hypot(dxVirtualRobot, dyVirtualRobot);

        solution.virtualDistance = vDist;
        solution.timeOfFlight = tFly;

        // 7. Compensated Angle Offset (degrees)
        double compensatedTxRad = Math.atan2(dxVirtualRobot, dyVirtualRobot);
        solution.compensatedTx = Math.toDegrees(compensatedTxRad);
        solution.leadAngleDeg = solution.compensatedTx - rawTx;
        solution.isCompensating = true;

        return solution;
    }

    public boolean toggleEnabled() {
        enabled = !enabled;
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void addTelemetry(Telemetry telemetry, ShotSolution solution) {
        if (telemetry == null) return;
        telemetry.addLine("=== Limelight Motion Compensation ===");
        telemetry.addData("Status", enabled ? (solution.isCompensating ? "ACTIVE" : "ON (Stationary)") : "OFF (Disabled)");
        telemetry.addData("Distance Source", solution.isFromLimelight ? "Limelight Vision" : "Odometry Fallback");
        if (solution != null) {
            telemetry.addData("Virtual Distance", "%.2f in (raw: %.2f in)", solution.virtualDistance, solution.staticDistance);
            telemetry.addData("Compensated tx", "%.2f deg (raw tx: %.2f, lead: %.2f)", solution.compensatedTx, solution.staticTx, solution.leadAngleDeg);
            telemetry.addData("Robot Speed", "%.1f in/s (Vx: %.1f, Vy: %.1f)", solution.robotSpeed, solution.robotVx, solution.robotVy);
            telemetry.addData("Flight Time", "%.3f s", solution.timeOfFlight);
        }
    }
}
