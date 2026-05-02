package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads.FTC2025to2026;

public class CannonThreadComm {

    float d;
    float y;
    boolean readyP = false;
    boolean readyG = false;

    public CannonThreadComm(float d, float y) {
        this.d = d;
        this.y = y;
    }

    public void setD(float d) {
        this.d = d;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void setReadyP(boolean ready) {
        this.readyP = ready;
    }
    public void setReadyG(boolean ready) {
        this.readyG = ready;
    }
    public float getD() {
        return d;
    }
    public float getY() {
        return y;
    }
    public boolean getReadyP() {
        return readyP;
    }
    public boolean getReadyG() {
        return readyG;
    }
}
