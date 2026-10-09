package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.*;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.io.File;
import java.util.Arrays;

@Autonomous
public class NNFileTest extends LinearOpMode2026 {
    @Override
    public void runOpMode() throws InterruptedException {
        config();
        Network n = new Network(3,2,256,1);
        File f = new File(MiscUtils.dataFolder+"/Test.nn");
        NNFileReader.write(n,f);
        Network n2 = NNFileReader.read(f);
        waitForStart();
        telemetry.addData("OG", Arrays.toString(n.predict(new double[] {0,0,0})));
        //for(Layer l : n.getLayers()) telemetry.addData(l.toString(),"");
        telemetry.addData("Read",Arrays.toString(n2.predict(new double[] {0,0,0})));
        Neuron[][] og = new Neuron[n.getLayers().length][];
        for(int i = 0; i < og.length; i++) og[i] = n.getLayers()[i].GetNeurons();
        Neuron[][] load = new Neuron[n2.getLayers().length][];
        for(int i = 0; i < load.length; i++) load[i] = n2.getLayers()[i].GetNeurons();
        telemetry.addData("Same: ",Arrays.deepEquals(load,og));

//        for(int i = 0; i < og.length; i++) {
//            telemetry.addData("OG "+i,Arrays.toString(og[i]));
//            telemetry.addData("Load "+i,Arrays.toString(load[i]));
//        }

//        telemetry.addLine("-- TESTING --");
//        telemetry.addLine("Layers: " + n2.getLayers().length);
//        telemetry.addLine("Layer Sizes: " );
//
//        for (Layer l : n2.getLayers()){
//            telemetry.addLine("  - " + l.size);
//        }

//        telemetry.addLine("-- TESTING --");
//        telemetry.addLine("-- TESTING --");
//        telemetry.addLine("-- TESTING --");


        //for(Layer l : n2.getLayers()) telemetry.addData(l.toString(),"");
        telemetry.update();
        sleep(100000);
    }
}
