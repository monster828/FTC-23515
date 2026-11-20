package org.firstinspires.ftc.teamcode.Utils.Shooting;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;

/**
 * Calculates motion-compensated shooting parameters (virtual distance and lead angle)
 * for hitting static field targets (BioBuzz Hive) while the robot is moving and turning.
 */
public class ShootingMotionCompensator {

    // Configurable parameters (tunable via TeleOp / Dashboard)
    public double ballVelocityInchesPerSec = 280.0; // Ball horizontal exit speed (~23 ft/s)
    public double latencySeconds = 0.040;            // Camera / processing / actuator lag (~40ms)
    public double leadMultiplier = 1.0;              // Tuning scalar (1.0 = standard physics)
    public boolean enabled = true;                   // Live toggle for motion compensation

    private final PositionGetter positionGetter;

    // BioBuzz Hive field positions (inches)
    public static final double UP_HIVE_X = 57.5;
    public static final double UP_HIVE_Y = 59.5;
    public static final double DOWN_HIVE_X = 57.5;
    public static final double DOWN_HIVE_Y = 83.5;

    // Velocity filtering state
    private double lastX = 0;
    private double lastY = 0;
    private long lastTimeMs = -1;
    private double vxFiltered = 0;
    private double vyFiltered = 0;
    private static final double ALPHA = 0.4; // Low pass filter weight for velocity

    public static class ShotSolution {
        public double staticDistance;    // Raw distance to static target (inches)
        public double virtualDistance;   // Motion-compensated distance for RPM/hood (inches)
        public double staticTx;          // Raw vision / odometry tx (degrees)
        public double compensatedTx;     // Final tx angle offset for turret (degrees)
        public double leadAngleDeg;      // Lead offset added to tx (degrees)
        public double timeOfFlight;      // Calculated ball flight time (seconds)
        public double robotVx;           // Robot field velocity X (in/s)
        public double robotVy;           // Robot field velocity Y (in/s)
        public double robotSpeed;        // Total linear speed (in/s)
        public double targetX;           // Physical target X (inches)
        public double targetY;           // Physical target Y (inches)
        public double virtualTargetX;    // Virtual target X (inches)
        public double virtualTargetY;    // Virtual target Y (inches)
        public boolean isCompensating;   // Whether compensation was applied
    }

    public ShootingMotionCompensator(PositionGetter positionGetter) {
        this.positionGetter = positionGetter;
    }

    /**
     * Updates velocity tracking. Call once per loop.
     */
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

