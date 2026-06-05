package org.firstinspires.ftc.teamcode.DriveTests_2026;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utils.Logger;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PinpointPosGet;
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

        mot[0].setDirection(DcMotorSimple.Direction.REVERSE);
        mot[2].setDirection(DcMotorSimple.Direction.REVERSE);
        GoBildaPinpointDriver pin = hardwareMap.get(GoBildaPinpointDriver.class,"POC");
        PinpointPosGet posGet = new PinpointPosGet(pin);
        String configPath = MiscUtils.dataFolder+"config2026.robocfg";
        int i = MiscUtils.readConfig(configPath, (byte) 0);
        File file = MiscUtils.getRobopathsIn(new File(MiscUtils.dataFolder))[i];
        MoveThreadComm move = new MoveThreadComm();
        MoveThread m = new MoveThread(this,move,mot,
                file,
                telemetry,
                posGet
        );
        File loggerFile = new File(MiscUtils.dataFolder+"/log.robolog");
        Logger l = new Logger(loggerFile);
        m.addLog(l);
        waitForStart();
        l.add("Opmode Started",new byte[0]);
        m.start();
        move.start();
        while(move.isRunning());
        telemetry.addData("Move time",move.getDriveTime());
        telemetry.addData("Time difference",move.getAhead());
        telemetry.update();
        sleep(30000);
    }
}
