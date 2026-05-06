package org.firstinspires.ftc.teamcode.Code_Summer_2026;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Maze Runner")
public class MazeRunner extends OpMode {
    MazeRunnerMind _mind;
    boolean _isMovingToGrid;

    @Override
    public void init(){
        _mind = new MazeRunnerMind();
    }

    @Override
    public void loop(){

        if (_isMovingToGrid){
            // Go to position then set _isMovingToGrid to false
            _isMovingToGrid = false;
        }else {
            // Retrieve info about maze mind to determine if changing direction
            int mazeMind = _mind.DoMazeRunnerMind(); // 0 means to go forward, 1 means to go right, -1 means to go left, 2 is backwards

            // Move forward or stop motors and rotate

            _isMovingToGrid = true;
        }
    }
}
