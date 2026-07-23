package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PinpointPosGet;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;

import java.io.File;

public abstract class LinearOpMode2026 extends LinearOpMode {
    public PositionGetter posGet;
    public DcMotor[] mot;
    public String configPath = MiscUtils.dataFolder+"config2026.robocfg";
    public File loggerFile = new File(MiscUtils.dataFolder+"/log.robolog");
    public Logger log = new Logger(loggerFile);


    /**
     * Call this to configure motors and the position getter.
     */
    public void config() {
        mot = new DcMotor[]{
                hardwareMap.get(DcMotorEx.class, "BL"), //back left
                hardwareMap.get(DcMotorEx.class, "BR"), //back right
                hardwareMap.get(DcMotorEx.class, "FL"), //front left
                hardwareMap.get(DcMotorEx.class, "FR") //front right
        };
        mot[0].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mot[1].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mot[2].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mot[3].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mot[0].setDirection(DcMotorSimple.Direction.REVERSE);
        mot[2].setDirection(DcMotorSimple.Direction.REVERSE);
        GoBildaPinpointDriver pin = hardwareMap.get(GoBildaPinpointDriver.class,"POC");
        posGet = new PinpointPosGet(pin);

        if(!MiscUtils.checkBattery(hardwareMap)) {
            telemetry.addData("OH NO","THE BATTERY IS LOW ON JUICE!!!!!");
            telemetry.update();
            telemetry.speak("The battery is low, please fix it!");
        }
    }
}
