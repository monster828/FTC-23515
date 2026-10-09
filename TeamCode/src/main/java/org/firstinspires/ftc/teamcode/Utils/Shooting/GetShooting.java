package org.firstinspires.ftc.teamcode.Utils.Shooting;

public class GetShooting {
    public float simX;
    public float simY;

    public void SetSimulatedPosition(float positionX, float positionY, float velocityX, float velocityY, float timeOut){
        simX = positionX - (velocityX * timeOut);
        simY = positionY - (velocityY * timeOut);
    }


    public float GetPower(){
        return 0;
    }
}
