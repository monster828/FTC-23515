package org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters;

public enum RobotOdoSetup implements RobotOdoOrientation {
    PINPOINT2025(-1,1,-1,true),
    SPARKFUNidk(-1,1,-1,true),
    PINPOINTPROTO2026(1,-1,-1,true),
    PINPOINTSTARTER2026(-1,-1,-1,true),
    PINPOINT2026(-1,1,-1,true);

    private float xM, yM, rM, x, y, r;
    private boolean swapXY;
    RobotOdoSetup(float xM, float yM, float rM, boolean swapXY) {
        this.xM = xM; this.yM = yM; this.rM = rM; this.swapXY = swapXY;
    }
    @Override
    public float getX() {
        return (swapXY ? y : x)*xM;
    }

    @Override
    public float getY() {
        return (swapXY ? x : y)*yM;
    }

    @Override
    public float getR() {
        return r*rM;
    }

    @Override
    public void given(float x, float y, float r) {
        this.x = x; this.y = y; this.r = r;
    }
}
