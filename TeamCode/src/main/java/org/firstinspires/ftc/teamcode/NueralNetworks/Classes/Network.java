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

    public Layer[] getLayers() {return layers.toArray(new Layer[0]);}
}
