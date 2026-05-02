package org.firstinspires.ftc.teamcode.Utils.Threads.OldThreads.FTC2025to2026;

import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PositionGetter;
import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

import java.util.List;

public class TurretAimThread extends Thread {

    private Limelight3A lime;
    private TurretAimThreadComm comm;
    private boolean status = false;
    private int pipe;
    private DcMotorEx turret;
    private int startAngle;
    private Telemetry tem;
    private int kink = 0;
    private LinearOpMode opMode;
    private PositionGetter posGet;
    private Position pos;

    /**
     * Automatically aims the turret
     * @param limelight Limelight to detect the goal
     * @param comm Comms to to the main OpMode
     * @param turret The motor that controls the turret
     * @param pipeline The limelight pipeline to use
     */
    public TurretAimThread(Limelight3A limelight, TurretAimThreadComm comm, DcMotorEx turret, int pipeline) {
        lime = limelight;
        this.comm = comm;
        this.turret = turret;
        this.pipe = pipeline;
    }
    /**
     * Automatically aims the turret
     * @param limelight Limelight to detect the goal
     * @param comm Comms to to the main OpMode
     * @param turret The motor that controls the turret
     * @param pipeline The limelight pipeline to use
     * @param kink An additional offset for the turret
     */
    public TurretAimThread(Limelight3A limelight, TurretAimThreadComm comm, DcMotorEx turret, int pipeline, int kink) {
        lime = limelight;
        this.comm = comm;
        this.turret = turret;
        this.pipe = pipeline;
        this.kink = kink;
    }
    /**
     * Automatically aims the turret
     * @param limelight Limelight to detect the goal
     * @param comm Comms to to the main OpMode
     * @param turret The motor that controls the turret
     * @param pipeline The limelight pipeline to use
     * @param tem Telemetry for debugging
     * @param kink An additional offset for the turret
     */
    public TurretAimThread(Limelight3A limelight, TurretAimThreadComm comm, DcMotorEx turret, int pipeline,Telemetry tem, int kink) {
        lime = limelight;
        this.comm = comm;
        this.turret = turret;
        this.pipe = pipeline;
        this.tem = tem;
        this.kink = kink;
    }
    /**
     * Automatically aims the turret
     * @param limelight Limelight to detect the goal
     * @param comm Comms to to the main OpMode
     * @param turret The motor that controls the turret
     * @param pipeline The limelight pipeline to use
     * @param tem Telemetry for debugging
     */
    public TurretAimThread(Limelight3A limelight, TurretAimThreadComm comm, DcMotorEx turret, int pipeline, Telemetry tem) {
        lime = limelight;
        this.comm = comm;
        this.turret = turret;
        this.pipe = pipeline;
        this.tem = tem;
    }
    /**
     * Automatically aims the turret
     * @param op The main OpMode to detect when to stop
     * @param limelight Limelight to detect the goal
     * @param comm Comms to to the main OpMode
     * @param turret The motor that controls the turret
     * @param pipeline The limelight pipeline to use
     * @param tem Telemetry for debugging
     */
    public TurretAimThread(LinearOpMode op, Limelight3A limelight, TurretAimThreadComm comm, DcMotorEx turret, int pipeline, Telemetry tem) {
        lime = limelight;
        this.comm = comm;
        this.turret = turret;
        this.pipe = pipeline;
        this.tem = tem;
        opMode = op;
    }
    /**
     * Automatically aims the turret
     * @param op The main OpMode to detect when to stop
     * @param limelight Limelight to detect the goal
     * @param comm Comms to to the main OpMode
     * @param turret The motor that controls the turret
     * @param pipeline The limelight pipeline to use
     * @param kink An additional offset for the turret
     */
    public TurretAimThread(LinearOpMode op, Limelight3A limelight, TurretAimThreadComm comm, DcMotorEx turret, int pipeline, int kink) {
        lime = limelight;
        this.comm = comm;
        this.turret = turret;
        this.pipe = pipeline;
        this.kink = kink;
        opMode = op;
    }
    /**
     * Automatically aims the turret
     * @param op The main OpMode to detect when to stop
     * @param limelight Limelight to detect the goal
     * @param comm Comms to to the main OpMode
     * @param turret The motor that controls the turret
     * @param pipeline The limelight pipeline to use
     * @param kink An additional offset for the turret
     * @param posGet A positiongetter to try to get the angle if the limelight can't see
     * @param target The position of the goal
     */
    public TurretAimThread(LinearOpMode op, Limelight3A limelight, TurretAimThreadComm comm, DcMotorEx turret, int pipeline, int kink, PositionGetter posGet, Position target) {
        lime = limelight;
        this.comm = comm;
        this.turret = turret;
        this.pipe = pipeline;
        this.kink = kink;
        opMode = op;
        this.posGet = posGet;
        this.pos = target;
    }
    /**
     * Automatically aims the turret
     * @param op The main OpMode to detect when to stop
     * @param limelight Limelight to detect the goal
     * @param comm Comms to to the main OpMode
     * @param turret The motor that controls the turret
     * @param pipeline The limelight pipeline to use
     * @param kink An additional offset for the turret (not included in posGet mode)
     * @param tem Telemetry for debugging
     * @param posGet A positiongetter to try to get the angle if the limelight can't see
     * @param target The position of the goal
     */
    public TurretAimThread(LinearOpMode op, Limelight3A limelight, TurretAimThreadComm comm, DcMotorEx turret, int pipeline, int kink, Telemetry tem, PositionGetter posGet, Position target) {
        lime = limelight;
        this.comm = comm;
        this.turret = turret;
        this.pipe = pipeline;
        this.kink = kink;
        opMode = op;
        this.tem = tem;
        this.posGet = posGet;
        this.pos = target;
    }


