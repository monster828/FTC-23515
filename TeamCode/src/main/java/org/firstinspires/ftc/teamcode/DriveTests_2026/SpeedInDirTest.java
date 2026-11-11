package org.firstinspires.ftc.teamcode.DriveTests_2026;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;
import org.firstinspires.ftc.teamcode.Utils.Threads.PosiBasedDriveThread;

import java.util.ArrayList;

@TeleOp
public class SpeedInDirTest extends LinearOpMode2026 {

    @Override
    public void runOpMode() throws InterruptedException {
        config();
        float[] vs = new float[10]; float[] as = new float[10];
        waitForStart();
        for(int i = 0; i < 10; i += 1) {
            Position p = new Position(0, 3, 0);
            p.rotateCoords(i*10);
            p.setR(0);
            DriveUtils.DriveThing(p.y(),p.x(),0,1,mot);
            float a = 0; int it = 0;
            sleep(50);
            while(it < 50) {
                a += posGet.getA();
                it++;
                sleep(2);
            }
            DriveUtils.stop(mot);
            vs[i] = posGet.getV(); as[i] = a/it;
            for(int n = 0; n < 10; n++) {
                telemetry.addData(String.valueOf(n*10),"V: "+vs[n]+"A: "+as[n]);
            }
            telemetry.update();
            while(!gamepad1.a && opModeIsActive());
            if(!opModeIsActive()) break;
        }
    }
}
