package org.firstinspires.ftc.teamcode.Utils.Movement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

public class TeleopMovementUtil {

    Gamepad gamepad1;
    DcMotor[] motors;

    public TeleopMovementUtil(Gamepad gamepad1, DcMotor[] mot){
        this.gamepad1 = gamepad1;
        motors = mot;
    }

    public void UpdateDrive(){
        double y  = -gamepad1.left_stick_y * 0.9;       // forward/back (Y is inverted)
        double x  =  gamepad1.left_stick_x * 0.9;       // strafe
        double rx =  gamepad1.right_stick_x * 0.5;      // rotate
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        motors[2].setPower((y + x + rx) / denominator);
        motors[0].setPower((y - x + rx) / denominator);
        motors[3].setPower((y - x - rx) / denominator);
        motors[1].setPower((y + x - rx) / denominator);
    }


    public void DeadDrive(){
        motors[2].setPower(0);
        motors[0].setPower(0);
        motors[3].setPower(0);
        motors[1].setPower(0);
    }

}
