package org.firstinspires.ftc.teamcode.Utils.Threads;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.MovementUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.Utils.RobofileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class PosiBasedDriveThread extends Thread {

    ArrayList<Position> path = new ArrayList<>();
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
    private boolean last = false;

    @Override
    public void run() {
        super.run();
        while(opMode.opModeIsActive()) {
            if(!path.isEmpty()) {
                Position robot = posGet.getPosi();
                float rD;
                float angle;
                if(last) {
                    float xD = path.get(1).x()-robot.x();
                    float yD = path.get(1).y()-robot.y();
                    angle = (float) Math.atan2(xD,yD);
                    rD = MiscUtils.getAngleDifferenceDegrees(robot.r(),path.get(1).r());
                    opMode.telemetry.addData("X diff",xD);
                    opMode.telemetry.addData("Y diff",yD);
                } else {
                    angle = (float) Math.atan2(path.get(0).x()-robot.x(),path.get(0).y()-robot.y());
                    rD = MiscUtils.getAngleDifferenceDegrees(robot.r(),path.get(0).r());
                    opMode.telemetry.addData("X diff",path.get(0).x()-robot.x());
                    opMode.telemetry.addData("Y diff",path.get(0).y()-robot.y());
                }
                opMode.telemetry.addData("R diff",rD);
                float rP = 0;
                if(Math.abs(rD) > rotTolerance) {
                    rP = MiscUtils.Clamp((rD / 15),-1.0f,1.0f);
                }

                float transP = 0;
                if(path.get(0).getDistTo(robot) > tolerance) {
                    Position pos2 = path.get(1);
                    transP = Math.abs(
                            (path.get(0).getDistTo(pos2)/
                                    (path.get(0).getTimeStamp()-pos2.getTimeStamp()))/
                                    MovementUtils.v(MiscUtils.getAngleDifferenceDegrees(robot.r(),(float) Math.toDegrees(angle)))
                    );
                }

                transP = MiscUtils.Clamp(transP,0.2f,1);

                float m;
                if(last) {
                    m = path.get(1).getDistTo(robot);
                } else {
                    m = path.get(0).getDistTo(robot);
                }
                float p2 = MiscUtils.Clamp(transP*m,0.2f,transP);
                opMode.telemetry.addData("Power",p2);
                opMode.telemetry.addData("Angle",angle);
                opMode.telemetry.addData("rP",rP);
                opMode.telemetry.addData("distance",m);
                opMode.telemetry.addData("last",last);
                opMode.telemetry.addData("Points remaining",path.size());

                DriveUtils.FieldDriveThing((float)Math.sin(angle)*p2,(float)Math.cos(angle)*p2,rP,0.7f, (float) Math.toRadians(robot.r()),mot);

                //check if the robot has passed the target point
                int i = 1;

                while(i <= lookAhead) {
                    if(path.size() > i) {
                        if (path.get(i).getDistTo(robot) < m) {
                            break;
                        }
                    } else break;
                    i++;
                }

                opMode.telemetry.addData("i",i);

                if(i < lookAhead && i < path.size()) {
                    if(i == path.size()-1) {last = true; i--;}
                    for(int a = 0; a < i; a++) path.remove(0);
                } else if(m < tolerance && (MiscUtils.getAngleDifferenceDegrees(path.get(0).r(),robot.r()) < rotTolerance || path.size() > 1)) {
                    if(!last && path.size() == 2) last = true;
                    else path.remove(0);
                    if(last) while(!path.isEmpty())path.remove(0);
                }
                if(path.size() > 2) last = false;
            } else {
                last = false;
                DriveUtils.stop(mot);
                opMode.telemetry.addLine("IDLE");
            }
            opMode.telemetry.addLine("PBDT is running");
            opMode.telemetry.update();
        }
    }

    public synchronized void queuePath(Position[] path) {
        this.path.addAll(Arrays.asList(path));
        last = false;
    }
    public synchronized void forcePath(Position[] path) {
        this.path = new ArrayList<>();
        this.path.addAll(Arrays.asList(path));
        last = false;
    }
    public synchronized void queueRoboPath(File f) {
        try {
            FileInputStream fI = new FileInputStream(f);
            byte[] dat = new byte[Math.toIntExact(f.length())];
            fI.read(dat);
            fI.close();
            path.addAll(Arrays.asList(RobofileUtils.loadRobopathV1(dat)));
        } catch (Exception e) {

        }
        last = false;
    }
    public synchronized void forceRoboPath(File f) {
        path = new ArrayList<>();
        try {
            FileInputStream fI = new FileInputStream(f);
            byte[] dat = new byte[Math.toIntExact(f.length())];
            fI.read(dat);
            fI.close();
            path.addAll(Arrays.asList(RobofileUtils.loadRobopathV1(dat)));
        } catch (Exception e) {

        }
        last = false;
    }

    public synchronized boolean idle() {return path.isEmpty();}
}
