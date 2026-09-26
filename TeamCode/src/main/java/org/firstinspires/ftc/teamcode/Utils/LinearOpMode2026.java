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
import java.util.Random;

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
        String[] dailyMessage = {"We're no strangers to looove, you know the rules and so do i. A full commitment is what I'm thinking of, you won't get this far from any other guy, I, just want to tell you how I'm feeling. Just wanna make you understand, never gunna give you up, never gunna let you down, never gunna run around and desert you, never gunna make you cry, never gunna say goodbye! never gunna tell a lie and hurt you!",
                                "Hello?",
                                "I've been alone with you inside my mind, And in my dreams, I've kissed your lips a thousand times, I sometimes see you pass outside my door, Hello, is it me you're looking for? I can see it in your eyes, I can see it in your smile, You're all I've ever wanted, And my arms are open wide, 'Cause you know just what to say, And you know just what to do, And I want to tell you so much, I love you... I long to see the sunlight in your hair, And tell you time and time again how much I care, Sometimes, I feel my heart will overflow... Hello, I've just got to let you know, 'Cause I wonder where you are, And I wonder what you do, Are you somewhere feeling lonely? Or is someone loving you? Tell me how to win your heart, For I haven't got a clue, But let me start by saying, I love you... Hello, is it me you're looking for? 'Cause I wonder where you are, And I wonder what you do, Are you somewhere feeling lonely? Or is someone loving you, Tell me how to win your heart, For I haven't got a clue, But let me start by saying, I love you...",
                                "Where do you live?"
        };

        Random random = new Random();
        boolean doMessage = random.nextFloat() > 0.85;

        if (doMessage){
            telemetry.speak(dailyMessage[random.nextInt(dailyMessage.length)]);
        }

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
