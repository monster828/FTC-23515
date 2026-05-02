package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads.FTC2025to2026;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

public class CannonThread extends Thread {

    Servo G;
    Servo P;
    DcMotor Gm;
    DcMotor Pm;
    CannonThreadComm comm;
    Telemetry tem;


    public CannonThread(CannonThreadComm comm, Servo G, Servo P, DcMotor Gm, DcMotor Pm, Telemetry tem) {
        this.G = G;
        this.P = P;
        this.Gm = Gm;
        this.Pm = Pm;
        this.comm = comm;
        this.tem = tem;
    }

    @Override
    public void run() {
        try {
            long delaystart = System.currentTimeMillis();
            while (System.currentTimeMillis()-delaystart<30000) {
                float x = comm.getD();
                float y = comm.getY();
                float Vp = MiscUtils.CannonPurpleVperP((float) Pm.getPower());
                float Vg = MiscUtils.CannonGreenVperP((float) Gm.getPower());
                tem.addData("Purple Muzzle Velocity", Vp);
                tem.addData("Green Muzzle Velocity", Vg);
                float g = 386.4f; //in/s^2
                double angleP = Math.pow(Vp, 4) - (g * ((g * Math.pow(x, 2)) + (2 * y * Math.pow(Vp, 2))));
                double angleG = Math.pow(Vg, 4) - (g * ((g * Math.pow(x, 2)) + (2 * y * Math.pow(Vg, 2))));
                //tem.addData("The thingy", angleP);
                if (angleP > 0) {
                    angleP = Math.atan((Math.pow(Vp, 2) + Math.sqrt(angleP)) / (g * x));
                    tem.addData("Purple ideal angle", angleP);
                    if (angleP < 1.26 && angleP > 0.98) {
                        //1 = 72.5
                        //0.9 = 70.8
                        //0.73 = 56.7
                        //71.3333 degrees/servo, 1.245 rad/servo
                        //0 = 11.86? 0.206 radians
                        angleP = (angleP * 0.8673f)-0.132f;
                        tem.addData("Purple servo input", angleP);
                        if (angleP > 0.73 && angleP < 1.0) {
                            tem.addLine("Purple is go");
                            P.setPosition(angleP);
                            comm.setReadyP(true);
                        } else {
                            comm.setReadyP(false);
                            tem.addData("woops", "Purple safety check 2 fail");
                        }
                    } else {
                        comm.setReadyP(false);
                        tem.addData("woops", "Purple safety check 1 fail");
                    }
                } else {
                    tem.addData("woops", "Purple too far");
                    comm.setReadyP(false);
                }
                if (angleG > 0) {
                    angleG = Math.atan((Math.pow(Vp, 2) + Math.sqrt(angleG)) / (g * x));
                    tem.addData("Green ideal angle", angleG);
                    if (angleG < 1.258 && angleG > 0.93) {
                        //1 = 72.1
                        //0.9 = 69.2
                        //0.73 = 53.3
                        //71.3333 degrees/servo, 1.245 rad/servo
                        //0 = 11.86? 0.206 radians
                        angleG = (angleG * 0.753f)+0.0245f;
                        tem.addData("Green servo input", angleG);
                        if (angleG > 0.73 && angleG < 1.0) {
                            tem.addLine("Green is go");
                            G.setPosition(angleG);
                            comm.setReadyG(true);
                        } else {
                            comm.setReadyG(false);
                            tem.addData("woops", "Green safety check 2 fail");
                        }
                    } else {
                        comm.setReadyG(false);
                        tem.addData("woops", "Green safety check 1 fail");
                    }
                } else {
                    tem.addData("woops", "Green too far");
                    comm.setReadyG(false);
                }
                try {
                    sleep(50);
                } catch (InterruptedException e) {

                }
            }
        } catch (Exception ignored) {

        }
    }
}
