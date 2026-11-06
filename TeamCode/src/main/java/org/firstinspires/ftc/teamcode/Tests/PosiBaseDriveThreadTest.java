package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.Movement.Pathfinder;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.Utils.Threads.PosiBasedDriveThread;

import java.util.Random;

@Autonomous
public class PosiBaseDriveThreadTest extends LinearOpMode2026 {

    @Override
    public void runOpMode() throws InterruptedException {
        config();
        Random random = new Random();
        PosiBasedDriveThread dt = new PosiBasedDriveThread(this);
        Pathfinder.compileDefaultObstaclesLC(11);
        posGet.setOffset(10,10);
        waitForStart();
        dt.start();
        while(opModeIsActive()) {
            int x = -1; int y = -1;
            for(int i = 0; i < 30; i++) {
                int x0 = random.nextInt(120)+10;
                int y0 = random.nextInt(120)+10;
                if(!Pathfinder.obstacle[x0][y0]) break;
                x = x0; y = y0;
            }
            if(x > 0) {
                Pathfinder.pathfind(
                        (int) posGet.getPosi().x(),
                        (int) posGet.getPosi().y(),
                        (int) posGet.getPosi().r(),
                        x,
                        y
                );
                Position p = new Position(x,y,0);
                dt.queuePath(Pathfinder.pos.toArray(new Position[0]));
                while(posGet.getPosi().getDistTo(p) > 1 && opModeIsActive());
            }

        }
    }
}
