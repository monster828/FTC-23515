package org.firstinspires.ftc.teamcode.DriveTests_2026;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Utils.Movement.PosGetters.PinpointPosGet;

@Autonomous
public class PinpointTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        GoBildaPinpointDriver pin = hardwareMap.get(GoBildaPinpointDriver.class,"POC");
        PinpointPosGet posGet = new PinpointPosGet(pin);
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("Position",posGet.getPosi().toString());
        }
    }
}
