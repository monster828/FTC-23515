package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;


import static android.os.SystemClock.sleep;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class Network {
    private int _initialDimentionSize;
    private int _hiddenLayers;
    private int _hiddenLayersSize;
    private int _actionLayer;

    private ArrayList<Layer> layers;

    public Network(int initialDimentionSize, int hiddenLayers, int hiddenLayersSize, int actionLayerSize){
        _initialDimentionSize = initialDimentionSize;
        _hiddenLayers = hiddenLayers;
        _hiddenLayersSize = hiddenLayersSize;
        _actionLayer = actionLayerSize;

        layers = new ArrayList<>();
        // HIDDEN
        layers.add(new Layer(hiddenLayersSize, initialDimentionSize, Neuron.ActivationFunctionNueralNetwork.RELU));
        for (int i = 0; i < hiddenLayers - 1; i++){
            layers.add(new Layer(hiddenLayersSize, hiddenLayersSize, Neuron.ActivationFunctionNueralNetwork.RELU));
        }

        // OUTPUT
        layers.add(new Layer(actionLayerSize, hiddenLayersSize, Neuron.ActivationFunctionNueralNetwork.Linear));
    }

    public Network(Layer[] layers) {
        this.layers.clear();
        this.layers.addAll(Arrays.asList(layers));
    }

    public double[] predict(double[] inputs){
        double[] output = inputs;

        for (Layer layer : layers){
            output = layer.activate(output);
        }

        return output;
    }

    public void train(double learningRate, int episodes, Telemetry telemetry){
        double total_cost = 0;
        long total_time = 0;

        for (int i = 0; i < episodes; i++){
            long start_time = System.currentTimeMillis();
            // Currently just a input that will need to be changed to something for real
            double[] output = {0.5, 5, 2};
            for (Layer layer : layers){
                output = layer.activate(output);
            }

            double[] y_true = {1};

            double[] doubles = MiscUtils.addDoubles(output, y_true, false);
            double[] gradient = MiscUtils.mutiplyDouble(doubles, 2);
            total_cost += MiscUtils.totalAddedNumber(doubles);

            for (int j = layers.size() - 1; j > -1; j--){
                gradient = layers.get(j).backwards(gradient, learningRate);
            }

            total_time = System.currentTimeMillis() - start_time;
            telemetry.addLine("Episode: " + i + ", Adverage Cost: " + total_cost / (i + 1));
            telemetry.addLine("It took " + start_time + "m to run this episode.");
            telemetry.addLine("Estimated time remaining: " + (total_time / (i + 1)) * (episodes - i + 1));
        }
    }

    public Layer[] getLayers() {return layers.toArray(new Layer[0]);}

    public String toString() {
        return layers.toString();
    }
}
