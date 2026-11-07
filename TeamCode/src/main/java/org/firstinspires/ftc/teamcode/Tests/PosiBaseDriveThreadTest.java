package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.Movement.Pathfinder;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.RobotOdoSetup;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.Utils.Threads.PosiBasedDriveThread;

import java.util.Random;

@TeleOp
public class PosiBaseDriveThreadTest extends LinearOpMode2026 {

    @Override
    public void runOpMode() throws InterruptedException {
        config();
        //For starter bot
        mot[0].setDirection(DcMotorSimple.Direction.FORWARD); mot[2].setDirection(DcMotorSimple.Direction.REVERSE);
        posGet.setSetup(RobotOdoSetup.PINPOINTSTARTER2026);
        Random random = new Random();
        PosiBasedDriveThread dt = new PosiBasedDriveThread(this);
        Pathfinder.compileDefaultObstaclesLC(11);
        posGet.setOffset(12,12);
        waitForStart();
        dt.start();
        while(opModeIsActive()) {
            int x = -1; int y = -1;
            for(int i = 0; i < 30; i++) {
                int x0 = random.nextInt(120)+10;
                int y0 = random.nextInt(120)+10;
                if(!Pathfinder.obstacle[x0][y0]) {
                    x = x0;
                    y = y0;
                    break;
                }
            }
            if(x > 0) {
                Pathfinder.pathfind(
                        (int) posGet.getPosi().x(),
                        (int) posGet.getPosi().y(),
                        (int) posGet.getPosi().r(),
                        x,
                        y
                );
                telemetry.addData("Current pos",posGet.getPosi());
                telemetry.addData("Target pos", x+","+y);
                Pathfinder.saveI(1,40,"DriveThreadTest");
                Position p = new Position(x,y,0);
                dt.queuePath(Pathfinder.pos.toArray(new Position[0]));
                while(posGet.getPosi().getDistTo(p) > 1 && opModeIsActive() && !gamepad1.a);
                while(gamepad1.a);
            }

        }
    }
}
