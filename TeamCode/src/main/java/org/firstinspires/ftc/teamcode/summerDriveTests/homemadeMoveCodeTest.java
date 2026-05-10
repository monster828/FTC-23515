package org.firstinspires.ftc.teamcode.summerDriveTests;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.SparkfunPosGet;
import org.firstinspires.ftc.teamcode.Utils.Threads.MoveThread;
import org.firstinspires.ftc.teamcode.Utils.Threads.MoveThreadComm;

import java.io.File;

public class homemadeMoveCodeTest extends LinearOpMode {

    public DcMotor[] mot = {
            hardwareMap.get(DcMotorEx.class,"BL"), //back left
            hardwareMap.get(DcMotorEx.class,"BR"), //back right
            hardwareMap.get(DcMotorEx.class,"FL"), //front left
            hardwareMap.get(DcMotorEx.class,"FR") //front right
    };
    public SparkFunOTOS spark = hardwareMap.get(SparkFunOTOS.class,"spark");

    @Override
    public void runOpMode() {
        SparkfunPosGet posGet = new SparkfunPosGet(spark);
        MoveThreadComm move = new MoveThreadComm();
        MoveThread m = new MoveThread(this,move,mot,
                new File(MiscUtils.dataFolder+"/testpath.robopath"),
                telemetry,
                posGet
        );
        m.run();
    }
}
