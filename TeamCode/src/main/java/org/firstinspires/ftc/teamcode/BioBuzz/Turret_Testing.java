package org.firstinspires.ftc.teamcode.BioBuzz;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Turret_Testing")
public class Turret_Testing extends OpMode {

    DcMotor flywheel;

    static final int MAX_POWER_PERCENT = 100;
    static final int STEP_PERCENT = 5;

    // Tracked as an integer percent to avoid floating-point drift
    int powerPercent = 0;
    boolean lastDpadUp = false;
    boolean lastDpadDown = false;

    @Override
    public void init() {
        flywheel = hardwareMap.get(DcMotor.class, "Flywheel");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        boolean dpadUp = gamepad1.dpad_up;
        boolean dpadDown = gamepad1.dpad_down;

        if (dpadUp && !lastDpadUp) {
            powerPercent = Math.min(powerPercent + STEP_PERCENT, MAX_POWER_PERCENT);
        }
        if (dpadDown && !lastDpadDown) {
            powerPercent = Math.max(powerPercent - STEP_PERCENT, 0);
        }
        lastDpadUp = dpadUp;
        lastDpadDown = dpadDown;

        double power = powerPercent / 100.0;
        flywheel.setPower(power);

        telemetry.addData("Controls", "Dpad Up/Down: +/-%d%%", STEP_PERCENT);
        telemetry.addData("Flywheel Power", "%d%%", powerPercent);
        telemetry.update();
    }
}
