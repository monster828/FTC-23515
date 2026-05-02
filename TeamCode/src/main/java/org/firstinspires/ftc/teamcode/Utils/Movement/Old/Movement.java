package org.firstinspires.ftc.teamcode.Utils.Movement.Old;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

public class Movement {

    private float p;
    private float[] a;
    private Position xyr;
    private float[] VxYx; //meant to be VxVy but i don't care to fix the typo
    private DcMotor[] motors;
    private PositionGetter posGet;
    private float[] VperP;
    private float tol;
    private Telemetry telemetry;

    /**
     * Program a movement
     * @param motors the Motors (BL,BR,FL,FR)
     * @param xyr The X, Y and Rotation of where you wanna be
     * @param VxVy Maybe to be removed
     * @param accel max acceleration (array length of 2, 0 is X accel, 1 is Y accel)
     * @param maxP maximum power for the motors
     * @param Tolerance how far away is acceptable?
     * @param posGet a PositionGetter to get position
     * @param VperP Velocity per motor power (array length of 2, 0 is X, 1 is Y)
     * @param tem Telemetry
     * **/
    public Movement(DcMotor[] motors, Position xyr, float[] VxVy, float[] accel, float maxP, float Tolerance, PositionGetter posGet, float[] VperP, Telemetry tem) {
        this.xyr = xyr;
        this.motors=motors;
        this.posGet=posGet;
        tol = Tolerance;
        a = accel;
        p = maxP;
        this.VxYx = VxVy;
        this.VperP = VperP;
        telemetry=tem;
    }


    /**
     * Move the robot in the pre-programmed way
     * **/
    public void execute() {
        Position pos = posGet.getPosi();

        //v = sqrt(2ad)
        //v^2 = 2ad
        //v^2/2a = d

        float deccelX = (float) (Math.pow(VxYx[0]-p*VperP[0],2)/(2*a[0]));
        float accelX = (float) (Math.pow(p*VperP[0]-posGet.getVxVy()[0],2)/(2*a[0]));
        if(deccelX+accelX > Math.abs(pos.x()-xyr.x())) {
            deccelX -= (Math.abs(pos.x()-xyr.x())-deccelX+accelX)/2;
            accelX -= (Math.abs(pos.x()-xyr.x())-deccelX+accelX)/2;
        }

        float deccelY = (float) (Math.pow(VxYx[1]-posGet.getVxVy()[1],2)/(2*a[1]));
        float accelY = (float) (Math.pow(p*VperP[1]-posGet.getVxVy()[1],2)/(2*a[1]));
        if(deccelY+accelY > Math.abs(pos.y()-xyr.y())) {
            deccelY -= (Math.abs(pos.y()-xyr.y())-deccelY+accelY)/2;
            accelY -= (Math.abs(pos.y()-xyr.y())-deccelY+accelY)/2;
        }

        boolean xD = false;
        boolean yD = false;
        float[] pOut = {0,0,0};

        long millAtStartX = System.currentTimeMillis();
        long millAtStartY = System.currentTimeMillis();

        while(pos.getDistTo(xyr)>tol || Math.abs(xyr.r()-pos.r())>5) {

            float xDist = xyr.x()- pos.x();
            float yDist = xyr.y()- pos.y();

            pos = posGet.getPosi();

            if(pos.getDistTo(xyr)>tol) {

                if (!xD) {
                    if (Math.abs(xDist) < deccelX) {
                        xD = true;
                        millAtStartX = System.currentTimeMillis();
                    } else {
                        pOut[0] = MiscUtils.Clamp(a[0] * ((float) (System.currentTimeMillis() - millAtStartX) / 1000.0f) / VperP[0], 0.0f, p);
                    }
                }
                if (xD) {
                    //pOut[0] = MiscUtils.Clamp(p - (a[0] * ((float) (System.currentTimeMillis() - millAtStartX) / 1000.0f) / VperP[0]), VxYx[0] / VperP[0], p);
                }

                //same thing but for Y
                if (!yD) {
                    if (Math.abs(yDist) < deccelY) {
                        yD = true;
                        millAtStartY = System.currentTimeMillis();
                    } else {
                        pOut[1] = MiscUtils.Clamp(a[1] * ((float) (System.currentTimeMillis() - millAtStartY) / 1000.0f) / VperP[1], 0.0f, p);
                    }
                }
                if (yD) {
                    //pOut[1] = MiscUtils.Clamp(p - (a[1] * ((float) (System.currentTimeMillis() - millAtStartY) / 1000.0f) / VperP[1]), VxYx[1] / VperP[1], p);
                }

            }

            if(Math.abs(xyr.r()-pos.r())>5) {
                //simple rotation control
                //pOut[2] = MiscUtils.Clamp(((xyr.r() - pos.r()) / 30),0.0f,1.0f);
            }

            pOut[0] *= (float) Math.sin(Math.atan2(xDist,yDist));
            pOut[1] *= (float) Math.cos(Math.atan2(xDist,yDist));

            telemetry.addData("xDiff",(xyr.x()-pos.x()));
            telemetry.addData("yDiff",(xyr.y()-pos.y()));
            telemetry.addData("rDiff",(xyr.r()-pos.r()));
            telemetry.addData("xPower",pOut[0]);
            telemetry.addData("yPower",pOut[1]);
            telemetry.addData("rPower",pOut[2]);
            telemetry.addData("rot",pos.r());

            DriveUtils.FieldDriveThing(pOut[0],pOut[1],pOut[2],p, (float) Math.toRadians(pos.r()),motors,telemetry);
            telemetry.update();
        }
    }

}
