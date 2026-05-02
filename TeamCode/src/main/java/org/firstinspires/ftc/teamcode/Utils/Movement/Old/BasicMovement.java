package org.firstinspires.ftc.teamcode.Utils.Movement.Old;

import androidx.annotation.Nullable;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

public class BasicMovement {

    private float p;
    private float[] a;
    public Position xyr;
    private float[] VxYx; //meant to be VxVy but i don't care to fix the typo
    private DcMotor[] motors;
    private PositionGetter posGet;
    private float tol;
    private Telemetry telemetry;
    private boolean I = false;
    private float rT = 0.8f;
    private float rotationMult = 1.0f;
    private long timeOut = 0;
    private float xTm = 1.0f;
    private int forceTimeOut = 99999;

    /**
     * Program a movement
     * @param motors the Motors (BL,BR,FL,FR)
     * @param xyr The X, Y and Rotation of where you wanna be
     * @param maxP maximum power for the motors
     * @param Tolerance how far away is acceptable?
     * @param posGet a PositionGetter to get position
     * @param I do you want to use some Integral?
     * @param tem Telemetry
     * **/
    public BasicMovement(DcMotor[] motors, Position xyr, float maxP, float Tolerance, PositionGetter posGet, boolean I, @Nullable Telemetry tem) {
        this.xyr = xyr;
        this.motors=motors;
        this.posGet=posGet;
        tol = Tolerance;
        p = maxP;
        telemetry=tem;
        this.I = I;
    }

    /**
     * Program a movement
     * @param motors the Motors (BL,BR,FL,FR)
     * @param xyr The X, Y and Rotation of where you wanna be
     * @param maxP maximum power for the motors
     * @param Tolerance how far away is acceptable?
     * @param posGet a PositionGetter to get position
     * @param I do you want to use some Integral?
     * @param tem Telemetry
     * @param rotationT Rotation tolerance.
     * **/
    public BasicMovement(DcMotor[] motors, Position xyr, float maxP, float Tolerance, PositionGetter posGet, boolean I, @Nullable Telemetry tem, float rotationT) {
        this.xyr = xyr;
        this.motors=motors;
        this.posGet=posGet;
        tol = Tolerance;
        p = maxP;
        telemetry=tem;
        this.I = I;
        this.rT = rotationT;
    }

    public void execute() {
        float[] pOut = {0,0,0};
        Position pos = posGet.getPosi();
        Position oPos = posGet.getPosi();
        long oTime = System.currentTimeMillis();
        long startTime = oTime;
        float xDist = xyr.x() - pos.x();
        float yDist = xyr.y() - pos.y();
        while ((Math.sqrt(Math.pow(xDist*(1/xTm),2)+Math.pow(yDist,2)) > tol || Math.abs(xyr.r() - pos.r()) > rT) && timeOut < 500) {

            float[] i = {0,0};

            xDist = xyr.x() - pos.x();
            yDist = xyr.y() - pos.y();

            pos = posGet.getPosi();

            pOut[0] = 0;
            pOut[1] = 0;

            if (Math.sqrt(Math.pow(xDist*(1/xTm),2)+Math.pow(yDist,2)) > tol) {
                pOut[0] = Math.min(Math.abs(xDist)/(p*15),1)+(i[0]*1000);
                pOut[1] = Math.min(Math.abs(yDist)/(p*15),1)+(i[1]*1000);
            }

            if(I && oTime != System.currentTimeMillis()) {
                i[0] += xDist*((float) (System.currentTimeMillis() - oTime) /1000);
                i[1] += yDist*((float) (System.currentTimeMillis() - oTime) /1000);
            }

            i[0] = MiscUtils.Clamp(i[0],-0.005f,0.005f);
            i[1] = MiscUtils.Clamp(i[1],-0.005f,0.005f);

            if (Math.abs(xyr.r() - pos.r()) > rT) {

                pOut[2] = MiscUtils.Clamp(((xyr.r() - pos.r()) / 15),-1.0f,1.0f);
            }

            float a = (float) Math.atan2(xDist, yDist);

            pOut[0] *= (float) Math.sin(a);
            pOut[1] *= (float) Math.cos(a);

            if(telemetry != null) {
                telemetry.addData("time", System.currentTimeMillis());
                telemetry.addData("X", (pos.x()));
                telemetry.addData("Y", (pos.y()));
                telemetry.addData("R", (pos.r()));
                telemetry.addData("xDiff", (xyr.x() - pos.x()));
                telemetry.addData("yDiff", (xyr.y() - pos.y()));
                telemetry.addData("rDiff", (xyr.r() - pos.r()));
                telemetry.addData("Dist", (xyr.getDistTo(pos)));
                telemetry.addData("angle to target:", Math.toDegrees(a));
                telemetry.addData("xPower", pOut[0]);
                telemetry.addData("yPower", pOut[1]);
                telemetry.addData("rPower", pOut[2]);
            }

            DriveUtils.FieldDriveThing(pOut[0], pOut[1], pOut[2]*rotationMult, p, (float) Math.toRadians(pos.r()), motors, telemetry);
            if(telemetry != null) telemetry.update();

            if(pos.getDistTo(oPos) < 0.5) {
                timeOut += System.currentTimeMillis()-oTime;
            } else {
                timeOut = 0;
                oPos = pos;
            }

            if(System.currentTimeMillis()-startTime > forceTimeOut) timeOut = 9999;

            oTime = System.currentTimeMillis();
        }
    }

    public void setRotationMult(float r) {
        rotationMult = r;
    }
    public void setXtol(float xTol) {
        xTm = xTol/tol;
    }
    public void setForcedTimeOut(int ms) {
        forceTimeOut = ms;
    }
}
