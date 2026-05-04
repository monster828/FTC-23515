package org.firstinspires.ftc.teamcode.Utils.Threads;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

    /**
     * Create a new MoveThread, this thread moves the robot according to a RoboPath file.
     * @param opmode OpMode that is running the thread.
     * @param comm communications with the main OpMode.
     * @param robopath The robopath file to read.
     * @param telemetry Telemetry to complain to.
     * @param posGet Position getter to find the position of the robot.
     */
    public MoveThread(LinearOpMode opmode,MoveThreadComm comm,File robopath,Telemetry telemetry, PositionGetter posGet) {
        tem = telemetry;
        opMode = opmode;
        this.posGet = posGet;
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

    @Override
    public void run() {
        try {
            long delaystart = System.currentTimeMillis();
            boolean opModeCheck = true;
            long moveTime = 0;
            long actualTime = 0;
            long lastTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - delaystart < 30000 && opModeCheck) {

                //CODE GOES HERE.

                if(comm.isRunning()) {
                    moveTime += System.currentTimeMillis()-lastTime;
                    actualTime += System.currentTimeMillis()-lastTime;
                }
                lastTime = System.currentTimeMillis();

                if(opMode != null)opModeCheck = opMode.opModeIsActive() || opMode.opModeInInit();
            }
        } catch (Exception e) {

        }
    }
}
