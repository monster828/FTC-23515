package org.firstinspires.ftc.teamcode.NueralNetworks;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.Network;
import org.firstinspires.ftc.teamcode.Utils.LinearOpMode2026;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

@TeleOp
public class FirstNueralNetwork extends LinearOpMode2026 {

    public void runOpMode(){
        config();
        Network network = new Network(3, 2, 20, 10);

        double[] inputs = {0.5, 5, 2};

        double[] outputs = network.predict(inputs);

        int predictedAction = MiscUtils.getIndexOfMax(outputs);

        telemetry.addLine("Predicted: " + predictedAction + "  Confidence: " + outputs[predictedAction]);
        telemetry.addLine();

        telemetry.addLine("Outputs");

        for (int i = 0; i < outputs.length; i++){
            telemetry.addData(String.valueOf(i), outputs[i]);
        }

        telemetry.update();

        sleep(100000);
    }

    void Train(){

    }

    void Predict(){

    }
}
