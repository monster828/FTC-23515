package org.firstinspires.ftc.teamcode.Utils.Movement;

public class MovementUtils {

    /**
     * Acceleration calc
     * @param angle direction to accel in (deg)
     * @return the max acceleration in (unit)/s^2
     */
    public static float a(float angle) {
        angle = Math.abs((Math.abs((angle+90.0f) % 180.0f))-90.0f);
        //return (float) sqrt(Math.pow((sin(angle)*StrafeA),2)+Math.pow((cos(angle)*ForwardBackA),2));
        //return Math.abs(((angle%180)/90)-1)*ForwardBackA+Math.abs((((angle+90)%180)/90)-1)*StrafeA;
        return (0.0001f*angle*angle*angle)+(0.012f*angle*angle)-(2.3751f*angle)+160.75f;
    }
    public static float topA=159;

    /**
     * Velocity calc
     * @param angle velocity direction (deg)
     * @return the max velocity in (unit)/s
     */
    public static float v(float angle) {
        float angle2 = Math.abs((Math.abs((angle+90.0f) % 180.0f))-90.0f);
        //return (float) sqrt(Math.pow((sin(angle)*StrafeV),2)+Math.pow((cos(angle)*ForwardBackV),2));
        //return Math.abs(((angle%180)/90)-1)*ForwardBackV+Math.abs((((angle+90)%180)/90)-1)*StrafeV;
        return (0.00005f*angle2*angle2*angle2)+(0.0014f*angle2*angle2)-(0.6224f*angle2)+46.083f;
    }
    public static final float topV=45.8f;

    public static float distanceCovered(float angle, float time) {
        float v = v(angle); float a = a(angle);
        if(time < v/a) {
            return a*time*time*0.5f;
        } else {
            return v*time-((v*v)/(2*a));
        }
    }

    public static float timeToTravel(float angle, float distance) {
        float v = v(angle); float a = a(angle);
        if(distance < (v*v)/(2*a)) {
            return (float) Math.sqrt(2*distance/a);
        } else {
            return (distance/v)+(v/(2*a));
        }
    }
}
