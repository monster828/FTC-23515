package org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters;

import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

public abstract class PositionGetter {

    public RobotOdoSetup setup;
    public float xOffset = 0;
    public float yOffset = 0;
    /**
     * +x is right
     * +y is forwards
     * +r is counterclockwise
     * **/
    public abstract float[] getPos();

    public abstract Position getPosi();
    public abstract float[] getVxVy();
    public abstract float getV();

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
