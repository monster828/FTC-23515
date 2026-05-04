package org.firstinspires.ftc.teamcode.Utils.Threads;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

    float tolerance = 0.1f;
    int lookAhead = 5;
    float rT = 5;

    @Override
    public void run() {
        try {
            long delaystart = System.currentTimeMillis();
            boolean opModeCheck = true;
            long moveTime = 0;
            long actualTime = 0;
            long lastTime = System.currentTimeMillis();
            int posNum = 1;
            float tP = 0.5f;
            long pauseTimeRemaining = 0;
            while (System.currentTimeMillis() - delaystart < 30000 && opModeCheck) {
                Position p = posGet.getPosi();

                if(comm.isRunning() && !comm.isPaused()) {

                    //TEMPORARY TEST MOVE CODE BIT THING IDK SYSTEM DEVICE
                    float angle = (float) Math.atan2(positions[posNum].x()-p.x(),positions[posNum].y()-p.y());
                    float rP = 0.0f;
                    if (Math.abs(positions[posNum].r() - p.r()) > rT) {
                         rP = MiscUtils.Clamp(((positions[posNum].r() - p.r()) / 15),-1.0f,1.0f);
                    }
                    DriveUtils.FieldDriveThing((float)Math.cos(angle),(float)Math.sin(angle),rP,tP,p.r(),mot);

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
                        posNum += i;
                        tP = 0.5f;
                    }

                    //check if the robot is at the target point
                    if(positions[posNum].getDistTo(p) < tolerance) {
                        posNum += 1;
                        tP = 0.5f;
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
                    posNum += 1;
                }
                lastTime = System.currentTimeMillis();
                comm.setAhead(moveTime-actualTime);

                //check if the main OpMode is still running.
                if(opMode != null)opModeCheck = opMode.opModeIsActive() || opMode.opModeInInit();
            }
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
}
