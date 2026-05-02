package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads;

public class ThreadStopper {

    boolean running = true;
    public ThreadStopper() {

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
