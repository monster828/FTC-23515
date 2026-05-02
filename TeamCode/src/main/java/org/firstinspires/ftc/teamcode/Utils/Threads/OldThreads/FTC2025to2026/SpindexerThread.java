package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads.FTC2025to2026;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.ColorDisplay;

public class SpindexerThread extends Thread {

    SpindexerComm comm;
    Servo SD;
    Servo Flipper;
    ColorSensor back;
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

    boolean bP = true;
    boolean bL = false;
    boolean lP = true;
    boolean lL = false;
    boolean rP = true;
    boolean rL = false;

    /** Create a new SpindexerThread.
     * @param comm The communications to talk between the opMode and the thread
     * @param SD The servo that rotates the spindexer
     * @param Flipper The flipper that lifts the ball into the cannon
     * @param back The back color sensor (shooting position)
     * @param left The sensor to the left
     * @param right The sensor to the right
     * @param tem (Optional) Telemetry from the opmode.
     * **/
    public SpindexerThread(SpindexerComm comm,Servo SD, Servo Flipper, ColorSensor back, ColorSensor left, ColorSensor right, Telemetry tem) {
        this.SD = SD;
        this.Flipper = Flipper;
        this.back = back;
        this.left = left;
        this.right = right;
        this.tem = tem;
        this.comm = comm;
    }

    /** Create a new SpindexerThread.
     * @param comm The communications to talk between the opMode and the thread
     * @param SD The servo that rotates the spindexer
     * @param Flipper The flipper that lifts the ball into the cannon
     * @param back The back color sensor (shooting position)
     * @param left The sensor to the left
     * @param right The sensor to the right
     * **/
    public SpindexerThread(SpindexerComm comm,Servo SD, Servo Flipper, ColorSensor back, ColorSensor left, ColorSensor right) {
        this.SD = SD;
        this.Flipper = Flipper;
        this.back = back;
        this.left = left;
        this.right = right;
        this.comm = comm;
    }

    /** Create a new SpindexerThread.
     * @param comm The communications to talk between the opMode and the thread
     * @param SD The servo that rotates the spindexer
     * @param Flipper The flipper that lifts the ball into the cannon
     * @param back The back color sensor (shooting position)
     * @param left The sensor to the left
     * @param right The sensor to the right
     * @param indicator An optional LED indidcator light
     * **/
    public SpindexerThread(SpindexerComm comm, Servo SD, Servo Flipper, ColorSensor back, ColorSensor left, ColorSensor right, ColorDisplay indicator) {
        this.SD = SD;
        this.Flipper = Flipper;
        this.back = back;
        this.left = left;
        this.right = right;
        this.comm = comm;
        this.light = indicator;
    }

    /** Create a new SpindexerThread.
     * @param comm The communications to talk between the opMode and the thread
     * @param SD The servo that rotates the spindexer
     * @param Flipper The flipper that lifts the ball into the cannon
     * @param back The back color sensor (shooting position)
     * @param left The sensor to the left
     * @param right The sensor to the right
     * @param indicator An optional LED indidcator light
     * @param op (Optional) The opmode that started the thread
     * **/
    public SpindexerThread(LinearOpMode op, SpindexerComm comm, Servo SD, Servo Flipper, ColorSensor back, ColorSensor left, ColorSensor right, ColorDisplay indicator) {
        this.SD = SD;
        this.Flipper = Flipper;
        this.back = back;
        this.left = left;
        this.right = right;
        this.comm = comm;
        this.light = indicator;
        opMode = op;
    }

    /** Create a new SpindexerThread.
     * @param comm The communications to talk between the opMode and the thread
     * @param SD The servo that rotates the spindexer
     * @param Flipper The flipper that lifts the ball into the cannon
     * @param back The back color sensor (shooting position)
     * @param left The sensor to the left
     * @param right The sensor to the right
     * @param telemetry (Optional) Telemetry from the opmode.
     * @param op (Optional) The opmode that started the thread
     * **/
    public SpindexerThread(LinearOpMode op, SpindexerComm comm, Servo SD, Servo Flipper, ColorSensor back, ColorSensor left, ColorSensor right, Telemetry telemetry) {
        this.SD = SD;
        this.Flipper = Flipper;
        this.back = back;
        this.left = left;
        this.right = right;
        this.comm = comm;
        this.tem = telemetry;
        opMode = op;
    }

