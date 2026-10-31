package org.firstinspires.ftc.teamcode.Utils.Shooting;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.io.File;

@Autonomous
public class TurretPIDTuner extends LinearOpMode2026 {

    String path = MiscUtils.dataFolder+"/PID.txt";

    public float loss;
    public float[] dataset = {0,90};
    public long refresh = 2000; // Time between data points in ms
    public long lossDelay = 20;
    public float adjust = 2.0f;
    float P = 10f;
    float I = 3f;
    float D = 0f;
    String done = null;
    AprilTagsTurretTarget attt;

    @Override
    public void runOpMode() throws InterruptedException {
        config();
        attt = new AprilTagsTurretTarget(turret);
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
        waitForStart();
        if(loss == -1) {
            loss = getLoss();
        }
        while(opModeIsActive()) {
            if(done == null) {
                float nP = P;
                attt.kp = P / (adjust+1);
                printTestTem();
                float tLoss = getLoss();
                if (tLoss < loss) {
                    nP = (float) attt.kp;
                    loss = tLoss;
                }
                attt.kp = P * (adjust+1);
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
                attt.ki = I / (adjust+1);
                printTestTem();
                float tLoss = getLoss();
                if (tLoss < loss) {
                    nI = (float) attt.ki;
                    loss = tLoss;
                }
                attt.ki = I * (adjust+1);
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
                attt.kd = D / (adjust+1);
                printTestTem();
                float tLoss = getLoss();
                if (tLoss < loss) {
                    nD = (float) attt.kd;
                    loss = tLoss;
                }
                attt.kd = D * (adjust+1);
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
            adjust /= 1.5f;
        }
    }

    public float getLoss() {
        float p = (float) attt.kp; float in = (float) attt.ki; float d = (float) attt.kd;
        attt.kp = 10f; attt.ki = 3f; attt.kd = 0f;
        while(Math.abs((turret.getCurrentPosition()*537.7f)/(360.0f/((float) 43 /14))-dataset[0]) > 5f) {
            attt.ChangeAngle(dataset[0]-turret.getCurrentPosition());
        }
        attt.kp = p; attt.ki = in; attt.kd = d;
        float l = 0;
        for(int i = 1; i < dataset.length; i++) {
            long start = System.currentTimeMillis();
            long prev = System.currentTimeMillis();
            while(System.currentTimeMillis()-start < refresh && opModeIsActive()) {
                attt.ChangeAngle(dataset[i]-turret.getCurrentPosition());
                l += Math.abs(((turret.getCurrentPosition()*537.7f)/360.0f/((float) 43 /14))-dataset[i])*((System.currentTimeMillis()-prev)/1000.0f);
                prev = System.currentTimeMillis();
            }
        }
        return l;
    }

    public void save() {
        String s = "Proportional: "+P+"\nIntegral: "+I+"\nDerivative: "+D+"\nAdjust: "+adjust+"\nLoss: "+loss;
        if(done != null) s = s+"\nDone: "+done;
        File f = new File(s); f.delete();
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
