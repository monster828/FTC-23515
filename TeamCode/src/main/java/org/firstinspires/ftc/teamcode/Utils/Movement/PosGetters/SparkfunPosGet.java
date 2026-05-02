package org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

public class SparkfunPosGet extends PositionGetter{

    private float oX = 0.0f;
    private float oY = 0.0f;
    private long oMill = 0;
    private SparkFunOTOS sparkfun;
    private float rOffset = 0;

    public SparkFunOTOS getSparkfun() {
        return sparkfun;
    }

    /**
     * A position getter for Movements.
     * @param spark A sparkfun
     * **/
    public SparkfunPosGet(SparkFunOTOS spark) {
        sparkfun=spark;
        //oMill = System.currentTimeMillis();
    }

    /**
     * A position getter for Movements.
     * @param spark A sparkfun
     * @param rotOffset Starting angle offset in degrees (+ is clockwise)
     * **/
    public SparkfunPosGet(SparkFunOTOS spark,float rotOffset) {
        sparkfun=spark;
        rOffset = -rotOffset;
        //oMill = System.currentTimeMillis();
    }

    /**
     * Get the position of the robot as an Array (0 is X, 1 is Y,2 is Rotation)
     * ROTATION OFFSET IS NOT TAKEN INTO ACCOUNT
     * **/
    @Override
    public float[] getPos() {
        SparkFunOTOS.Pose2D pos = sparkfun.getPosition();
        return new float[] {-(float)pos.y,(float)pos.x,(float)-pos.h};
    }

    /**
     * Get the position of the robot as a Position
     * **/
    @Override
    public Position getPosi() {
        SparkFunOTOS.Pose2D pos = sparkfun.getPosition();
        return new Position(-(float)pos.y,(float)pos.x,(float)-pos.h).rotateCoords(rOffset);
    }

    /**
     * Get Velocity X and Y (0 is X, 1 is Y)
     * **/
    @Override
    public float[] getVxVy() {
        /*float[] pos = getPos();
        float[] v = new float[2];
        v[0] = (pos[0]-oX)/((System.currentTimeMillis()-oMill)/1000.0f);
        v[1] = (pos[1]-oY)/((System.currentTimeMillis()-oMill)/1000.0f);
        oX = pos[0]; oY = pos[1];
        oMill = System.currentTimeMillis();*/

        return new float[] {(float)-sparkfun.getVelocity().y,(float)sparkfun.getVelocity().x};
    }

    /**
     * Get overall velocity over the field
     * **/
    @Override
    public float getV() {
        return (float) Math.sqrt(Math.pow(sparkfun.getVelocity().x,2)+Math.pow(sparkfun.getVelocity().y,2));
    }
}
