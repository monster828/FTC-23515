package org.firstinspires.ftc.teamcode.BioBuzz;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Turret_Testing")
public class Turret_Testing extends OpMode {

    DcMotorEx flywheel;
    Servo hood;
    static final int MAX_VELOCITY_PERCENT = 6000;

    // Tracked as an integer percent to avoid floating-point drift
    int velocity = 0;
    boolean lastDpadUp = false;
    boolean lastDpadDown = false;
    boolean lastDpadLeft = false;
    boolean lastDpadRight = false;

    int[] velocityIncrements = {10, 100, 500};

    int selectedIncrement;

    @Override
    public void init() {
        flywheel = hardwareMap.get(DcMotorEx.class, "Flywheel");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        boolean dpadUp = gamepad1.dpad_up;
        boolean dpadDown = gamepad1.dpad_down;
        boolean dpadLeft = gamepad1.dpad_left;
        boolean dpadRight = gamepad1.dpad_right;

        if (dpadLeft && !lastDpadLeft) {
            selectedIncrement += 1;
            if (selectedIncrement > 2)
                selectedIncrement = 2;
        }
        if (dpadRight && !lastDpadRight) {
            selectedIncrement -= 1;
            if (selectedIncrement < 0)
                selectedIncrement = 0;
        }
        lastDpadLeft = dpadLeft;
        lastDpadRight = dpadRight;

        if (dpadUp && !lastDpadUp) {
            velocity = Math.min(velocity + velocityIncrements[selectedIncrement], MAX_VELOCITY_PERCENT);
        }
        if (dpadDown && !lastDpadDown) {
            velocity = Math.max(velocity - velocityIncrements[selectedIncrement], 0);
        }
        lastDpadUp = dpadUp;
        lastDpadDown = dpadDown;


        flywheel.setVelocity(velocity * 28.0/60);

        telemetry.addData("Increment Value ", velocityIncrements[selectedIncrement]);
        telemetry.addData("Flywheel Wanted Velocity ", velocity);
        telemetry.addData("Flywheel Current Velocity ", flywheel.getVelocity() / 28.0 * 60);
        telemetry.update();
    }
}
