package org.firstinspires.ftc.teamcode._TeamCode2026_2027;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class Teleop extends OpMode {

    DcMotor frontLeft, backLeft, frontRight, backRight;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotorEx.class, "FL");
        backLeft = hardwareMap.get(DcMotorEx.class, "BL");
        frontRight = hardwareMap.get(DcMotorEx.class, "FR");
        backRight = hardwareMap.get(DcMotorEx.class, "BR");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop(){
        double y  = -gamepad1.left_stick_y * 0.9;       // forward/back (Y is inverted)
        double x  =  gamepad1.left_stick_x * 0.9; // strafe
        double rx =  gamepad1.right_stick_x * 0.5;       // rotate

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        frontLeft.setPower((y + x + rx) / denominator);
        backLeft.setPower((y - x + rx) / denominator);
        frontRight.setPower((y - x - rx) / denominator);
        backRight.setPower((y + x - rx) / denominator);
    }
}
