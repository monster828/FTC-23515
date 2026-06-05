package org.firstinspires.ftc.teamcode.Prototype;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "intakeTest1")
public class intakeTest1 extends OpMode {

    DcMotor frontLeft, backLeft, frontRight, backRight;
    DcMotor intake;
    GoBildaPinpointDriver pinpoint;

    boolean intakeOn = false;
    double intakeSpeed = 0.5;
    static final double SPEED_STEP = 0.1;
    static final double SPEED_MIN = 0.1;
    static final double SPEED_MAX = 1.0;

    boolean wasAPressed = false;
    boolean wasDpadUpPressed = false;
    boolean wasDpadDownPressed = false;

    @Override
    public void init() {
        frontLeft  = hardwareMap.get(DcMotorEx.class, "FL");
        backLeft   = hardwareMap.get(DcMotorEx.class, "BL");
        frontRight = hardwareMap.get(DcMotorEx.class, "FR");
        backRight  = hardwareMap.get(DcMotorEx.class, "BR");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake = hardwareMap.get(DcMotor.class, "Intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "POC");
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpoint.resetPosAndIMU();
    }

    @Override
    public void loop() {
        // Toggle intake on A button press (rising edge)
        if (gamepad1.a && !wasAPressed) {
            intakeOn = !intakeOn;
        }
        wasAPressed = gamepad1.a;

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

        intake.setPower(intakeOn ? intakeSpeed : 0);

        double y  = -gamepad1.left_stick_y * 0.5;       // forward/back (Y is inverted)
        double x  =  gamepad1.left_stick_x * 0.9; // strafe (1.1 counteracts imperfect strafing)
        double rx =  gamepad1.right_stick_x * 0.6;       // rotate

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        frontLeft.setPower((y + x + rx) / denominator);
        backLeft.setPower((y - x + rx) / denominator);
        frontRight.setPower((y - x - rx) / denominator);
        backRight.setPower((y + x - rx) / denominator);

        telemetry.addData("Intake", intakeOn ? "ON" : "OFF");
        telemetry.addData("Intake Speed", String.format("%.1f", intakeSpeed));
        telemetry.update();
    }
}
