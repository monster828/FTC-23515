package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.util.Random;

public class Neuron {
    public ActivationFunctionNueralNetwork activationFunctionNueralNetwork;
    public double[] weights;
    public double[] inputs;
    public double bias;
    public double output;
    public double delta;
    public static Random random = new Random();

    public Neuron(int inputSize, ActivationFunctionNueralNetwork activationFunctionNueralNetwork){
        weights = new double[inputSize];
        for (int i = 0; i < weights.length; i++){
            weights[i] = random.nextGaussian() * 0.5;
        }
        this.activationFunctionNueralNetwork = activationFunctionNueralNetwork;
        bias = random.nextGaussian() * 0.5;
    }
    public Neuron(double[] weights, double bias, ActivationFunctionNueralNetwork a) {
        this.weights = weights;
        this.bias = bias;
        this.activationFunctionNueralNetwork = a;
    }

    public double activate(double[] input){
        double sum = bias;
        sum += MiscUtils.dotProduct(input, weights);

        if (activationFunctionNueralNetwork == ActivationFunctionNueralNetwork.Linear){
            output = sum;
        }else  if (activationFunctionNueralNetwork == ActivationFunctionNueralNetwork.RELU){
            output = ReLu(sum);
        }else if (activationFunctionNueralNetwork == ActivationFunctionNueralNetwork.Sigmoid){
            output = Sigmoid(sum);
        }

        this.inputs = input;

        return output;
    }

    public double[] backwards(double outputGradient, double learningRate){
        double dz;
        if (activationFunctionNueralNetwork == ActivationFunctionNueralNetwork.Linear){
            dz = outputGradient;
        }else if (activationFunctionNueralNetwork == ActivationFunctionNueralNetwork.Sigmoid){
            dz = outputGradient * SigmoidDerivative(output);
        }else{
            dz = outputGradient * ReLuDerivative(output);
        }

        double[] dw = MiscUtils.mutiplyDouble(this.inputs, dz);

        double[] inputGradient = MiscUtils.mutiplyDouble(this.weights, dz);

        this.weights = MiscUtils.addDoubles(weights, MiscUtils.mutiplyDouble(dw, learningRate), false);
        this.bias -= learningRate * dz;

        return inputGradient;
    }

    public double GetOutput(){
        return output;
    }

    public double GetDelta(){
        return delta;
    }

    public void SetDelta(double x){
        delta = x;
    }

    public double[] GetWeights(){
        return weights;
    }

    public void SetWeights(double[] x){
        weights = x;
    }

    public double GetBias(){
        return bias;
    }

    public void SetBias(double x){
        bias = x;
    }

    public double Sigmoid(double x){
        return 1/ (1 + Math.pow(Math.E, MiscUtils.Clamp(-x, -500, 500)));
    }

    public double SigmoidDerivative(double x){
        double sig = Sigmoid(x);
        return sig * (1-sig);
    }

    public double ReLu(double x){
        return Math.max(0, x);
    }

    public double ReLuDerivative(double x){
        return x > 0 ? 1 : 0;
    }

    public enum ActivationFunctionNueralNetwork{
        Linear,
        RELU,
        Sigmoid
    }
}
