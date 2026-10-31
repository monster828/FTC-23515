package org.firstinspires.ftc.teamcode.Utils.Shooting;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.io.File;

public class TurretPIDTuner extends LinearOpMode2026 {

    String path = MiscUtils.dataFolder+"/PID.txt";

    public float loss;
    public float[] dataset = {0,10,0,-10,20,15,10,-5,-10,-15,-5,0};
    public long refresh = 500; // Time between data points in ms
    public long lossDelay = 50;
    public float adjust = 2.0f;
    float P = 5;
    float I = 3;
    float D = 1;
    String done = null;
    AprilTagsTurretTarget attt = new AprilTagsTurretTarget(turret);

    @Override
    public void runOpMode() throws InterruptedException {
        config();
        attt.kp = P; attt.ki = I; attt.kd = D;
        loss = -1;
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
            i = s.indexOf("Adjust: ");
            b = s.indexOf("\n",i);
            if(b < 0) b = s.length();
            if(i > 0) adjust = Float.parseFloat(s.substring(i+8,b));
            i = s.indexOf("Derivative: ");
            b = s.indexOf("\n",i);
            if(b < 0) b = s.length();
            if(i > 0) D = Float.parseFloat(s.substring(i+12,b));
            i = s.indexOf("Loss: ");
            b = s.indexOf("\n",i);
            if(b < 0) b = s.length();
            if(i > 0) loss = Float.parseFloat(s.substring(i+6,b));
            i = s.indexOf("Done: ");
            b = s.indexOf("\n",i);
            if(b < 0) b = s.length();
            if(i > 0) done = s.substring(i+6,b);
        } else {
            String def = "Proportional: 5\nIntegral: 3\nDerivative: 1\nAdjust: 2";
            MiscUtils.writeFile(path,def.getBytes());
        }
        if(loss == -1) {
            loss = getLoss();
        }
        while(opModeIsActive()) {
            if(done == null) {
                float nP = P;
                attt.kp = P / adjust;
                printTestTem();
                float tLoss = getLoss();
                if (tLoss < loss) {
                    nP = (float) attt.kp;
                    loss = tLoss;
                }
                attt.kp = P * adjust;
                printTestTem();
                tLoss = getLoss();
                if (tLoss < loss) {
                    nP = (float) attt.kp;
                    loss = tLoss;
                }
                P = nP;
                done = "P";
                save();
            }

            if(done.equals("P") && opModeIsActive()) {
                float nI = I;
                attt.ki = I / adjust;
                printTestTem();
                float tLoss = getLoss();
                if (tLoss < loss) {
                    nI = (float) attt.ki;
                    loss = tLoss;
                }
                attt.ki = I * adjust;
                printTestTem();
                tLoss = getLoss();
                if (tLoss < loss) {
                    nI = (float) attt.ki;
                    loss = tLoss;
                }
                I = nI;
                done = "I";
                save();
            }

            if(opModeIsActive()) {
                float nD = D;
                attt.kd = D / adjust;
                printTestTem();
                float tLoss = getLoss();
                if (tLoss < loss) {
                    nD = (float) attt.kd;
                    loss = tLoss;
                }
                attt.kd = D * adjust;
                printTestTem();
                tLoss = getLoss();
                if (tLoss < loss) {
                    nD = (float) attt.kd;
                    loss = tLoss;
                }
                D = nD;
                done = null;
                save();
            }
        }
    }

    public float getLoss() {
        float l = 0;
        for(int i = 1; i < dataset.length; i++) {
            attt.ChangeAngle(dataset[i]-dataset[i-1]);
            long start = System.currentTimeMillis();
            while(System.currentTimeMillis()-start < refresh && opModeIsActive()) {
                long start2 = System.currentTimeMillis();
                while(System.currentTimeMillis()-start2 < lossDelay && opModeIsActive());
                l += (Math.abs(turret.getCurrentPosition()-dataset[i])*(537.7f)/360.0f)*(lossDelay/1000.0f);
            }
        }
        return l;
    }

    public void save() {
        String s = "Proportional: "+P+"\nIntegral: "+I+"\nDerivative: "+D+"\nAdjust: "+adjust+"\nLoss: "+loss;
        if(done != null) s = s+"\nDone: "+done;
        MiscUtils.writeFile(path,s.getBytes());
    }

    public void printTestTem() {
        telemetry.addLine("Testing:");
        telemetry.addData("P",attt.kp);
        telemetry.addData("I",attt.ki);
        telemetry.addData("D",attt.kd);
        telemetry.addData("Loss so far",loss);
        telemetry.update();
    }
}
