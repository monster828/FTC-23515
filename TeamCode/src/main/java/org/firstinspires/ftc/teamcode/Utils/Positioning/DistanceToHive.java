package org.firstinspires.ftc.teamcode.Utils.Positioning;

import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;

public class DistanceToHive {

    PositionGetter positionGetter;

    public float upHiveX = 57.5f;
    public float upHiveY = 59.5f;
    public float downHiveX = 57.5f;
    public float downHiveY = 83.5f;

    public DistanceToHive(PositionGetter positionGetter){
        this.positionGetter = positionGetter;
    }

    public double GetTargetX() {
        return (positionGetter != null && positionGetter.y() > 64) ? downHiveX : upHiveX;
    }

    public double GetTargetY() {
        return (positionGetter != null && positionGetter.y() > 64) ? downHiveY : upHiveY;
    }

    public double GetDistance(){
        if (positionGetter == null) return 0;
        double rx = positionGetter.x();
        double ry = positionGetter.y();

        double tx = GetTargetX();
        double ty = GetTargetY();

        double dx = rx - tx;
        double dy = ry - ty;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public double GetFieldAngleToHive() {
        if (positionGetter == null) return 0;
        double rx = positionGetter.x();
        double ry = positionGetter.y();

        double targetX = GetTargetX();
        double targetY = GetTargetY();

        double dx = targetX - rx;
        double dy = targetY - ry;

        return Math.toDegrees(Math.atan2(dy, dx));
    }

    public double GetEstimatedTx() {
        if (positionGetter == null) return 0;
        return GetEstimatedTx(positionGetter.r(), 0);
    }

    public double GetEstimatedTx(double robotHeadingDeg, double turretRelativeAngleDeg) {
        double targetFieldAngle = GetFieldAngleToHive();
        double currentTurretFieldAngle = robotHeadingDeg + turretRelativeAngleDeg;

        double tx = targetFieldAngle - currentTurretFieldAngle;

        // Wrap to [-180, 180] degrees
        while (tx > 180.0) tx -= 360.0;
        while (tx < -180.0) tx += 360.0;

        return tx;
    }
}
