package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.NNFileReader;
import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.Network;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.io.File;
import java.util.Arrays;

@Autonomous
public class NNFileTest extends LinearOpMode2026 {
    @Override
    public void runOpMode() throws InterruptedException {
        Network n = new Network(3,2,256,1);
        NNFileReader.write(n,new File(MiscUtils.dataFolder+"/Test.nn"));
        Network n2 = NNFileReader.read(new File(MiscUtils.dataFolder+"/Test.nn"));
        telemetry.addData("OG", Arrays.toString(n.predict(new double[] {0,0,0})));
        telemetry.addData("Read",Arrays.toString(n2.predict(new double[] {0,0,0})));
        telemetry.update();
        sleep(10000);
    }
}
