package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class Armpair {
    DcMotor Left;
    DcMotor Right;
    
    public Armpair(DcMotor Left, DcMotor Right) {
        this.Left = Left;
        this.Right = Right;
    }
    
    public void SetupDirection() {
        Left.setDirection(DcMotorSimple.Direction.REVERSE);
        Right.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    
    public void setPower(double wee) {
        Left.setPower(wee);
        Right.setPower(wee);
    }
    
    public void setTargetPosition(int p_64755_) {
        Left.setTargetPosition(p_64755_);
        Right.setTargetPosition(p_64755_);
    }
    
    public void setMode(DcMotor.RunMode p_64756_) {
        Left.setMode(p_64756_);
        Right.setMode(p_64756_);
    }
    
}
