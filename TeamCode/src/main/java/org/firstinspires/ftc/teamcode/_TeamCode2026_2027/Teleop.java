package org.firstinspires.ftc.teamcode._TeamCode2026_2027;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
@Disabled
public class Teleop extends OpMode {

    DcMotor frontLeft, backLeft, frontRight, backRight, flywheel, lift, intake, turretrotation;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotorEx.class, "FL");
        backLeft = hardwareMap.get(DcMotorEx.class, "BL");
        frontRight = hardwareMap.get(DcMotorEx.class, "FR");
        backRight = hardwareMap.get(DcMotorEx.class, "BR");
        flywheel = hardwareMap.get(DcMotorEx.class, "FW");
        lift = hardwareMap.get(DcMotorEx.class, "lift");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        turretrotation = hardwareMap.get(DcMotorEx.class, "TR");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /*ControlsTrigger on = new ControlsTrigger(
               new GamepadButton(gamepad1, GamepadButton.GButton.dpad_up)
       );
       */

    boolean throughputOn = false;
    boolean wasAPressed = false;
    double intakepow = 0;
    double liftpow = 0;

    @Override
    public void loop(){
        double y  = -gamepad1.left_stick_y * 0.9;       // forward/back (Y is inverted)
        double x  =  gamepad1.left_stick_x * 0.9;       // strafe
        double rx =  gamepad1.right_stick_x * 0.5;      // rotate

        // toggle only on the moment A goes down, not while it's held
        if (gamepad1.a && !wasAPressed) {
            throughputOn = !throughputOn;
            if (throughputOn) {
                intakepow = 0.8;
                liftpow = 1.0;
            } else {
                intakepow = 0;
                liftpow = 0;
            }
        }
        wasAPressed = gamepad1.a;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        frontLeft.setPower((y + x + rx) / denominator);
        backLeft.setPower((y - x + rx) / denominator);
        frontRight.setPower((y - x - rx) / denominator);
        backRight.setPower((y + x - rx) / denominator);
        intake.setPower(intakepow);
        lift.setPower(liftpow);
        flywheel.setPower(0.8);
    }
}
