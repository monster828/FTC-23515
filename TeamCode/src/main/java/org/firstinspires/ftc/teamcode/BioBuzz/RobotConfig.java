package org.firstinspires.ftc.teamcode.BioBuzz;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.Controls.ControlsTrigger;
import org.firstinspires.ftc.teamcode.Utils.Controls.GamepadButton;
import org.firstinspires.ftc.teamcode.Utils.GUI.*;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.io.File;
import java.io.IOException;

@TeleOp(name=" Configure Robot")
public class RobotConfig extends LinearOpMode2026 {

    @Override
    public void runOpMode() throws InterruptedException {
        RoGUI gui = new RoGUI(telemetry);
        RoTab funStuff = new RoTab("Fun Stuff",telemetry);
        gui.add(funStuff);
        String path = configPath;
        RoSwitch s = new RoSwitch("Easter Eggs",telemetry);
        funStuff.add(s);
        File f = new File(path);
        try {
            if (!f.exists()) f.createNewFile();
            else {
                byte d = MiscUtils.readConfig(path,(byte) 0);
                s.setOutput(d==0);
            }
        } catch (Exception e) {
            telemetry.update();
            telemetry.addData("Error", e.toString());
            StackTraceElement[] St = e.getStackTrace();
            for (int i = 0; i < St.length; i++) telemetry.addLine(St[i].toString());
            telemetry.update();
            sleep(500);
        }
        ControlsTrigger up = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.dpad_up));
        ControlsTrigger down = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.dpad_down));
        ControlsTrigger left = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.dpad_left));
        ControlsTrigger right = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.dpad_right));
        ControlsTrigger inter = new ControlsTrigger(new GamepadButton(gamepad1, GamepadButton.GButton.left_bumper));
        waitForStart();
        while(opModeIsActive()) {
            if(up.IsTriggered()) gui.up();
            if(down.IsTriggered()) gui.down();
            if(left.IsTriggered()) gui.left();
            if(right.IsTriggered()) gui.right();
            if(inter.IsTriggered()) gui.interact();
            gui.render();
            telemetry.update();
        }
        try {
            if (!f.exists()) f.createNewFile();
            else {
                MiscUtils.writeToConfig(path, (byte)0, (byte) ((boolean) s.getOutput() ? 0 : 1));
            }
        } catch (Exception e) {
            telemetry.update();
            telemetry.addData("Error", e.toString());
            StackTraceElement[] St = e.getStackTrace();
            for (int i = 0; i < St.length; i++) telemetry.addLine(St[i].toString());
            telemetry.update();
            sleep(500);
        }
    }
}
