package org.firstinspires.ftc.teamcode.Tests.NeuralNetwork;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.NNFileReader;
import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.Network;

public class BallsTargetingSystem {
    Network network;

    int maxBalls = 10;
    int featuresPerBall = 4;
    // X
    // Y
    // Type (0,1)
    // Is Used (0,1)

    int predictedBallCount;

    public void runOpMode(){
        int inputSize = maxBalls * featuresPerBall; // 40

        int hiddenLayers = 2;
        int hiddenLayersSize = 52;
        int actionLayerSize = maxBalls;

        network = new Network(inputSize, hiddenLayers, hiddenLayersSize, actionLayerSize);
    }

    public void predict(double[] inputs){
        double[] outpus = network.predict(inputs);
    }

    public double[] getInputs(Ball[] balls){
        double[] inputs = new double[maxBalls * featuresPerBall];

        predictedBallCount = balls.length;

        for (int i = 0; i < inputs.length; i += featuresPerBall){
            if (i < balls.length){
                int index = i / featuresPerBall;
                inputs[i] = balls[index].x;
                inputs[i + 1] = balls[index].y;
                inputs[i + 2] = balls[index].type;
                inputs[i + 3] = 1;
            }else {
                inputs[i + 3] = 0;
            }
        }

        return inputs;
    }

    public void Save(String fileName){
        NNFileReader.writeC(network, fileName);
    }

    public void Load(String fileName){
        network = NNFileReader.readC(fileName);
    }

    public void train(){
//        double[] simulatedInputs = generateRandomBallStates(); // e.g., 16 inputs
//        double[] y_true = calculateOptimalScoresFromHeuristic(simulatedInputs); // e.g., 4 target scores
//
//        // Then feed these into your existing gradient calculation logic
//        double[] output = predict(simulatedInputs);
//        double[] errors = MiscUtils.addDoubles(output, y_true, false);
//        double[] gradient = MiscUtils.mutiplyDouble(errors, 2);
//
//        for (int j = layers.size() - 1; j > -1; j--){
//            gradient = layers.get(j).backwards(gradient, learningRate);
//        }
    }

    public class Ball{
        public float x;
        public float y;
        public int type;
    }
}
