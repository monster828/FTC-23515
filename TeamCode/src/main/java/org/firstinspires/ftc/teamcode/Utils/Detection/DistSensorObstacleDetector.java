package org.firstinspires.ftc.teamcode.Utils.Detection;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.ArrayDrawer;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

public class DistSensorObstacleDetector {

    CheckedDistanceSensor l, r;
    boolean[][] o = new boolean[141][141];
    float FOV = 25;
    float leftAngle = -10;
    float rightAngle = 10;
    PositionGetter posGet;
    public static Telemetry telemetry;
    public DistSensorObstacleDetector(DistanceSensor left, DistanceSensor right, boolean[][] obstacles, PositionGetter p) {
        l = new CheckedDistanceSensor(left,3); r = new CheckedDistanceSensor(right,3);
        o = obstacles;
        posGet = p;
    }
    public DistSensorObstacleDetector(CheckedDistanceSensor left, CheckedDistanceSensor right, boolean[][] obstacles, PositionGetter p) {
        l = left; r = right;
        o = obstacles;
        posGet = p;
    }

    public void update(float turretAngle) {
        Position p = new Position(70,70,0);
        if(posGet != null) p = posGet.getPosi();
        l.update(); r.update();
        double lD = l.getDistance();
        double rD = r.getDistance();
        telemetry.addData("LeftDist",lD);
        telemetry.addData("RightDist",rD);
        if(lD > 0.1f){
            int lX = (int) (Math.sin(Math.toRadians(leftAngle - (FOV / 2))) * lD);
            int rX = (int) (Math.sin(Math.toRadians(leftAngle + (FOV / 2))) * lD);
            int mY = (int) Math.min(Math.cos(Math.toRadians(leftAngle - (FOV / 2))) * lD, Math.cos(Math.toRadians(leftAngle + (FOV / 2))) * lD);
            Position lC = new Position(lX, mY, 0);
            Position rC = new Position((float) rX, (float) lD+18, 0);
            lC.rotateCoords(p.r()+turretAngle);
            rC.rotateCoords(p.r()+turretAngle);
            ArrayDrawer.drawRect(o, (int) (Math.min(lC.x(),rC.x())+p.x()), (int) (Math.min(lC.y(),rC.y())+p.y()), (int) Math.abs(rC.x() - lC.x()), (int) Math.abs(rC.y() - lC.y()), true);
        }
        if(rD > 0.1f){
            int lX = (int) (Math.sin(Math.toRadians(rightAngle - (FOV / 2))) * rD);
            int rX = (int) (Math.sin(Math.toRadians(rightAngle + (FOV / 2))) * rD);
            int mY = (int) Math.min(Math.cos(Math.toRadians(rightAngle - (FOV / 2))) * rD, Math.cos(Math.toRadians(rightAngle + (FOV / 2))) * rD);
            Position lC = new Position(lX, mY, 0);
            Position rC = new Position((float) rX, (float) rD+18, 0);
            lC.rotateCoords(p.r()+turretAngle);
            rC.rotateCoords(p.r()+turretAngle);
            ArrayDrawer.drawRect(o, (int) (Math.min(lC.x(),rC.x())+p.x()), (int) (Math.min(lC.y(),rC.y())), (int) Math.abs(rC.x() - lC.x()), (int) Math.abs(rC.y() - lC.y()), true);
        }
    }

}