    @Override
    public void run() {
        startAngle = turret.getCurrentPosition();
        long delaystart = System.currentTimeMillis();
        boolean opModeCheck = true;
        while (System.currentTimeMillis() - delaystart < 30000 && opModeCheck) {
            if(comm.getOffset() != 0) {
                startAngle += comm.getOffset();
                comm.Offset(0);
            }
            if(comm.getActive()) {
                if(tem != null) tem.addData("Status ",status);
                if(!status) {
                    lime.start();
                    lime.pipelineSwitch(pipe);
                    status = true;
                }
                List<LLResultTypes.FiducialResult> tags = lime.getLatestResult().getFiducialResults();
                if(tags.toArray().length > 0) {
                    int targetAngle = (int) (turret.getCurrentPosition()+(tags.get(0).getTargetXDegrees()*6f)+kink);
                    targetAngle = (int) MiscUtils.Clamp(targetAngle, startAngle - 400f, startAngle + 400f);
                    turret.setTargetPosition(targetAngle);
                    turret.setPower(0.8);
                    if(tem != null)tem.addData("Target Position ",targetAngle);
                } else {
                    if(posGet != null) {
                        float xDist = pos.x() - posGet.getPosi().x();
                        float yDist = pos.y() - posGet.getPosi().y();
                        float angle = (float) Math.toDegrees(Math.atan2(xDist,yDist));
                        turret.setTargetPosition((int) (MiscUtils.Clamp(startAngle+((angle-posGet.getPosi().r())*7f), startAngle - 800, startAngle + 800)));
                        turret.setPower(0.8);
                        if (tem != null) tem.addData("Target Position ", pos.toString());
                        if (tem != null) tem.addData("Target Angle ", angle);
                    } else {
                        turret.setTargetPosition(startAngle);
                        if (tem != null) tem.addData("Target Position ", "No tags");
                    }
                }
            } else {
                turret.setTargetPosition(startAngle);
                //lime.close();
                status = false;
            }
            if(tem != null)tem.update();
            if(opMode != null) opModeCheck = opMode.opModeIsActive() || opMode.opModeInInit();
        }
        lime.close();
    }
}
