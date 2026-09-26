package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.util.Random;

public class Layer {
    public Neuron[] neurons;
    public int size;

    public Layer(int nueronCount, int inputSize, Neuron.ActivationFunctionNueralNetwork activationFunctionNueralNetwork){
        this.size = inputSize;

        this.neurons = new Neuron[nueronCount];

        for (int i = 0; i<nueronCount; i++){
            this.neurons[i] = new Neuron(inputSize, activationFunctionNueralNetwork);
        }
    }

    public Layer(int inputSize, Neuron[] neurons){
        this.size = inputSize;
        this.neurons = neurons;
    }

    public double[] activate(double[] inputs){
        double[] outputs = new double[neurons.length];

        for (int i = 0; i < outputs.length; i++){
            outputs[i] = neurons[i].activate(inputs);
        }

        return outputs;
    }

    public Neuron[] GetNeurons(){
        return  neurons;
    }

    public int GetSize(){
        return size;
    }
}