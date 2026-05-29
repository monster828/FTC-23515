package org.firstinspires.ftc.teamcode.Utils.Threads;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.teamcode.Utils.Logger;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.Utils.RobofileUtils;

import java.io.File;
import java.io.FileInputStream;

public class MoveThread extends Thread {

    LinearOpMode opMode;
    Position[] positions;
    Telemetry tem;
    PositionGetter posGet;
    MoveThreadComm comm;
    DcMotor[] mot;
    Logger log;

    /**
     * Create a new MoveThread, this thread moves the robot according to a RoboPath file.
     * @param opmode OpMode that is running the thread.
     * @param comm communications with the main OpMode.
     * @param mot wheel motors (BL,BR,FL,FR)
     * @param robopath The robopath file to read.
     * @param telemetry Telemetry to complain to.
     * @param posGet Position getter to find the position of the robot.
     */
    public MoveThread(LinearOpMode opmode,MoveThreadComm comm,DcMotor[] mot,File robopath,Telemetry telemetry, PositionGetter posGet) {
        tem = telemetry;
        opMode = opmode;
        this.posGet = posGet;
        this.mot = mot;
        this.comm = comm;
        try {
            FileInputStream fI = new FileInputStream(robopath);
            byte[] dat = new byte[Math.toIntExact(robopath.length())];
            fI.read(dat);
            fI.close();
            positions = RobofileUtils.loadRobopathV1(dat);
        } catch (Exception e) {

        }
    }

    float tolerance = 2f;
    int lookAhead = 40;
    float rT = 5;
    float antiJERK = 1.0f;

    @Override
    public void run() {
        try {
            long delaystart = System.currentTimeMillis();
            boolean opModeCheck = true;
            long moveTime = 0;
            long actualTime = 0;
            long lastTime = System.currentTimeMillis();
            int posNum = 1;
            float tP = 1.0f;
            long pauseTimeRemaining = 0;
            long logTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - delaystart < 30000 && opModeCheck && posNum < positions.length-1) {
                Position p = posGet.getPosi();
                float angle = 0;

                if(comm.isRunning() && !comm.isPaused()) {

                    //TEMPORARY TEST MOVE CODE BIT THING IDK SYSTEM DEVICE
                    angle = (float) Math.atan2(positions[posNum].x()-p.x(),positions[posNum].y()-p.y());
                    float rP = 0.0f;
                    if (Math.abs(positions[posNum].r() - p.r()) > rT) {
                         rP = MiscUtils.Clamp(((positions[posNum].r() - p.r()) / 15),-1.0f,1.0f);
                    }
                    float transP = 0;
                    if(positions[posNum].getDistTo(p) > tolerance) {
                        transP = tP;
                    }
                    float p2 = MiscUtils.Clamp((transP/antiJERK)*positions[posNum].getDistTo(p),0.2f,1.0f);
                    DriveUtils.FieldDriveThing((float)Math.sin(angle),(float)Math.cos(angle),rP,p2, (float) Math.toRadians(p.r()),mot);

                    //check if the robot has passed the target point
                    int i = 1;
                    float m = positions[posNum].getDistTo(p);
                    boolean skip = false;
                    while(i <= lookAhead) {
                        if(positions.length > posNum+i) {
                            if(positions[posNum + i].getType() > 1) {
                                skip = true;
                                break;
                            }
                            if (positions[posNum + i].getDistTo(p) < m) {
                                break;
                            }
                        }
                        i++;
                    }
                    if(i < lookAhead && !skip) {
                        moveTime = positions[posNum+1].getTimeStamp();
                        if(posNum+i < positions.length) posNum += i;
                        tP = 1.0f;
                    }

                    //check if the robot is at the target point
                    if(positions[posNum].getDistTo(p) < tolerance && (MiscUtils.getAngleDifferenceDegrees(positions[posNum].r(),p.r()) < rT || positions[posNum].getType() == 0)) {
                        if(posNum < positions.length-1) posNum += 1;
                        tP = 1.0f;
                    }

                    if(positions[posNum].getType() == 3) {
                        comm.setPaused(true);
                        pauseTimeRemaining = (long)positions[posNum].getExtraData()[0];
                    }

                    if(positions[posNum].getType() == 2) {
                        comm.stop();
                    }
                }
                if(comm.isRunning()) {
                    //progress time
                    moveTime += System.currentTimeMillis()-lastTime;
                    actualTime += System.currentTimeMillis()-lastTime;
                    if(pauseTimeRemaining > 0) {
                        pauseTimeRemaining -= (System.currentTimeMillis()-lastTime);
                    }
                }
                if(pauseTimeRemaining <= 0 && comm.isPaused()) {
                    comm.setPaused(false);
                    if(posNum < positions.length-1) posNum += 1;
                }
                lastTime = System.currentTimeMillis();
                comm.setAhead(moveTime-actualTime);
                comm.setDriveTime(actualTime);

                if(tem != null) {
                    tem.addData("Target Point", positions[posNum].toString());
                    tem.addData("Current Pos", posGet.getPosi().toString());
                    tem.addData("Angle to target", Math.toDegrees(angle));
                    tem.addData("Predicted time difference",moveTime-actualTime);
                }
                if(System.currentTimeMillis()-logTime > 50 && log != null) {
                    log.add("Target Point", new byte[] {(byte) positions[posNum].x(),(byte) positions[posNum].y(),(byte) positions[posNum].r()});
                    log.add("Current Point", new byte[] {(byte) posGet.getPosi().x(),(byte) posGet.getPosi().y(),(byte) posGet.getPosi().r()});
                    log.add("Angle to target",new byte[] {(byte) Math.toDegrees(angle)});
                    logTime = System.currentTimeMillis();
                }
                tem.update();

                //check if the main OpMode is still running.
                if(opMode != null)opModeCheck = opMode.opModeIsActive() || opMode.opModeInInit();
            }
            DriveUtils.stop(mot);
        } catch (Exception e) {

        }
    }

    /**
     * How accurate the robot has to be.
     * @param tol tolerance.
     */
    public void setTolerance(float tol) {
        tolerance = tol;
    }

    /**
     * Sets how many points the robot can skip ahead at any given time.
     * If the robot is back-tracking increase this, if the robot is cutting corners, decrease this.
     * @param i how many points to skip.
     */
    public void setLookAhead(int i) {
        lookAhead = i;
    }

    /**
     * Adds a log
     * @param log the log to add
     */
    public void addLog(Logger log) {
        this.log = log;
    }

    /**
     * Sets the anti jerk variable
     * @param aJ how little of a jerk the robot should be
     */
    public void setAntiJERK(float aJ) {
        antiJERK = aJ;
    }
}
