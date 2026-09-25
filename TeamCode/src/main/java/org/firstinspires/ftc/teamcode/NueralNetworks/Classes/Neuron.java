package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import java.util.Random;

public class Neuron {
    public double[] weights;
    public double bias;
    public double output;
    public double delta;
    public static Random random = new Random();

    public Neuron(int inputSize){
        weights = new double[inputSize];
        for (int i = 0; i < weights.length; i++){
            weights[i] = random.nextGaussian() * 0.5;
        }

        bias = random.nextGaussian() * 0.5;
    }

    public double activate(double[] input){

    }
}
