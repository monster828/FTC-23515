package org.firstinspires.ftc.teamcode.Utils.Threads;

public class MoveThreadComm {

    private boolean running = false;
    private boolean paused = false;
    public MoveThreadComm() {
    }

    public boolean isPaused() {
        return paused;
    }
    public boolean isRunning() {
        return running;
    }
    public void start() {
        running = true;
        paused = false;
    }
    public void stop() {
        running = false;
    }
    public void setPaused(boolean p) {
        paused = p;
    }
}