                // Blend position differentiation with localizer reported velocity
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
     * Calculates the motion-compensated shot solution.
     *
     * @param rawDistance Raw distance to target (inches) - from vision or odometry
     * @param rawTx Raw horizontal angle offset to target (degrees)
     * @return ShotSolution containing virtual distance and compensated tx
     */
    public ShotSolution calculateSolution(double rawDistance, double rawTx) {
        updateVelocity();

        ShotSolution solution = new ShotSolution();
        solution.staticDistance = rawDistance;
        solution.staticTx = rawTx;
        solution.robotVx = vxFiltered;
        solution.robotVy = vyFiltered;
        solution.robotSpeed = Math.hypot(vxFiltered, vyFiltered);

        if (positionGetter == null) {
            solution.virtualDistance = rawDistance;
            solution.compensatedTx = rawTx;
            solution.leadAngleDeg = 0;
            solution.isCompensating = false;
            return solution;
        }

        // Target hive identification based on robot Y position
        double rx = positionGetter.x();
        double ry = positionGetter.y();
        if (ry > 64.0) {
            solution.targetX = DOWN_HIVE_X;
            solution.targetY = DOWN_HIVE_Y;
        } else {
            solution.targetX = UP_HIVE_X;
            solution.targetY = UP_HIVE_Y;
        }

        // If motion compensation is disabled or robot is virtually stationary
        if (!enabled || (solution.robotSpeed < 1.0 && Math.abs(positionGetter.getVr()) < 1.0)) {
            solution.virtualDistance = rawDistance;
            solution.leadAngleDeg = 0;
            solution.compensatedTx = rawTx;
            solution.timeOfFlight = rawDistance / Math.max(100.0, ballVelocityInchesPerSec);
            solution.virtualTargetX = solution.targetX;
            solution.virtualTargetY = solution.targetY;
            solution.isCompensating = false;
            return solution;
        }

        // --- MOTION COMPENSATION CALCULATIONS ---
        // 1. Static target vector from robot: D_static = P_target - P_robot
        double dxStatic = solution.targetX - rx;
        double dyStatic = solution.targetY - ry;
        double staticDist = Math.hypot(dxStatic, dyStatic);

        // Fallback distance if rawDistance is invalid
        double distForTFly = (rawDistance > 10.0 && rawDistance < 200.0) ? rawDistance : staticDist;

        // 2. Initial time of flight estimate + latency
        double tFly = distForTFly / Math.max(100.0, ballVelocityInchesPerSec);
        double totalPredictionTime = (tFly + latencySeconds) * leadMultiplier;

        // 3. Virtual target position: P_virtual = P_target - V_robot * totalPredictionTime
        double vTargetX = solution.targetX - vxFiltered * totalPredictionTime;
        double vTargetY = solution.targetY - vyFiltered * totalPredictionTime;

        // Vector from robot to virtual target: D_virtual = P_virtual - P_robot
        double dxVirtual = vTargetX - rx;
        double dyVirtual = vTargetY - ry;

        // 4. Virtual distance
        double vDist = Math.hypot(dxVirtual, dyVirtual);

        // Refine calculation with virtual distance (2nd order iteration)
        tFly = vDist / Math.max(100.0, ballVelocityInchesPerSec);
        totalPredictionTime = (tFly + latencySeconds) * leadMultiplier;

        vTargetX = solution.targetX - vxFiltered * totalPredictionTime;
        vTargetY = solution.targetY - vyFiltered * totalPredictionTime;
        dxVirtual = vTargetX - rx;
        dyVirtual = vTargetY - ry;
        vDist = Math.hypot(dxVirtual, dyVirtual);

        solution.virtualDistance = vDist;
        solution.virtualTargetX = vTargetX;
        solution.virtualTargetY = vTargetY;
        solution.timeOfFlight = tFly;

        // 5. Angular lead offset calculation
        double staticAngleRad = Math.atan2(dyStatic, dxStatic);
        double virtualAngleRad = Math.atan2(dyVirtual, dxVirtual);

        double deltaAngleRad = virtualAngleRad - staticAngleRad;
        // Normalize angle difference to [-PI, PI]
        while (deltaAngleRad > Math.PI) deltaAngleRad -= 2 * Math.PI;
        while (deltaAngleRad < -Math.PI) deltaAngleRad += 2 * Math.PI;

        solution.leadAngleDeg = Math.toDegrees(deltaAngleRad);
        solution.compensatedTx = rawTx + solution.leadAngleDeg;
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
        telemetry.addLine("=== Motion Compensated Shooting ===");
        telemetry.addData("Motion Comp Status", enabled ? (solution.isCompensating ? "ACTIVE" : "ON (Stationary)") : "OFF (Disabled)");
        if (solution != null) {
            telemetry.addData("Lead Angle (deg)", "%.2f", solution.leadAngleDeg);
            telemetry.addData("Compensated tx", "%.2f (raw: %.2f)", solution.compensatedTx, solution.staticTx);
            telemetry.addData("Virtual Distance", "%.2f in (raw: %.2f in)", solution.virtualDistance, solution.staticDistance);
            telemetry.addData("Robot Velocity", "Vx: %.1f, Vy: %.1f, Speed: %.1f in/s", solution.robotVx, solution.robotVy, solution.robotSpeed);
            telemetry.addData("Time of Flight", "%.3f s", solution.timeOfFlight);
        }
    }
}
