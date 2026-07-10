package org.firstinspires.ftc.teamcode.DriveTests_2026.TestFiles;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "SLIDERS TEST")
public class TestSlides extends OpMode {
    DcMotor slide;
    DcMotor slide2;

    @Override
    public void init(){
        slide = hardwareMap.get(DcMotor.class, "NAME");
        slide2 = hardwareMap.get(DcMotor.class, "NAME");
    }

    @Override
    public void loop(){
        slide.setPower(gamepad1.left_stick_y);
        slide2.setPower(gamepad1.right_stick_y);
    }
}
