package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads.FTC2025to2026;

public class SpindexerComm {
    boolean shoot = false;
    int demand = 0;
    boolean busy = false;
    boolean loading = false;
    int ballcount = 0;

    public SpindexerComm() {}
    public void shoot() {
        shoot = true;
    }
    public void shot() {
        shoot = false;
    }

    /** Sets what color is needed
     * @param demand 0 = none, 1 = purple, 2 = green
     * **/
    public void setDemand(int demand) {
        this.demand = demand;
    }
    public int getDemand() {
        return demand;
    }
    public boolean shotQueued() {
        return shoot;
    }
    public boolean getBusy() {
        return busy;
    }
    public void setBusy(boolean b) {
        busy = b;
    }
    public void setLoading(boolean l) {
        loading = l;
    }
    public boolean getLoading() {
        return loading;
    }
    public void setBallcount(int balls) {ballcount = balls;}
    public int getBallcount() {return ballcount;}
}
