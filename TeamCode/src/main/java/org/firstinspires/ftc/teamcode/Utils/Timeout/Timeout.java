package org.firstinspires.ftc.teamcode.Utils.Timeout;

public class Timeout {
    public TypeOfTimeout typeOfTimeout;
    private long _startTime = 0;
    public int millisecondsWaitTime;

    private FrameRateCounter frameRateCounter;


    public Timeout(int waitTime, TypeOfTimeout typeOfTimeout, FrameRateCounter frameRateCounter) {
        this.typeOfTimeout = typeOfTimeout;
        millisecondsWaitTime = waitTime;
        this.frameRateCounter = frameRateCounter;
    }

    public Timeout(int waitTime) {
        this(waitTime, TypeOfTimeout.ContinueWhileWaiting, null);
    }

    public Timeout(int waitTime, TypeOfTimeout typeOfTimeout) {
        this(waitTime, typeOfTimeout, null);
    }

    // This returns if it has completed it's wait time
    public boolean IsComplete() {
        if (_startTime == 0) return false;
        long elapsedMillis = (System.nanoTime() - _startTime) / 1_000_000;
        return elapsedMillis >= millisecondsWaitTime;
    }


    public void Start(){
        _startTime = System.nanoTime();

        if (typeOfTimeout == TypeOfTimeout.WaitUntil){
            while (!IsComplete()){
                if (frameRateCounter != null){
                    frameRateCounter.Frame();
                }

                Thread.yield();
            }
        }
    }

    public void Reset(){
        _startTime = 0;
    }
}