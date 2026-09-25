package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
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
    public Limelight3A limelight;


    /**
     * Call this to configure motors and the position getter.
     */
    public void config() {
        //telemetry.speak("We're no strangers to looove, you know the rules and so do i. A full commitment is what I'm thinking of, you won't get this far from any other guy, I, just want to tell you how I'm feeling. Just wanna make you understand, never gunna give you up, never gunna let you down, never gunna run around and desert you, never gunna make you cry, never gunna say goodbye! never gunna tell a lie and hurt you!");
        try {
            limelight = hardwareMap.get(Limelight3A.class, "Limelight");
        } catch (Exception e) {
            telemetry.addData("Config error: ","Limelight not found");
        }
        try {
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
        } catch (Exception e) {
            telemetry.addData("Config error: ","Motors not found");
        }
        try {
            GoBildaPinpointDriver pin = hardwareMap.get(GoBildaPinpointDriver.class, "POC");
            posGet = new PinpointPosGet(pin);
        } catch (Exception e) {
            telemetry.addData("Config error: ","Pinpoint not found");
        }

        if(!MiscUtils.checkBattery(hardwareMap)) {
            telemetry.addData("OH NO","THE BATTERY IS LOW ON JUICE!!!!!");
            telemetry.speak("The battery is low, please fix it!");
        }
        telemetry.update();
    }
}
