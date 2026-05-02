package org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters;

import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

public abstract class PositionGetter {
    /**
     * +x is right
     * +y is forwards
     * +r is counterclockwise
     * **/
    public abstract float[] getPos();

    public abstract Position getPosi();
    public abstract float[] getVxVy();
    public abstract float getV();
}
