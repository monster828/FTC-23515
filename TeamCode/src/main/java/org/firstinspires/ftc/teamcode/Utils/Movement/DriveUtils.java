

package org.firstinspires.ftc.teamcode.Utils.Movement;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class DriveUtils {


    static float Fbias = 1.0f;
    static float Sbias = 1.0f;
    static float Rbias = 1.0f;

    public static void setBiases(float f, float s, float r) {
        Fbias = f;
        Sbias = s;
        Rbias = r;
    }

    /**
     * Sets motor powers for moving
     * @param f How much to move forwards
     * @param s How much to strafe
     * @param r How much to rotate
     * @param p How much power to use
     * @param mot Motor array
     */
    public static void DriveThing(float f, float s, float r, float p, DcMotor[] mot) {
        /*float Fbias = 1.0f;
        float Sbias = 1.0f;
        float Rbias = 1.0f;*/
        float div = 0.0f; //used for averaging total power
        div += Math.abs(f)*Fbias;
        div += Math.abs(s)*Sbias;
        div += Math.abs(r)*Rbias;
        mot[0].setPower(((f*Fbias - s*Sbias + r*Rbias)/div)*p); //bl
        mot[1].setPower(((f*Fbias + s*Sbias - r*Rbias)/div)*p); //br
        mot[2].setPower(((f*Fbias + s*Sbias + r*Rbias)/div)*p); //fl
        mot[3].setPower(((f*Fbias - s*Sbias - r*Rbias)/div)*p); //fr
        
    }

    /**
     * Field-centric DriveThing
     * @param x Power to move in the X direction
     * @param y Power to move in the Y direction
     * @param r Power for rotating
     * @param p SPEEEEEEEED
     * @param currentr Current rotation of the robot
     * @param mot Motor array
     */
    public static void FieldDriveThing(float x,float y,float r, float p,float currentr, DcMotor[] mot) {
        DriveThing((float) (Math.cos(currentr)*y+Math.sin(currentr)*x), (float) (Math.cos(currentr)*x-Math.sin(currentr)*y),r,p,mot);
    }

    /**
     * Field-centric DriveThing
     * @param x Power to move in the X direction
     * @param y Power to move in the Y direction
     * @param r Power for rotating
     * @param p SPEEEEEEEED
     * @param currentr Current rotation of the robot
     * @param mot Motor array
     * @param tem Telemetry for FSR powers
     */
    public static void FieldDriveThing(float x, float y, float r, float p, float currentr, DcMotor[] mot, Telemetry tem) {
        if(tem != null) {
            tem.addData("f", Math.cos(currentr) * y + Math.sin(currentr) * x);
            tem.addData("s", Math.cos(currentr) * x - Math.sin(currentr) * y);
            tem.addData("r", r);
        }
        DriveThing((float) (Math.cos(currentr)*y+Math.sin(currentr)*x), (float) (Math.cos(currentr)*x-Math.sin(currentr)*y),r,p,mot);
    }

    /**
     * Stops all the motors in the array
     * @param mot motors to stop
     */
    public static void stop(DcMotor[] mot) {
        DriveThing(0,0,0,0,mot);
    }

    /**
     * Stops all the motors in the array directly
     * @param mot motors to stop
     */
    public static void stopD(DcMotor[] mot) {
        for(DcMotor m : mot) m.setPower(0);
    }

}

