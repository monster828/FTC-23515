package org.firstinspires.ftc.teamcode.Utils.Movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class PositionUtils {
    

public static float[] WhereAmI(SparkFunOTOS sparkfun, Telemetry telemetry) {
    SparkFunOTOS.Pose2D pos = sparkfun.getPosition();
    float[] a = {(float)-pos.y,(float)pos.x,(float)-pos.h};
    telemetry.addData("PositionX",a[0]);
    telemetry.addData("PositionY",a[1]);
    telemetry.addData("PositionR",a[2]);
    return a;
}


public static float[] PositionViaRotate(DcMotor[] motors, Telemetry telemetry) {
    float circumference = 301.5928947446f; //mm
    float tilesize = 609.6f;
    float tilesperrot = circumference/tilesize;
    float ticksperrot = 537.6f;
    float[] mcp = { //current position of all motors in array
        motors[0].getCurrentPosition(), //back left
        motors[1].getCurrentPosition(), //back right
        motors[2].getCurrentPosition(), //front left
        motors[3].getCurrentPosition() //front right
    };
    float avgticks = (mcp[0] + mcp[1] + mcp[2] + mcp[3])/4;
    float avgl = (mcp[0] + mcp[2])/2;
    
    float y = avgticks/ticksperrot*tilesperrot;
    
    float x = (mcp[1] - mcp[3] - avgticks)/2;
    float r = ((avgl-avgticks)/1000)*180;
    
    x/=1031;
    
    telemetry.addData("Strafe",x);
    telemetry.addData("Forward",y);
    telemetry.addData("Rotate",r);
    
    float[] pos = {x,y,r};
    return pos;
    
}

public static float[] PositionViaRotate(DcMotor[] motors) {
    float circumference = 326.725f; //mm
    float tilesize = 609.6f;
    float tilesperrot = circumference/tilesize;
    float ticksperrot = 537.6f;
    float[] mcp = { //current position of all motors in array
        motors[0].getCurrentPosition(), //back left
        motors[1].getCurrentPosition(), //back right
        motors[2].getCurrentPosition(), //front left
        motors[3].getCurrentPosition() //front right
    };
    float avgticks = (mcp[0] + mcp[1] + mcp[2] + mcp[3])/4;
    float avgr = (mcp[1]+mcp[3])/2;
    
    float y = (avgticks/ticksperrot*tilesperrot)*-1;
    
    float x = (((mcp[1]-avgticks) - (mcp[3] - avgticks))/2)*1.111f;
    float r = ((avgr-avgticks)/1000)*-95.29f;
    
    x/=1031;
    
    float[] pos = {x,y,r};
    return pos;
    
}


}
