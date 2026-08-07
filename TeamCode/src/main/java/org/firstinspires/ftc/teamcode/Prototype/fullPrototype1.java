package org.firstinspires.ftc.teamcode.Prototype;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

@TeleOp(name = "fullPrototype1")
public class fullPrototype1 extends OpMode {

    DcMotor frontLeft, backLeft, frontRight, backRight;
    DcMotor intake, slideR, slideL;
    GoBildaPinpointDriver pinpoint;

    Servo servoRT, servoRB, servoLT, servoLB;

    boolean intakeOn = false;
    boolean intakeOnR = false;
    double intakeSpeed = 0.4;
    static final double SPEED_STEP = 0.1;
    static final double SPEED_MIN = 0.1;
    static final double SPEED_MAX = 1.0;

    boolean wasAPressed = false;
    boolean wasBPressed = false;
    boolean wasDpadUpPressed = false;
    boolean wasDpadDownPressed = false;

    int extend_Position = 0;
    boolean isDumping = false;

    boolean isFerrisWheel = false;

    float dumpingTime = 0;

    long lastTime = System.nanoTime();

    @Override
    public void init() {
        frontLeft  = hardwareMap.get(DcMotorEx.class, "FL");
        backLeft   = hardwareMap.get(DcMotorEx.class, "BL");
        frontRight = hardwareMap.get(DcMotorEx.class, "FR");
        backRight  = hardwareMap.get(DcMotorEx.class, "BR");
        slideR = hardwareMap.get(DcMotor.class, "RS");
        slideL = hardwareMap.get(DcMotor.class, "LS");

        // ServoLT = Servo Left top, ect.
        servoLT = hardwareMap.get(Servo.class, "LT");
        servoLB = hardwareMap.get(Servo.class, "LB");
        servoRT = hardwareMap.get(Servo.class, "RT");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        servoLT.setDirection(Servo.Direction.REVERSE);
        //Servo LT Reversed
        //Servo RT Normal
        //Servo LB Reversed, 900 is dumping

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hardwareMap.get(DcMotor.class, "Intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "POC");
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpoint.resetPosAndIMU();
    }

    @Override
    public void loop() {
        long currentTime = System.nanoTime();

        // Calculate elapsed nanoseconds, then convert to seconds
        double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;

        // Update lastTime for the next frame iteration
        lastTime = currentTime;

        if (gamepad1.a && !wasAPressed) {
            intakeOn = !intakeOn;
            if (intakeOn) intakeOnR = false;
        }
        wasAPressed = gamepad1.a;

        if (gamepad1.b && !wasBPressed) {
            intakeOnR = !intakeOnR;
            if (intakeOnR) intakeOn = false;
        }
        wasBPressed = gamepad1.b;

        // Increase speed on dpad_up (rising edge)
        if (gamepad1.dpad_up && !wasDpadUpPressed) {
            intakeSpeed = Math.min(intakeSpeed + SPEED_STEP, SPEED_MAX);
        }
        wasDpadUpPressed = gamepad1.dpad_up;

        // Decrease speed on dpad_down (rising edge)
        if (gamepad1.dpad_down && !wasDpadDownPressed) {
            intakeSpeed = Math.max(intakeSpeed - SPEED_STEP, SPEED_MIN);
        }
        wasDpadDownPressed = gamepad1.dpad_down;

        if (intakeOn) {
            intake.setPower(intakeSpeed);
        } else if (intakeOnR) {
            intake.setPower(-0.4);
        } else {
            intake.setPower(0);
        }

        double y  = -gamepad1.left_stick_y * 0.5;       // forward/back (Y is inverted)
        double x  =  gamepad1.left_stick_x * 0.9; // strafe
        double rx =  gamepad1.right_stick_x * 0.6;       // rotate

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        frontLeft.setPower((y + x + rx) / denominator);
        backLeft.setPower((y - x + rx) / denominator);
        frontRight.setPower((y - x - rx) / denominator);
        backRight.setPower((y + x - rx) / denominator);

        //2670 ticks for full extension
        // VERY EASY TO MAKE AUTOMATED

        // ==> Button clicked <==
        // => Rotate servos while extending <=
        // => When reached top bucket dump <=
        // => Button click to dump <=
        // => Button click to retract <=
        // => After dumping rotate bucket out of the way (vertical) and then pull down the slides <=
        // => Reset servos while pulling down <=


        if (gamepad1.right_trigger > 0) {
            extend_Position += 300 * gamepad1.right_trigger;
            if (extend_Position < 20) {
                extend_Position = 20;
            }
        } else if (gamepad1.left_trigger > 0) {
            extend_Position += -300 * gamepad1.left_trigger;
            if (extend_Position > 2670) {
                extend_Position = 2670;
            }
        }

        if (Math.abs(extend_Position - slideR.getCurrentPosition()) < -1) {
            slideL.setPower(0);
            slideR.setPower(0);
        } else {
            if (Math.abs(extend_Position - slideR.getCurrentPosition()) < 90) {
                slideL.setPower(0.001 * (extend_Position - slideL.getCurrentPosition()));
                slideR.setPower(0.001 * (extend_Position - slideR.getCurrentPosition()));
            } else {
                slideL.setPower(0.0025 * (extend_Position - slideL.getCurrentPosition()));
                slideR.setPower(0.0025 * (extend_Position - slideR.getCurrentPosition()));
            }

            if (gamepad1.xWasPressed()) {
                servoLT.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.ThreeHundredDegrees,0));
                servoRT.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.ThreeHundredDegrees,0));
                servoLB.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.FiveTurn, 0));
            }

            telemetry.addData("Intake", intakeOn ? "ON" : "OFF");
            telemetry.addData("Intake Reversed", intakeOnR ? "ON" : "OFF");
            telemetry.addData("Intake Speed", String.format("%.1f", intakeSpeed));
            telemetry.update();
        }

        DumpingAndFerrisWheel(deltaTime);
    }

    public void DumpingAndFerrisWheel(double deltaTime){
        if (Math.abs(extend_Position - slideR.getCurrentPosition()) < 90){

            if (isFerrisWheel) {
                servoLT.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.ThreeHundredDegrees,180));
                servoRT.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.ThreeHundredDegrees,180));
                servoLB.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.FiveTurn, 180));
            }

            if (isDumping){
                if (dumpingTime > 0){
                    dumpingTime -= deltaTime;

                    if (dumpingTime <= 0){
                        isDumping = false;
                    }
                }else{
                    servoLB.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.FiveTurn, 45));

                    dumpingTime = 1.5f;
                }
            }
        }

        if (gamepad1.rightBumperWasPressed()){
            isDumping = true;
        }
        if (gamepad1.leftBumperWasPressed()){
            isFerrisWheel = true;
        }
    }
}
