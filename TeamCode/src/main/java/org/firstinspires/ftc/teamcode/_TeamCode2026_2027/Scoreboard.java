package org.firstinspires.ftc.teamcode._TeamCode2026_2027;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.GUI.RoGUI;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.ScoreBoard;

@TeleOp
@Disabled
public class Scoreboard extends LinearOpMode2026 {
    @Override
    public void runOpMode() throws InterruptedException {
        config();
        ScoreBoard scores = new ScoreBoard(MiscUtils.dataFolder+"/scores.scb",false);
        RoGUI gui = new RoGUI(telemetry,gamepad1);
        while(opModeIsActive()) {
            gui.defaultLoop();
            int[][] leaderboard = scores.getLeaderboard(15,true);
        }
    }
}
