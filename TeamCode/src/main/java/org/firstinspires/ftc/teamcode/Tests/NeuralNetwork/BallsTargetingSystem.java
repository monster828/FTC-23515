package org.firstinspires.ftc.teamcode.Tests.NeuralNetwork;

import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.NNFileReader;
import org.firstinspires.ftc.teamcode.NueralNetworks.Classes.Network;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.nio.charset.StandardCharsets;

public class BallsTargetingSystem {

    static String filename = "MySpecialFile_BallTargetSystem";

    static Network network;

    static int maxBalls = 10;
    static int featuresPerBall = 4;
    // X
    // Y
    // Type (0,1)
    // Is Used (0,1)

    static int predictedBallCount;

    public static void main(String[] args) {
        NNFileReader.Aldenify();
        train();

        Ball[] simulatedInputs = {new Ball(0,2, false), new Ball(3,4,true), new Ball(2,9,true), new Ball(9,11, false)};// generateRandomBallStates();

        double[] inputs = getInputs(simulatedInputs);
        predict(inputs);
    }

//    public void runOpMode(){
//
//    }

    public static void predict(double[] inputs){
        double[] outputs = network.predict(inputs);

        int predictedBall = MiscUtils.getIndexOfMax(outputs);

        String predicted = "Predicted ball: " + predictedBall + "\n Ouputs: " + outputs.toString();

        MiscUtils.writeFile(NNFileReader.aldensPath, predicted.getBytes(StandardCharsets.UTF_8));

        // telemetry.addLine("PREDICTED BALL: " + predictedBall);
    }

    public static double[] getInputs(Ball[] balls){
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

    public static void Save(String fileName){
        NNFileReader.writeC(network, fileName);
    }

    public static void Load(String fileName){
        network = NNFileReader.readC(fileName);
    }

    public static void train(){
        Load(filename);

        if (network == null){
            int inputSize = maxBalls * featuresPerBall; // 40

            int hiddenLayers = 2;
            int hiddenLayersSize = 52;
            int actionLayerSize = maxBalls;

            network = new Network(inputSize, hiddenLayers, hiddenLayersSize, actionLayerSize);
        }


        Ball[] simulatedInputs = {new Ball(0,2, false), new Ball(3,4,true), new Ball(2,9,true), new Ball(9,11, false)};// generateRandomBallStates();

        double[] inputs = getInputs(simulatedInputs);

        double[] y_true = {0.9, 0.09, 0, 0.01, 0,0,0,0,0,0};//calculateOptimalScoresFromHeuristic(simulatedInputs); // e.g., 10 target scores

        network.train(0.001, 500, null);

        Save(filename);
    }

//    public Ball[] generateRandomBallStates(){
//        Ball[] balls =
//    }



    public static class Ball{
        public float x, y;
        public int type; // Pollen 25   Nector 41

        public Ball(float x, float y, boolean isPollen){
            this.x = x;
            this.y = y;
            this.type = isPollen ? 25 : 41;
        }
    }
}
