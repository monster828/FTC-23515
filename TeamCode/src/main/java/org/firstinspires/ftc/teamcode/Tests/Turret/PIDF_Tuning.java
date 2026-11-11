package org.firstinspires.ftc.teamcode.Tests.Turret;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "PIDF_Tuning (Blocks to Java)")
public class PIDF_Tuning extends LinearOpMode {

    private DcMotorEx flywheel;

    /**
     * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
     * Comment Blocks show where to place Initialization code (runs once, after touching the
     * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
     * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
     * Stopped).
     */
    @Override
    public void runOpMode() {
        int Step_Index;
        int High_Velocity;
        int Low_Velocity;
        int curTargetVelocity;
        List Step_Sizes;
        double p;
        double f;
        double curVelocity;
        double error;

        flywheel = hardwareMap.get(DcMotorEx.class, "FW");

        // Put initialization blocks here.
        Step_Index = 1;
        High_Velocity = 1500;
        Low_Velocity = 900;
        curTargetVelocity = 900;
        Step_Sizes = JavaUtil.createListWith(10.0, 1.0, 0.1, 0.01, 0.001, 0.0001);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setDirection(DcMotor.Direction.FORWARD);
        p = 139.2;
        f = 13.19;
        ((DcMotorEx) flywheel).setVelocityPIDFCoefficients(p, 0, 0, f);
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                // Put loop blocks here.
                if (gamepad1.y) {
                    if (curTargetVelocity == High_Velocity) {
                        curTargetVelocity = 900;
                        sleep(500);
                    } else {
                        curTargetVelocity = 1500;
                        sleep(500);
                    }
                }
                if (gamepad1.b) {
                    Step_Index += 1;
                    if (Step_Index > 6) {
                        Step_Index = 1;
                    }
                    sleep(500);
                }
                if (gamepad1.dpad_left) {
                    f = f - ((Double) JavaUtil.inListGet(Step_Sizes, JavaUtil.AtMode.FROM_START, (int) (Step_Index - 1), false)).doubleValue();
                    sleep(500);
                }
                if (gamepad1.dpad_right) {
                    f = f + ((Double) JavaUtil.inListGet(Step_Sizes, JavaUtil.AtMode.FROM_START, (int) (Step_Index - 1), false)).doubleValue();
                    sleep(500);
                }
                if (gamepad1.dpad_up) {
                    p = p + ((Double) JavaUtil.inListGet(Step_Sizes, JavaUtil.AtMode.FROM_START, (int) (Step_Index - 1), false)).doubleValue();
                    sleep(500);
                }
                if (gamepad1.dpad_down) {
                    p = p - ((Double) JavaUtil.inListGet(Step_Sizes, JavaUtil.AtMode.FROM_START, (int) (Step_Index - 1), false)).doubleValue();
                    sleep(500);
                }
                ((DcMotorEx) flywheel).setVelocityPIDFCoefficients(p, 0, 0, f);
                ((DcMotorEx) flywheel).setVelocity(curTargetVelocity);
                curVelocity = ((DcMotorEx) flywheel).getVelocity();
                error = curTargetVelocity - curVelocity;
                telemetry.addData("Current Target Velocity", curTargetVelocity);
                telemetry.addData("Actual Velocity", curVelocity);
                telemetry.addData("Error", error);
                telemetry.addData("p", p);
                telemetry.addData("f", f);
                telemetry.addData("Step Size", ((Double) JavaUtil.inListGet(Step_Sizes, JavaUtil.AtMode.FROM_START, (int) (Step_Index - 1), false)).doubleValue());
                telemetry.update();
            }
        }
    }
}