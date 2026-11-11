package org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters;

import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

public abstract class PositionGetter {

    public RobotOdoSetup setup;
    public float xOffset = 0;
    public float yOffset = 0;
    private long lastVupdate = System.currentTimeMillis();
    private float lastV = 0;
    /**
     * +x is right
     * +y is forwards
     * +r is counterclockwise
     * **/
    public abstract float[] getPos();

    public abstract Position getPosi();
    public abstract float[] getVxVy();

    public abstract double getVr();

    public float getV() {
        float[] vxvy = getVxVy();
        float v = (float) Math.sqrt((vxvy[0]*vxvy[0])+(vxvy[1]*vxvy[1]));
        lastV = v; lastVupdate = System.currentTimeMillis();
        return v;
    }

    public float getA() {
        float v = getV();
        float a = (v-lastV)/((System.currentTimeMillis()-lastVupdate)/1000.0f);
        lastV = v;
        lastVupdate = System.currentTimeMillis();
        return a;
    }

    public void setOffset(float x, float y) {
        xOffset = x;
        yOffset = y;
    }
    public float x() {
        return getPosi().x();
    }
    public float y() {
        return getPosi().y();
    }
    public float r() {
        return getPosi().r();
    }

    public void setSetup(RobotOdoSetup setup) {this.setup = setup;}
    public RobotOdoSetup getSetup() {return setup;}

}
