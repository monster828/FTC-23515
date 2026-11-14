package org.firstinspires.ftc.teamcode.Utils.Positioning;

import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;

public class DistanceToHive {

    PositionGetter positionGetter;

    float upHiveX, upHiveY;
    float downHiveX, downHiveY;

    public DistanceToHive(PositionGetter positionGetter){
        this.positionGetter = positionGetter;
        upHiveX = 57.5f;
        upHiveY = 59.5f;
        downHiveX = 57.5f;
        downHiveY = 83.5f;
    }

    public double GetDistance(){
        double yPosition = positionGetter.y();

        if (yPosition > 64){
            // Down hive

            double xdist = positionGetter.x() - downHiveX;
            double ydist = positionGetter.y() - downHiveY;

            return Math.sqrt(xdist * xdist + ydist * ydist);
        } else {
            // Up hive

            double xdist = positionGetter.x() - upHiveX;
            double ydist = positionGetter.y() - upHiveY;

            return Math.sqrt(xdist * xdist + ydist * ydist);
        }
    }

}
