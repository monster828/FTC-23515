package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads.FTC2025to2026;

public class TurretAimThreadComm {
    private boolean active;
    private int offset = 0;

    /**
     * Class for communicating to the TurretAimThread
     * @param active If the TurretAimThread should be on by default
     */
    public TurretAimThreadComm(boolean active) {
        this.active = active;
    }

    /**
     * Check if the TurretAimThread is active
     * @return boolean idk
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Sets if the TurretAimThread should be active
     * @param a boolean
     */
    public void setActive(boolean a) {
        active = a;
    }

    /**
     * Offsets the TurretAimThread's default position
     * @param o Amount of ticks to offset
     */
    public void Offset(int o) {
        offset = o;
    }

    /**
     * Gets the desired start position change for the turret
     * @return Offset basically
     */
    public int getOffset() {
        return offset;
    }
}
