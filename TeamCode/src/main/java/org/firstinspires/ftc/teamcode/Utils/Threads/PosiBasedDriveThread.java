package org.firstinspires.ftc.teamcode.Utils.Threads;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
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

    @Override
    public void run() {
        super.run();
        while(opMode.opModeIsActive()) {
            if(!path.isEmpty()) {
                Position robot = posGet.getPosi();
                float rD = MiscUtils.getAngleDifferenceDegrees(robot.r(),path.get(0).r());
                if(rD > rotTolerance) {

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
