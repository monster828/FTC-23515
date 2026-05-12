package org.firstinspires.ftc.teamcode.summerDriveTests;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Utils.Movement.DriveUtils;

@TeleOp
public class MoveRotTest extends LinearOpMode {

    public DcMotor[] mot = {
            hardwareMap.get(DcMotorEx.class,"BL"), //back left
            hardwareMap.get(DcMotorEx.class,"BR"), //back right
            hardwareMap.get(DcMotorEx.class,"FL"), //front left
            hardwareMap.get(DcMotorEx.class,"FR") //front right
    };
    public SparkFunOTOS spark = hardwareMap.get(SparkFunOTOS.class,"spark");

    @Override
    public void runOpMode() {
        for(int a = 0; a <= 90; a += 10) {
            while(!gamepad1.a);
            DriveUtils.DriveThing((float) Math.cos(Math.toRadians(a)), (float) Math.sin(Math.toRadians(a)),0,1,mot);
            long s = System.currentTimeMillis();
            double accel = 0;
            int i = 0;
            while(System.currentTimeMillis()-s < 100) {
                accel += Math.sqrt(Math.pow(spark.getAcceleration().x, 2) + Math.pow(spark.getAcceleration().y, 2));
                i += 1;
                sleep(2);
            }
            accel /= i;
            double v = Math.sqrt(Math.pow(spark.getVelocity().x,2)+Math.pow(spark.getVelocity().y,2));
            DriveUtils.stop(mot);
            telemetry.addData("Angle",a);
            telemetry.addData("Velocity",v);
            telemetry.addData("Acceleration",accel);
            telemetry.update();
            sleep(200);
        }

    }
}
