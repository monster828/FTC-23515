package org.firstinspires.ftc.teamcode.Utils.Threads;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utils.Logger;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;

public class LogThread extends Thread {

    Logger l; long wait;
    LinearOpMode opMode;
    Object[] m; Object[] mx; Object[] s; PositionGetter p;
    public LogThread(LinearOpMode op, Logger log, long frq, DcMotor[] m, DcMotorEx[] mx, Servo[] s, PositionGetter posGet) {
        l = log; wait = frq; this.m = m; this.mx = mx; this.s = s; p = posGet;
        opMode = op;
    }

    public LogThread(LinearOpMode op, Logger log, long frq, HardwareMap h, PositionGetter posGet) {
        l = log; wait = frq; p = posGet;
        m = h.getAll(DcMotor.class).toArray();
        s = h.getAll(Servo.class).toArray();
        mx = h.getAll(DcMotorEx.class).toArray();
        opMode = op;
    }

    @Override
    public void run() {
        super.run();
        long prev = System.currentTimeMillis();
        while(opMode.opModeIsActive()) {
            if(System.currentTimeMillis() - prev > wait) {
                prev = System.currentTimeMillis();
                for(Object mo : m) {
                    DcMotor mot = (DcMotor) mo;
                    l.add(mot.getDeviceName(),new byte[]{(byte) (mot.getPower()*100)});
                }
                for(Object mo : mx) {
                    DcMotorEx mot = (DcMotorEx) mo;
                    l.add(mot.getDeviceName(),new byte[]{(byte) (mot.getPower()*100), (byte) mot.getCurrentPosition()});
                }
                for(Object ser : s) {
                    Servo serv = (Servo) ser;
                    l.add(serv.getDeviceName(),new byte[]{(byte) (serv.getPosition()*100)});
                }
                l.add("Position",new byte[]{(byte) p.getPosi().x(),(byte) p.getPosi().y(),(byte) p.getPosi().r()});
            }
        }
    }
}
