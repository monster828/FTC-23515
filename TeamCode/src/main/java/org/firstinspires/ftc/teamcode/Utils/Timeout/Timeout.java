package org.firstinspires.ftc.teamcode.Utils.Timeout;

public class Timeout {
    public TypeOfTimeout typeOfTimeout;
    private long _startTime = 0;
    public int millisecondsWaitTime;


    public Timeout(int waitTime, TypeOfTimeout typeOfTimeout) {
        this.typeOfTimeout = typeOfTimeout;
        millisecondsWaitTime = waitTime;
    }

    public Timeout(int waitTime) {
        this(waitTime, TypeOfTimeout.ContinueWhileWaiting);
    }


    public boolean IsComplete(){
        if (_startTime - System.nanoTime() >= millisecondsWaitTime) return true;
        return false;
    }

    public void Start(){
        _startTime = System.nanoTime();

        if (typeOfTimeout == TypeOfTimeout.WaitUntil){
            while (_startTime - System.nanoTime() < millisecondsWaitTime){

            }
        }
    }

    public void Reset(){
        _startTime = 0;
    }
}