package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads.FTC2025to2026;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.ColorDisplay;

public class Spindexer2Thread extends Thread {

    SpindexerComm comm;
    Servo SD;
    Servo Flipper;
    ColorSensor front;
    ColorSensor left;
    ColorSensor right;
    Telemetry tem;
    ColorDisplay light;
    LinearOpMode opMode;

    boolean shoot = false;
    int demand = 0;
    float SDchange = 0;
    //final float changyness = 0.079f;
    int ballcount = 0;

    boolean fP = true;
    boolean fL = false;
    boolean lP = true;
    boolean lL = false;
    boolean rP = true;
    boolean rL = false;


    /** Create a new SpindexerThread.
     * @param comm The communications to talk between the opMode and the thread
     * @param SD The servo that rotates the spindexer
     * @param front The front color sensor (shooting position)
     * @param left The sensor to the left
     * @param right The sensor to the right
     * @param indicator An optional LED indidcator light
     * @param op (Optional) The opmode that started the thread
     * **/
    public Spindexer2Thread(LinearOpMode op, SpindexerComm comm, Servo SD, ColorSensor front, ColorSensor left, ColorSensor right, ColorDisplay indicator) {
        this.SD = SD;
        this.front = front;
        this.left = left;
        this.right = right;
        this.comm = comm;
        this.light = indicator;
        opMode = op;
    }

    /** Create a new SpindexerThread.
     * @param comm The communications to talk between the opMode and the thread
     * @param SD The servo that rotates the spindexer
     * @param front The front color sensor (shooting position)
     * @param left The sensor to the left
     * @param right The sensor to the right
     * @param telemetry (Optional) Telemetry from the opmode.
     * @param op (Optional) The opmode that started the thread
     * **/
    public Spindexer2Thread(LinearOpMode op, SpindexerComm comm, Servo SD, ColorSensor front, ColorSensor left, ColorSensor right, Telemetry telemetry) {
        this.SD = SD;
        this.front = front;
        this.left = left;
        this.right = right;
        this.comm = comm;
        this.tem = telemetry;
        opMode = op;
    }

    float zeroPos = 0.484f;

    @Override
    public void run() {
        try {
            SD.setPosition(zeroPos);
            long delaystart = System.currentTimeMillis();
            boolean opModeCheck = true;
            while (System.currentTimeMillis() - delaystart < 30000 && opModeCheck) {
                updateColors();
                comm.setBallcount(ballcount);
                if(light != null) {
                    light.setColor((float) ballcount / 5.0f);
                }
                shoot = comm.shotQueued();
                demand = comm.getDemand();
                if(tem != null) {
                    tem.addData("Demand: ", demand);
                    tem.addData("Left: Loaded?", lL+" Purple?"+lP);
                    tem.addData("Front: Loaded?", fL+" Purple?"+fP);
                    tem.addData("Right: Loaded?", rL+" Purple?"+rP);
                }
                if (shoot) {

                    SDchange -= 0.07f;
                    SD.setPosition(zeroPos+SDchange);
                    sleep(500);

                    updateColors();
                    comm.setBallcount(ballcount);
                    loadDemand();
                    comm.shot();
                    shoot = false;
                } else {
                    loadDemand();
                }
                updateColors();
                comm.setBallcount(ballcount);
                if(tem != null) tem.update();
                if(opMode != null)opModeCheck = opMode.opModeIsActive() || opMode.opModeInInit();
            }
        } catch (Exception e) {

        }
    }



    /**
     * Loads the desired ball into the back slot
     * @throws InterruptedException
     */
    private void loadDemand() throws InterruptedException {
        if(comm.getLoading() && fL) {
            SDchange += 0.07f;
            SD.setPosition(zeroPos+SDchange);
            sleep(500);
        } else {
            if(demand == 1) { //purple
                if(!fP || !fL) {
                    if(lP && lL) {
                        SDchange += 0.07f;
                        SD.setPosition(zeroPos+SDchange);
                        sleep(500);
                    }else if(rP && rL) {
                        SDchange += 0.14f;
                        SD.setPosition(zeroPos+SDchange);
                        sleep(1000);
                    }
                }
            } else if(demand == 2) { //green
                if(fP || !fL) {
                    if(!lP && lL) {
                        SDchange += 0.07f;
                        SD.setPosition(zeroPos+SDchange);
                        sleep(500);
                    }else if(!rP && rL) {
                        SDchange += 0.14f;
                        SD.setPosition(zeroPos+SDchange);
                        sleep(1000);
                    }
                }
            }
        }
    }



    /**
     * Updates the ball positions
     */
    private void updateColors() {
        ballcount = 0;
        fL = front.alpha() > 105;
        if(fL) {
            ballcount++;
            fP = front.blue() > front.green();
        }
        lL = left.alpha() > 90;
        if(lL) {
            ballcount++;
            lP = left.blue() > left.green();
        }
        rL = right.alpha() > 90;
        if(rL) {
            ballcount++;
            rP = right.blue() > right.green();
        }
    }
}
