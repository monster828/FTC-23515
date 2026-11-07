package org.firstinspires.ftc.teamcode.BioBuzz;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Turret_Testing")
public class Turret_Testing extends OpMode {

    DcMotorEx flywheel, lift, intake;
    Servo hood;
    Servo lla;
    static final double INTAKE_POWER = 0.8;
    static final double LIFT_POWER = 1.0;
    static final int MAX_RPM = 6000;
    // goBILDA 6000 RPM (1:1) motor encoder
    static final double TICKS_PER_REV = 28;

    int velocity = 0;
    double hoodAngle = 0.1;
    double cameraAngle = 0.5;
    boolean lastDpadUp = false;
    boolean lastDpadDown = false;
    boolean lastDpadLeft = false;
    boolean lastDpadRight = false;
    boolean wasCrossPressed = true;
    boolean intakeOn = false;
    boolean wasRightBumperPressed = false;

    int[] velocityIncrements = {10, 100, 500};
    double[] servoIncrements = {0.01, 0.05, 0.1};
    int selectedTune = 0;
    int selectedIncrement = 0;

    @Override
    public void init() {
        flywheel = hardwareMap.get(DcMotorEx.class, "FW");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hood = hardwareMap.get(Servo.class, "Hood");
        lla = hardwareMap.get(Servo.class, "LLA");
        lift = hardwareMap.get(DcMotorEx.class, "lift");
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        intake = hardwareMap.get(DcMotorEx.class, "intake");
    }

    @Override
    public void loop() {
        boolean dpadUp = gamepad1.dpad_up;
        boolean dpadDown = gamepad1.dpad_down;
        boolean dpadLeft = gamepad1.dpad_left;
        boolean dpadRight = gamepad1.dpad_right;

        //Changes what is tuned
        if (gamepad1.cross && !wasCrossPressed) {
            selectedTune += 1;
            if (selectedTune > 2)
                selectedTune = 0;
        }
        wasCrossPressed = gamepad1.cross;

        //Intake toggles on right bumper press
        if (gamepad1.right_bumper && !wasRightBumperPressed) {
            intakeOn = !intakeOn;
        }
        wasRightBumperPressed = gamepad1.right_bumper;

        //Changes tuning increment
        if (dpadRight && !lastDpadRight) {
            selectedIncrement += 1;
            if (selectedIncrement > 2)
                selectedIncrement = 0;
        }
        if (dpadLeft && !lastDpadLeft) {
            selectedIncrement -= 1;
            if (selectedIncrement < 0)
                selectedIncrement = 2;
        }
        lastDpadLeft = dpadLeft;
        lastDpadRight = dpadRight;

        //Tuning Math
        if (selectedTune == 0) { //Flywheel
        if (dpadUp && !lastDpadUp) {
            velocity = Math.min(velocity + velocityIncrements[selectedIncrement], MAX_RPM);
        }
        if (dpadDown && !lastDpadDown) {
            velocity = Math.max(velocity - velocityIncrements[selectedIncrement], 0);
        }
        lastDpadUp = dpadUp;
        lastDpadDown = dpadDown;

        telemetry.addLine("Flywheel Tuning");
        telemetry.addData("Increment Value ", velocityIncrements[selectedIncrement]);

        } else if (selectedTune == 1) { // Hood
            if (dpadUp && !lastDpadUp) {
                hoodAngle = Math.min(hoodAngle + servoIncrements[selectedIncrement], 0.37);
            }
            if (dpadDown && !lastDpadDown) {
                hoodAngle = Math.max(hoodAngle - servoIncrements[selectedIncrement], 0.05);
            }
            lastDpadUp = dpadUp;
            lastDpadDown = dpadDown;
            telemetry.addLine("Hood Tuning");
            telemetry.addData("Increment Value ", servoIncrements[selectedIncrement]);

        } else { //Camera
            if (dpadUp && !lastDpadUp) {
                cameraAngle = Math.min(cameraAngle + servoIncrements[selectedIncrement], 0.69);
            }
            if (dpadDown && !lastDpadDown) {
                cameraAngle = Math.max(cameraAngle - servoIncrements[selectedIncrement], 0.43);
            }
            lastDpadUp = dpadUp;
            lastDpadDown = dpadDown;

            telemetry.addLine("Camera Tuning");
            telemetry.addData("Increment Value ", servoIncrements[selectedIncrement]);
        }


        // velocity is in RPM; setVelocity takes ticks per second
        flywheel.setVelocity(velocity * TICKS_PER_REV / 60.0);
        hood.setPosition(hoodAngle);
        lla.setPosition(cameraAngle);
        //Intake on toggle; loop is earlier
        intake.setPower(intakeOn ? INTAKE_POWER : 0);
        //Lift only runs while left bumper is held
        lift.setPower(gamepad1.left_bumper ? LIFT_POWER : 0);

        showTelemetry();
    }
    void showTelemetry() {
        telemetry.addData("Flywheel Wanted RPM ", velocity);
        telemetry.addData("Flywheel Current RPM ", flywheel.getVelocity() * 60.0 / TICKS_PER_REV);
        telemetry.addData("Hood Angle", "%.2f", hoodAngle);
        telemetry.addData("Camera Angle","%.2f", cameraAngle);
        telemetry.addData("Intake", intakeOn ? "ON" : "OFF");
        telemetry.addData("Lift", gamepad1.left_bumper ? "ON" : "OFF");
        telemetry.update();
    }
}
