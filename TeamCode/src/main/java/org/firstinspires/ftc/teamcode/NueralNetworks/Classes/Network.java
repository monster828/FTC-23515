package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.util.ArrayList;

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
        layers.add(new Layer(initialDimentionSize, hiddenLayersSize, Neuron.ActivationFunctionNueralNetwork.RELU));
        for (int i = 0; i < hiddenLayers - 1; i++){
            layers.add(new Layer(hiddenLayersSize, hiddenLayersSize, Neuron.ActivationFunctionNueralNetwork.RELU));
        }

        // OUTPUT
        layers.add(new Layer(hiddenLayersSize, actionLayerSize, Neuron.ActivationFunctionNueralNetwork.Linear));
    }

    public double[] predict(double[] inputs){
        double[] output = inputs;

        for (Layer layer : layers){
            output = layer.activate(output);
        }

        return output;
    }

    public static void main(String[] args) {
        Network network = new Network(3, 2, 20, 10);

        double[] inputs = {0.5, 5, 2};

        double[] outputs = network.predict(inputs);

        System.out.println(outputs.toString());

        int predictedAction = MiscUtils.getIndexOfMax(outputs);

        System.out.println("Predicted: " + predictedAction + "  Confidence: " + outputs[predictedAction]);
    }

}
