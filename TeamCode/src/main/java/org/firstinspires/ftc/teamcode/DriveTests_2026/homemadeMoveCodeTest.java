package org.firstinspires.ftc.teamcode.DriveTests_2026;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.SparkfunPosGet;
import org.firstinspires.ftc.teamcode.Utils.Threads.MoveThread;
import org.firstinspires.ftc.teamcode.Utils.Threads.MoveThreadComm;

import java.io.File;

@Autonomous
public class homemadeMoveCodeTest extends LinearOpMode {



    @Override
    public void runOpMode() {
        DcMotor[] mot = {
                hardwareMap.get(DcMotorEx.class,"BL"), //back left
                hardwareMap.get(DcMotorEx.class,"BR"), //back right
                hardwareMap.get(DcMotorEx.class,"FL"), //front left
                hardwareMap.get(DcMotorEx.class,"FR") //front right
        };
        SparkFunOTOS spark = hardwareMap.get(SparkFunOTOS.class,"SF");

        mot[0].setDirection(DcMotorSimple.Direction.REVERSE);
        mot[2].setDirection(DcMotorSimple.Direction.REVERSE);
        spark.resetTracking();
        SparkfunPosGet posGet = new SparkfunPosGet(spark);
        MoveThreadComm move = new MoveThreadComm();
        MoveThread m = new MoveThread(this,move,mot,
                new File(MiscUtils.dataFolder+"/testpath.robopath"),
                telemetry,
                posGet
        );
        waitForStart();
        m.start();
        sleep(30000);
    }
}
