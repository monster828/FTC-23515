package org.firstinspires.ftc.teamcode.Utils.Shooting;

public class LimelightAngle {
    // Inputs (column 2), must be in increasing order
    private static final double[] DIST  = {37.75, 46.5, 52.0, 58.0, 67.75};
    // Outputs (column 1)
    private static final double[] ANGLE = {0.66,  0.67, 0.62, 0.65, 0.64};

    public static double getAngle(double distance) {
        if (distance <= DIST[0]) return ANGLE[0];
        int last = DIST.length - 1;
        if (distance >= DIST[last]) return ANGLE[last];

        int i = 0;
        while (distance > DIST[i + 1]) i++;

        double t = (distance - DIST[i]) / (DIST[i + 1] - DIST[i]);
        double s = t * t * (3 - 2 * t);           // smoothstep, stays in 0..1
        return ANGLE[i] + (ANGLE[i + 1] - ANGLE[i]) * s;
    }
}
