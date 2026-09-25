package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.util.Random;

public class Layer {

    private double[][] weights;
    private double[][] biases;
    private ActivationFunctionNueralNetwork _activationFunctionNueralNetwork;


    public Layer(int inputSize, int outputSize, ActivationFunctionNueralNetwork activationFunctionNueralNetwork){
        _activationFunctionNueralNetwork = activationFunctionNueralNetwork;

        Random random = new Random();
        this.weights = new double[outputSize][inputSize];
        double scale = Math.sqrt(2.0 / inputSize);

        for (int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[i].length; j++){
                this.weights[i][j] = random.nextGaussian() * scale;
            }
        }

        this.biases = new double[outputSize][1];
    }

    public float ReLu(float x){
        return Math.max(0, x);
    }

    public float ReLuDerivative(float x){
        return x > 0 ? 1 : 0;
    }

    public int Forward(double[] inputs){
        MiscUtils.dotProduct();
    }

    public void Backward(){

    }

    public enum ActivationFunctionNueralNetwork{
        RELU,
        Sigmoid
    }
}