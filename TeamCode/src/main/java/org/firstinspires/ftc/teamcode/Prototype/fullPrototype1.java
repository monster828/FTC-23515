package org.firstinspires.ftc.teamcode.Prototype;

import static java.lang.Thread.sleep;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.Utils.Timeout.Timeout;
import org.firstinspires.ftc.teamcode.Utils.Timeout.TypeOfTimeout;

@TeleOp(name = "fullPrototype1")
public class fullPrototype1 extends OpMode {

    private final ElapsedTime runtime = new ElapsedTime();

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
    boolean wasRBPressed= false;
    boolean wasLBPressed = false;

    int extend_Position = 0;
    boolean isDumping = false;

    boolean isFerrisWheel = false;

    long lastTime = System.nanoTime();

    Timeout dumpTime = new Timeout(1500, TypeOfTimeout.ContinueWhileWaiting);

    @Override
    public void init() {
        frontLeft  = hardwareMap.get(DcMotorEx.class, "FL");
        backLeft   = hardwareMap.get(DcMotorEx.class, "BL");
        frontRight = hardwareMap.get(DcMotorEx.class, "FR");
        backRight  = hardwareMap.get(DcMotorEx.class, "BR");
        slideR = hardwareMap.get(DcMotor.class, "RS");
        slideL = hardwareMap.get(DcMotor.class, "LS");

        slideL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideL.setDirection(DcMotorSimple.Direction.REVERSE);


        // ServoLT = Servo Left top, ect.
        servoLT = hardwareMap.get(Servo.class, "LT");
        servoLB = hardwareMap.get(Servo.class, "LB");
        servoRT = hardwareMap.get(Servo.class, "RT");

        extend_Position = 0;
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


        if (gamepad1.right_trigger > 0.05) {
            extend_Position += 25 * gamepad1.right_trigger;
            if (extend_Position > 2670) {
                extend_Position = 2670;
            }
            //2670 Max?
        } else if (gamepad1.left_trigger > 0.05) {
            extend_Position += -25 * gamepad1.left_trigger;
            if (extend_Position < 0) {
                extend_Position = 0;
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

            telemetry.addData("Intake", intakeOn ? "ON" : "OFF");
            telemetry.addData("Intake Reversed", intakeOnR ? "ON" : "OFF");
            telemetry.addData("Intake Speed", String.format("%.1f", intakeSpeed));
            telemetry.addData("FerrisWheel", isFerrisWheel ? "ON" : "OFF");
            telemetry.addData("Dump", isDumping ? "ON" : "OFF");
            telemetry.addData("SlideR Position", slideR.getCurrentPosition());
            telemetry.update();
        }

        DumpingAndFerrisWheel();
    }

    public void DumpingAndFerrisWheel(){
        if (Math.abs(extend_Position - slideR.getCurrentPosition()) < 90){

            if (isFerrisWheel && !isDumping) {
                servoLT.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.ThreeHundredDegrees,180));
                servoRT.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.ThreeHundredDegrees,180));
                runtime.reset();

                Timeout timeout = new Timeout(250, TypeOfTimeout.WaitUntil);
                timeout.Start();

                servoLB.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.FiveTurn, 1260));

            }

            if (isDumping){
                if (dumpTime.IsComplete()){
                    isDumping = false;
                    servoLB.setPosition(0.725); //(MiscUtils.servoConvert(MiscUtils.ServoType.FiveTurn, 1305));
                    dumpTime.Reset();
                }
            }

            if (!isFerrisWheel && !isDumping) {
                servoLT.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.ThreeHundredDegrees,20));
                servoRT.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.ThreeHundredDegrees,20));
                servoLB.setPosition(MiscUtils.servoConvert(MiscUtils.ServoType.FiveTurn, 1125));
            }

        }

        if (gamepad1.right_bumper && !wasRBPressed){
            isDumping = !isDumping;
            dumpTime.Start();
        }
        wasRBPressed = gamepad1.right_bumper;

        if (gamepad1.left_bumper && !wasLBPressed){
            isFerrisWheel = !isFerrisWheel;
        }
        wasLBPressed = gamepad1.left_bumper;
    }
}
