package org.firstinspires.ftc.teamcode.DriveTests_2026;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PinpointPosGet;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.SparkfunPosGet;

@TeleOp
public class RotationSpeedTest extends LinearOpMode2026 {

    @Override
    public void runOpMode() throws InterruptedException {
        /*GoBildaPinpointDriver pin = hardwareMap.get(GoBildaPinpointDriver.class,"POC");
        PinpointPosGet posGet = new PinpointPosGet(pin);
        DcMotor[] mot = {
                hardwareMap.get(DcMotorEx.class,"BL"), //back left
                hardwareMap.get(DcMotorEx.class,"BR"), //back right
                hardwareMap.get(DcMotorEx.class,"FL"), //front left
                hardwareMap.get(DcMotorEx.class,"FR") //front right
        };
        mot[0].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mot[1].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mot[2].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mot[3].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        mot[0].setDirection(DcMotorSimple.Direction.REVERSE);
        mot[2].setDirection(DcMotorSimple.Direction.REVERSE);*/

        config();

        waitForStart();

        long start = System.currentTimeMillis();
        DriveUtils.DriveThing(0,0,1,1,mot);
        sleep(50);
        while(!(posGet.getPosi().r() > -10 && posGet.getPosi().r() < 0));
        telemetry.addData("350 speed", 350000/(System.currentTimeMillis()-start));
        telemetry.update();
        DriveUtils.stop(mot);

        while(opModeIsActive());
    }
}
