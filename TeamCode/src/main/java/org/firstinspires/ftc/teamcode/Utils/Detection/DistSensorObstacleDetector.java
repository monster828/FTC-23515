package org.firstinspires.ftc.teamcode.Utils.Detection;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utils.ArrayDrawer;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

public class DistSensorObstacleDetector {

    DistanceSensor l, r;
    boolean[][] o = new boolean[141][141];
    float FOV = 25;
    float leftAngle = -10;
    float rightAngle = 10;
    PositionGetter posGet;
    public DistSensorObstacleDetector(DistanceSensor left, DistanceSensor right, boolean[][] obstacles, PositionGetter p) {
        l = left; r = right;
        o = obstacles;
        posGet = p;
    }

    public void update() {
        Position p = posGet.getPosi();
        if(l.getDistance(DistanceUnit.INCH) > 0.1f){
            int lX = (int) (Math.sin(leftAngle - (FOV / 2)) * l.getDistance(DistanceUnit.INCH));
            int rX = (int) (Math.sin(leftAngle + (FOV / 2)) * l.getDistance(DistanceUnit.INCH));
            int mY = (int) Math.min(Math.cos(leftAngle - (FOV / 2)) * l.getDistance(DistanceUnit.INCH), Math.cos(leftAngle + (FOV / 2)) * l.getDistance(DistanceUnit.INCH));
            Position lC = new Position(lX, mY, 0);
            Position rC = new Position((float) rX, (float) l.getDistance(DistanceUnit.INCH), 0);
            lC.rotateCoords(p.r());
            rC.rotateCoords(p.r());
            ArrayDrawer.drawRect(o, (int) lC.x(), (int) lC.y(), (int) (rC.x() - lC.x()), (int) (rC.y() - lC.y()), true);
        }
        if(r.getDistance(DistanceUnit.INCH) > 0.1f){
            int lX = (int) (Math.sin(rightAngle - (FOV / 2)) * r.getDistance(DistanceUnit.INCH));
            int rX = (int) (Math.sin(rightAngle + (FOV / 2)) * r.getDistance(DistanceUnit.INCH));
            int mY = (int) Math.min(Math.cos(rightAngle - (FOV / 2)) * r.getDistance(DistanceUnit.INCH), Math.cos(rightAngle + (FOV / 2)) * r.getDistance(DistanceUnit.INCH));
            Position lC = new Position(lX, mY, 0);
            Position rC = new Position((float) rX, (float) r.getDistance(DistanceUnit.INCH), 0);
            lC.rotateCoords(p.r());
            rC.rotateCoords(p.r());
            ArrayDrawer.drawRect(o, (int) lC.x(), (int) lC.y(), (int) (rC.x() - lC.x()), (int) (rC.y() - lC.y()), true);
        }
    }

}
