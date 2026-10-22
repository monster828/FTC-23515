package org.firstinspires.ftc.teamcode.Utils.Shooting;

public class GetShooting {
    public float simX;
    public float simY;

    public void SetSimulatedPosition(float positionX, float positionY, float velocityX, float velocityY, float timeOut){
        simX = positionX - (velocityX * timeOut);
        simY = positionY - (velocityY * timeOut);
    }

    /*
    This is kind of the code for turret aiming (Also with Sudo Code)
    - turret point towards simX and Y
    - hood adjusted for simX and Y


    set velocity to distance (with Z?) (relative to the power curve)
    limelight distance to april tag?
     */

    public float GetHoodPosition(){
        return 0;
    }

    public float GetPower(){
        return 0;
    }
}
