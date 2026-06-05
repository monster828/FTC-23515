package org.firstinspires.ftc.teamcode.Utils.Movement.Testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Utils.Logger;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PinpointPosGet;
import org.firstinspires.ftc.teamcode.Utils.Threads.MoveThread;
import org.firstinspires.ftc.teamcode.Utils.Threads.MoveThreadComm;

import java.io.File;

public class Tester extends LinearOpMode {
    @Override
    public void runOpMode() {
        DcMotor[] mot = {
                hardwareMap.get(DcMotorEx.class,"BL"), //back left
                hardwareMap.get(DcMotorEx.class,"BR"), //back right
                hardwareMap.get(DcMotorEx.class,"FL"), //front left
                hardwareMap.get(DcMotorEx.class,"FR") //front right
        };

        mot[0].setDirection(DcMotorSimple.Direction.REVERSE);
        mot[2].setDirection(DcMotorSimple.Direction.REVERSE);
        GoBildaPinpointDriver pin = hardwareMap.get(GoBildaPinpointDriver.class,"POC");

        waitForStart();
        telemetry.addLine("Configuring Test...");
        telemetry.update();
        File loggerFile = new File(MiscUtils.dataFolder+"/log.robolog");
        BestToleranceFinder bestToleranceFinder = new BestToleranceFinder(10);

        sleep(100);

        while (!bestToleranceFinder.IsTestComplete()){
            bestToleranceFinder.NewTest();

            if (bestToleranceFinder.AmountOfTest() < 0){
                telemetry.addLine("Press A to run movement test");
            }else{
                telemetry.addData("Current Test",bestToleranceFinder.AmountOfTest() + 1);
                telemetry.addData("Current Tolerance", bestToleranceFinder.GetCurrentTolerance());
                telemetry.addLine("Press A to run " + bestToleranceFinder.AmountOfTest() + 1 + "th test");
            }
            telemetry.update();

            while (!this.gamepad1.a){
                // Wait for start
            }

            telemetry.addLine("Configuring Test...");
            telemetry.update();

            PinpointPosGet posGet = new PinpointPosGet(pin);
            MoveThreadComm move = new MoveThreadComm();
            MoveThread m = new MoveThread(this,move,mot,
                    new File(MiscUtils.dataFolder+"/testpath2.robopath"),
                    telemetry,
                    posGet
            );
            Logger l = new Logger(loggerFile);
            m.addLog(l);
            l.add("Opmode Started",new byte[0]);

            m.setTolerance(bestToleranceFinder.GetCurrentTolerance());

            sleep(1000);

            telemetry.addLine("Testing...");
            telemetry.update();

            m.start();
            move.start();
            while(move.isRunning());

            float driveTime = move.getDriveTime();
            float accuracy = 1;
            telemetry.addData("Time", driveTime);
            telemetry.addData("Accuracy", accuracy);
            telemetry.addLine("Press B to continue...");
            telemetry.update();

            bestToleranceFinder.CompletedTestResults(driveTime, accuracy);

            while(!this.gamepad1.b){
                // Wait to continue
            }
        }

        telemetry.addLine("Test has been complete...");
        telemetry.addData("Final Tolerance", bestToleranceFinder.GetCurrentTolerance());
        telemetry.addLine("Press A to finish...");
        telemetry.update();

        while (!this.gamepad1.a){        }
    }
}