package org.firstinspires.ftc.teamcode.DriveTests_2026;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.GUI.RoFileSelector;
import org.firstinspires.ftc.teamcode.Utils.GUI.RoGUI;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

@TeleOp
public class HomemadeMoveCodeConfig extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        RoGUI gui = new RoGUI(telemetry);
        String path = MiscUtils.dataFolder;
        RoFileSelector fileSelect = new RoFileSelector("Move: ",telemetry,path);
        String configPath = MiscUtils.dataFolder+"config2026.robocfg";
        gui.add(fileSelect);
        if(MiscUtils.checkFile(configPath)) {
            gui.get(0).setOutput((int)(128+MiscUtils.readConfig(configPath, (byte) 0)));
        }
        gui.render();
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {
            if(this.gamepad1.dpad_down) {
                gui.down();
                while(this.gamepad1.dpad_down)
                {
                    gui.render();
                    telemetry.update();
                }
            }
            if(this.gamepad1.dpad_up) {
                gui.up();
                while(this.gamepad1.dpad_up)
                {
                    gui.render();
                    telemetry.update();
                }
            }
            if(this.gamepad1.dpad_right) {
                gui.right();
                while(this.gamepad1.dpad_right)
                {
                    gui.render();
                    telemetry.update();
                }
            }
            if(this.gamepad1.dpad_left) {
                gui.left();
                while(this.gamepad1.dpad_left)
                {
                    gui.render();
                    telemetry.update();
                }
            }
            if(this.gamepad1.left_bumper) {
                gui.interact();
                while(this.gamepad1.left_bumper)
                {
                    gui.render();
                    telemetry.update();
                }
            }
        }
        MiscUtils.writeToConfig(configPath, (byte) 0, (byte) (fileSelect.getSelectedI()-128));
    }
}
