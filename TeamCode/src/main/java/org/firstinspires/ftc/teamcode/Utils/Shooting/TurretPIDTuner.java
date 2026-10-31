package org.firstinspires.ftc.teamcode.Utils.Shooting;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.io.File;

public class TurretPIDTuner extends LinearOpMode2026 {

    String path = MiscUtils.dataFolder+"/PID.txt";

    public float loss;
    public float[] dataset = {10,0,-10,20,15,10,-5,-10,-15,-5,0};
    public long refresh = 500; // Time between data points in ms
    public long lossDelay = 50;

    @Override
    public void runOpMode() throws InterruptedException {
        config();
        float P = 5;
        float I = 3;
        float D = 1;
        if(MiscUtils.checkFile(path)) {
            String s = new String(MiscUtils.readFile(path));
            int i = s.indexOf("Proportional: ");
            int b = s.indexOf("\n",i);
            if(b < 0) b = s.length();
            if(i > 0) P = Float.parseFloat(s.substring(i+14,b));
            i = s.indexOf("Integral: ");
            b = s.indexOf("\n",i);
            if(b < 0) b = s.length();
            if(i > 0) I = Float.parseFloat(s.substring(i+10,b));
            i = s.indexOf("Derivative: ");
            b = s.indexOf("\n",i);
            if(b < 0) b = s.length();
            if(i > 0) D = Float.parseFloat(s.substring(i+12,b));
        } else {
            String def = "Proportional: 5\nIntegral: 3\nDerivative: 1";
            MiscUtils.writeFile(path,def.getBytes());
        }
        while(opModeIsActive()) {

        }
    }

    public void getLoss() {
        for(int i = 0; i < dataset.length; i++) {

            long start = System.currentTimeMillis();
            while(System.currentTimeMillis()-start < refresh && opModeIsActive()) {
                
                long start2 = System.currentTimeMillis();
                while(System.currentTimeMillis()-start2 < lossDelay && opModeIsActive());
            }
        }
    }
}
