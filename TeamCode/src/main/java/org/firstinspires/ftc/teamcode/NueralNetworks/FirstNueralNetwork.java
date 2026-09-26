package org.firstinspires.ftc.teamcode.NueralNetworks;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.Network;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

@TeleOp
public class FirstNueralNetwork extends LinearOpMode2026 {

    public void runOpMode(){
        config();
        Network network = new Network(3, 2, 256, 1);

        double[] inputs = {0.5, 5, 2};

        long l = System.currentTimeMillis();
        double[] outputs = network.predict(inputs);
        telemetry.addData("Time",(System.currentTimeMillis()-l)+"ms");

        int predictedAction = MiscUtils.getIndexOfMax(outputs);

        telemetry.addLine("Predicted: " + predictedAction + "  Confidence: " + outputs[predictedAction]);
        telemetry.addLine();

        telemetry.addLine("Outputs");

        for (int i = 0; i < outputs.length; i++){
            telemetry.addData(String.valueOf(i), outputs[i]);
        }

        telemetry.update();

        sleep(1000);

        network.train(0.001, 100, telemetry);

        sleep(1000);

        l = System.currentTimeMillis();
        outputs = network.predict(inputs);
        telemetry.addData("Time",(System.currentTimeMillis()-l)+"ms");

        predictedAction = MiscUtils.getIndexOfMax(outputs);

        telemetry.addLine("Predicted: " + predictedAction + "  Confidence: " + outputs[predictedAction]);
        telemetry.addLine();

        telemetry.addLine("Outputs");

        for (int i = 0; i < outputs.length; i++){
            telemetry.addData(String.valueOf(i), outputs[i]);
        }

        telemetry.update();
    }
}
