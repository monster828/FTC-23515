package org.firstinspires.ftc.teamcode.Utils.Threads;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.MovementUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

import java.util.ArrayList;
import java.util.Arrays;

public class PosiBasedDriveThread extends Thread {

    ArrayList<Position> path;
    DcMotor[] mot;
    PositionGetter posGet;
    LinearOpMode opMode;

    public PosiBasedDriveThread(LinearOpMode2026 opMode) {
        this.mot = opMode.mot;
        this.posGet = opMode.posGet;
        this.opMode = opMode;
    }

    public static float tolerance = 2;
    public static float rotTolerance = 10;
    public static int lookAhead = 3;

    @Override
    public void run() {
        super.run();
        while(opMode.opModeIsActive()) {
            if(!path.isEmpty()) {
                Position robot = posGet.getPosi();
                float rD = MiscUtils.getAngleDifferenceDegrees(robot.r(),path.get(0).r());
                float angle = (float) Math.atan2(path.get(0).x()-robot.x(),path.get(0).y()-robot.y());
                float rP = 0;
                if(rD > rotTolerance) {
                    rP = MiscUtils.Clamp((MiscUtils.getAngleDifferenceDegrees(robot.r(),path.get(0).r()) / 15),-1.0f,1.0f);
                }

                float transP = 0;
                if(path.get(0).getDistTo(robot) > tolerance) {
                    Position pos2 = path.get(0);
                    transP = (
                            (path.get(0).getDistTo(pos2)/
                                    (path.get(0).getTimeStamp()-pos2.getTimeStamp()))/
                                    MovementUtils.v(MiscUtils.getAngleDifferenceDegrees(robot.r(),(float) Math.toDegrees(angle)))
                    );
                }

                float m = path.get(0).getDistTo(robot);
                float p2 = MiscUtils.Clamp(transP*m,0.2f,transP);
                DriveUtils.FieldDriveThing((float)Math.sin(angle)*p2,(float)Math.cos(angle)*p2,rP,1, (float) Math.toRadians(robot.r()),mot);

                //check if the robot has passed the target point
                int i = 1;

                while(i <= lookAhead) {
                    if(path.size() > i) {
                        if (path.get(i).getDistTo(robot) < m) {
                            break;
                        }
                    }
                    i++;
                }

                if(i < lookAhead && i < path.size()) {
                    for(int a = 0; a < i; a++) path.remove(0);
                } else if(m < tolerance && (MiscUtils.getAngleDifferenceDegrees(path.get(0).r(),robot.r()) < rotTolerance || path.size() > 1)) {
                    path.remove(0);
                }
            }
        }
    }

    public synchronized void queuePath(Position[] path) {
        this.path.addAll(Arrays.asList(path));
    }
    public synchronized void forcePath(Position[] path) {
        this.path = new ArrayList<>();
        this.path.addAll(Arrays.asList(path));
    }
}
