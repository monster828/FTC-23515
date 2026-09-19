package org.firstinspires.ftc.teamcode._TeamCode2026_2027;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.Controls.ControlsTrigger;
import org.firstinspires.ftc.teamcode.Utils.Controls.GamepadButton;
@TeleOp
public class GamepadTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        GamepadButton b = new GamepadButton(gamepad1, GamepadButton.GButton.left_bumper);
        ControlsTrigger c = new ControlsTrigger(b);
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("Button pressed",b.isPressed());
            telemetry.addData("Triggered",c.IsTriggered());
            telemetry.update();
            sleep(100);
        }
    }
}
