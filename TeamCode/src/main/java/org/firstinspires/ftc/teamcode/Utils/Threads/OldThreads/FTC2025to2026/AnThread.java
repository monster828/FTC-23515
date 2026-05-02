package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads.FTC2025to2026;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AnThread extends Thread {

    LinearOpMode opmode;
    Telemetry tem;

    public AnThread(LinearOpMode starter, Telemetry tem) {
        opmode = starter;
        this.tem = tem;
    }

    @Override
    public void run() {
        while(opmode.opModeIsActive()) {
            tem.addData("","OH NO THE THREAD IS RUNNING! \n HELP! HELP! \n STOP IT PLEEEEEEEASE!");
            tem.update();
        }
    }
}
