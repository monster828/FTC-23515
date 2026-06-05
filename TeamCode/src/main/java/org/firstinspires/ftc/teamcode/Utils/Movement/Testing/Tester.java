package org.firstinspires.ftc.teamcode.Utils.Movement.Testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utils.Logger;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PinpointPosGet;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.Utils.Threads.MoveThread;
import org.firstinspires.ftc.teamcode.Utils.Threads.MoveThreadComm;

import java.io.File;

@TeleOp(name = "FindBestTolerance")
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

            while (!this.gamepad1.a && opModeIsActive()){
                // Wait for start
            }

            telemetry.addLine("Configuring Test...");
            telemetry.update();

            PinpointPosGet posGet = new PinpointPosGet(pin);
            MoveThreadComm move = new MoveThreadComm();
            MoveThread m = new MoveThread(this,move,mot,
                    new File(MiscUtils.dataFolder+"/testpath4.robopath"),
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
            while(move.isRunning() && opModeIsActive());

            float driveTime = move.getDriveTime();
            Position[] positions = m.GetPositions();
            float accuracy = AccuracyCalculator(positions[positions.length - 1], posGet);
            float rotationAccuracy = RotationAccuracyCalculator(positions[positions.length - 1], posGet);
            telemetry.addData("Time", driveTime);
            telemetry.addData("Accuracy", accuracy);
            telemetry.addLine("Press B to continue...");
            telemetry.update();

            bestToleranceFinder.CompletedTestResults(driveTime, accuracy, rotationAccuracy);

            while(!this.gamepad1.b && opModeIsActive()){
                // Wait to continue
            }
        }

        telemetry.addLine("Test has been complete...");
        telemetry.addData("Final Tolerance", bestToleranceFinder.GetCurrentTolerance());
        telemetry.addLine("Press A to finish...");
        telemetry.update();

        while (!this.gamepad1.a && opModeIsActive()){        }
    }

    private float AccuracyCalculator(Position position, PinpointPosGet pinpointPosGet)
    {
        Position currentPosition = pinpointPosGet.getPosi();

        // Calculate differences
        float deltaX = currentPosition.x() - position.x();
        float deltaY = currentPosition.y() - position.y();

        // Euclidean distance formula: sqrt(dX^2 + dY^2)
        float spatialDistance = (float) Math.sqrt((double)(deltaX * deltaX) + (double)(deltaY * deltaY));

        return spatialDistance;
        // Perfect accuracy = 0. The further away, the higher the number.
    }

    private float RotationAccuracyCalculator(Position position, PinpointPosGet pinpointPosGet){
        Position currentPosition = pinpointPosGet.getPosi();

        return currentPosition.r() - position.r();
    }
}