package org.firstinspires.ftc.teamcode.Code_Summer_2026;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Maze Runner")
public class MazeRunner extends OpMode {
    MazeRunnerMind _mind;

    @Override
    public void init(){
        _mind = new MazeRunnerMind();
    }

    @Override
    public void loop(){
        // Retrieve info about maze mind to determine if changing direction

        // Move forward or stop motors and rotate

    }
}
