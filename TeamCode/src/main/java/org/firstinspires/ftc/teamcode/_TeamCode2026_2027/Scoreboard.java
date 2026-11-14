package org.firstinspires.ftc.teamcode._TeamCode2026_2027;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.GUI.RoButton;
import org.firstinspires.ftc.teamcode.Utils.GUI.RoDropdown;
import org.firstinspires.ftc.teamcode.Utils.GUI.RoGUI;
import org.firstinspires.ftc.teamcode.Utils.GUI.RoNumInput;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.ScoreBoard;

@TeleOp(name="Z Scoreboard")
public class Scoreboard extends LinearOpMode2026 {
    @Override
    public void runOpMode() throws InterruptedException {
        config();
        ScoreBoard scores = new ScoreBoard(MiscUtils.dataFolder+"/scores.scb",false);
        RoGUI gui = new RoGUI(telemetry,gamepad1);
        RoDropdown P1 = new RoDropdown("Driver ",telemetry);
        RoDropdown P2 = new RoDropdown("Operator ",telemetry);
        for(int i = 0; i < people.values().length; i++) {
            P1.addOption(people.values()[i].toString());
            P2.addOption(people.values()[i].toString());
        }
        RoNumInput scoreIn = new RoNumInput("Score",5,0,telemetry);
        RoButton addScore = new RoButton(telemetry,"Add Score");
        gui.add(P1); gui.add(P2); gui.add(scoreIn); gui.add(addScore);
        waitForStart();
        while(opModeIsActive()) {
            gui.defaultLoop();
            int[][] leaderboard = scores.getLeaderboard(15,true);
            telemetry.addLine();
            for(int i = 0; i < leaderboard.length; i++) {
                telemetry.addData(String.valueOf(i+1),
                        people.values()[leaderboard[i][0]].toString()
                                +" and "+
                                people.values()[leaderboard[i][1]].toString()
                                +" "+leaderboard[i][2]
                        );
            }
            telemetry.update();
            if((boolean) addScore.getOutput()) {
                scores.addScore((Integer) P1.getOutput(),(Integer) P2.getOutput(),(Integer) scoreIn.getOutput());
            }
            sleep(10);
        }
    }

    enum people {
        NOBODY,
        NOBODY2,
        NOBODY3
    }
}
