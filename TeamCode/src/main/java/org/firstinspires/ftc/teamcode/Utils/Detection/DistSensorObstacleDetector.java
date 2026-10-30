package org.firstinspires.ftc.teamcode.Utils.Detection;

import android.graphics.Point;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.ArrayDrawer;
import org.firstinspires.ftc.teamcode.Utils.MatrixDrawer;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.Pathfinder;
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
            Position lcC = new Position(lX, mY, 0);
            Position rcC = new Position(rX, mY, 0);
            Position lfC = new Position((float) lX, (float) (lD+18), 0);
            Position rfC = new Position((float) rX, (float) (lD+18), 0);
            lcC.rotateCoords(p.r()+turretAngle); rcC.rotateCoords(p.r()+turretAngle); lfC.rotateCoords(p.r()+turretAngle); rfC.rotateCoords(p.r()+turretAngle);
            MatrixDrawer.fillNGon(o,new Point[]{
                    new Point((int) MiscUtils.Clamp(lcC.x(),0,141), (int) MiscUtils.Clamp(lcC.y(),0,141)),
                    new Point((int) MiscUtils.Clamp(rcC.x(),0,141), (int) MiscUtils.Clamp(rcC.y(),0,141)),
                    new Point((int) MiscUtils.Clamp(rfC.x(),0,141), (int) MiscUtils.Clamp(rfC.y(),0,141)),
                    new Point((int) MiscUtils.Clamp(lfC.x(),0,141), (int) MiscUtils.Clamp(lfC.y(),0,141))
            },true);
        }
        if(rD > 0.1f){
            int lX = (int) (Math.sin(Math.toRadians(rightAngle - (FOV / 2))) * rD);
            int rX = (int) (Math.sin(Math.toRadians(rightAngle + (FOV / 2))) * rD);
            int mY = (int) Math.min(Math.cos(Math.toRadians(rightAngle - (FOV / 2))) * rD, Math.cos(Math.toRadians(rightAngle + (FOV / 2))) * rD);
            Position lcC = new Position(lX, mY, 0);
            Position rcC = new Position(rX, mY, 0);
            Position lfC = new Position((float) lX, (float) (rD+18), 0);
            Position rfC = new Position((float) rX, (float) (rD+18), 0);
            lcC.rotateCoords(p.r()+turretAngle); rcC.rotateCoords(p.r()+turretAngle); lfC.rotateCoords(p.r()+turretAngle); rfC.rotateCoords(p.r()+turretAngle);
            MatrixDrawer.fillNGon(o,new Point[]{
                    new Point((int) MiscUtils.Clamp(lcC.x(),0,141), (int) MiscUtils.Clamp(lcC.y(),0,141)),
                    new Point((int) MiscUtils.Clamp(rcC.x(),0,141), (int) MiscUtils.Clamp(rcC.y(),0,141)),
                    new Point((int) MiscUtils.Clamp(rfC.x(),0,141), (int) MiscUtils.Clamp(rfC.y(),0,141)),
                    new Point((int) MiscUtils.Clamp(lfC.x(),0,141), (int) MiscUtils.Clamp(lfC.y(),0,141))
            },true);
        }
    }

}
