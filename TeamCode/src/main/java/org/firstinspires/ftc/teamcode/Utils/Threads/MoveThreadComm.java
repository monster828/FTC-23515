package org.firstinspires.ftc.teamcode.Utils.Threads;

public class MoveThreadComm {

    private boolean running = false;
    private boolean paused = false;
    private long ahead = 0;
    public MoveThreadComm() {
    }

    /**
     *checks if the movement is paused by a pause point.
     */
    public boolean isPaused() {
        return paused;
    }
    /**
     *checks if the movement is running.
     */
    public boolean isRunning() {
        return running;
    }
    /**
     * starts or restarts the movement
     */
    public void start() {
        running = true;
        paused = false;
    }
    /**
     * stops the movement
     */
    public void stop() {
        running = false;
    }
    public void setPaused(boolean p) {
        paused = p;
    }
    public void setAhead(long a) {
        ahead = a;
    }

    /**
     *Returns the difference between the expected time and the actual time. (negative means it's faster irl)
     */
    public long getAhead() {
        return ahead;
    }
}
