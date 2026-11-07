package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.RobotOdoSetup;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.Utils.RobofileUtils;
import org.firstinspires.ftc.teamcode.Utils.Threads.PosiBasedDriveThread;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

@Autonomous
public class JustAPreprogrammedPath extends LinearOpMode2026 {
    @Override
    public void runOpMode() throws InterruptedException {
        config();
        mot[1].setDirection(DcMotorSimple.Direction.FORWARD); mot[3].setDirection(DcMotorSimple.Direction.FORWARD);
        posGet.setSetup(RobotOdoSetup.PINPOINTSTARTER2026);
        PosiBasedDriveThread dt = new PosiBasedDriveThread(this);
        File rp = new File(MiscUtils.dataFolder+"/preprogpath.robopath");
        byte[] dat = new byte[Math.toIntExact(rp.length())];
        try {
            FileInputStream fI = new FileInputStream(rp);
            fI.read(dat);
            fI.close();
        } catch (Exception e) {
            telemetry.addLine("Aw, crud");
            telemetry.update();
            sleep(5000);
        }
        Position[] first = RobofileUtils.loadRobopathV1(dat);
        posGet.setOffset(first[0].x(),first[0].y());
        File rp2 = new File(MiscUtils.dataFolder+"/preprogpath2.robopath");
        waitForStart();
        dt.start();
        dt.forcePath(first);
        sleep(100);
        while(!dt.idle());
        sleep(2000);
        dt.queueRoboPath(rp2);
        while(opModeIsActive());
    }
}