    float zeroPos = 0.49f;
    long spinDelay = 425;

    @Override
    public void run() {
        try {
            SD.setPosition(zeroPos);
            Flipper.setPosition(0);
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
                    tem.addData("Back: Loaded?", bL+" Purple?"+bP);
                    tem.addData("Right: Loaded?", rL+" Purple?"+rP);
                }
                if (shoot) {

                    Flipper.setPosition(0.077f);
                    sleep(500); //450
                    Flipper.setPosition(0.0f);
                    sleep(150); //200

                    SD.setPosition(zeroPos);
                    if(SDchange != 0) {
                        SDchange = 0;
                        sleep(600); //600 / 250 / 350 / 280
                    }
                    updateColors();
                    comm.setBallcount(ballcount);
                    //if((bL && bP && demand == 1) || (bL && !bP && demand == 2))sleep(270); //nada / 350 / 250 / 320

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
        if (demand != 0) {
            if (demand == 1) {
                if (!(bP && bL)) {
                    if (lP && lL) {
                        SDchange += 0.078f;
                        comm.setBusy(true);
                        SD.setPosition(zeroPos+SDchange);
                        if(tem != null) {
                            tem.addData("Demand: ", demand);
                            tem.addData("Loading: ", "Purple from left");
                            tem.update();
                        }
                        sleep(spinDelay); //600
                        comm.setBusy(false);
                    } else if (rP && rL) {
                        SDchange -= 0.078f;
                        comm.setBusy(true);
                        SD.setPosition(zeroPos+SDchange);
                        if(tem != null) {
                            tem.addData("Demand: ", demand);
                            tem.addData("Loading: ", "Purple from right");
                            tem.update();
                        }
                        sleep(spinDelay);
                        comm.setBusy(false);
                    }
                }
            } else {
                if (!(!bP && bL)) {
                    if (!lP && lL) {
                        SDchange += 0.078f;
                        comm.setBusy(true);
                        SD.setPosition(zeroPos+SDchange);
                        if(tem != null) {
                            tem.addData("Demand: ", demand);
                            tem.addData("Loading: ", "Green from left");
                            tem.update();
                        }
                        sleep(spinDelay);
                        comm.setBusy(false);
                    } else if (!rP && rL) {
                        SDchange -= 0.078f;
                        comm.setBusy(true);
                        SD.setPosition(zeroPos+SDchange);
                        if(tem != null) {
                            tem.addData("Demand: ", demand);
                            tem.addData("Loading: ", "Green from right");
                            tem.update();
                        }
                        sleep(spinDelay);
                        comm.setBusy(false);
                    }
                }
            }
            if(!bL && lL && ballcount == 1 && comm.getLoading()) {
                SDchange += 0.078f;
                comm.setBusy(true);
                SD.setPosition(zeroPos+SDchange);
                if(tem != null) {
                    tem.addData("Demand: ", demand);
                    tem.addData("Loading: ", "Something from left");
                    tem.update();
                }
                sleep(spinDelay);
                comm.setBusy(false);
            } else if(!bL && rL && ballcount == 1 && comm.getLoading()) {
                SDchange -= 0.078f;
                comm.setBusy(true);
                SD.setPosition(zeroPos+SDchange);
                if(tem != null) {
                    tem.addData("Demand: ", demand);
                    tem.addData("Loading: ", "Something from right");
                    tem.update();
                }
                sleep(spinDelay);
                comm.setBusy(false);
            }
        }
    }

    /**
     * Updates the ball positions
     */
    private void updateColors() {
        ballcount = 0;
        bL = back.alpha() > 400;
        if(bL) {
            ballcount++;
            bP = back.blue() > back.green();
        }
        lL = left.alpha() > 600;
        if(lL) {
            ballcount++;
            lP = left.blue() > left.green();
        }
        rL = right.alpha() > 600;
        if(rL) {
            ballcount++;
            rP = right.blue() > right.green();
        }
    }

}
