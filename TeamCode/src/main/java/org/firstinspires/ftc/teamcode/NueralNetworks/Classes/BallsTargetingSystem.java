package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;

//TO DO
//        - WHEN GETTING CONTINUES PATH, UPDATE BALL POSITIONS

public class BallsTargetingSystem {
    static String filename = "MySpecialFile_BallTargetSystem";

    static Network network;

    static int maxBalls = 10;
    static int featuresPerBall = 4;
    // X
    // Y
    // Type (0,1)
    // Is Used (0,1)

//    static int predictedBallCount;
//
//    public static void main(String[] args) {
//        NNFileReader.Aldenify();
//
//        Load(filename);
//        if (network == null){
//            int inputSize = maxBalls * featuresPerBall; // 40
//
//            int hiddenLayers = 2;
//            int hiddenLayersSize = 520;
//            int actionLayerSize = maxBalls;
//
//            network = new Network(inputSize, hiddenLayers, hiddenLayersSize, actionLayerSize);
//        }
//
//        // runTestNetwork();
//        runGoToBallTargetingSystem();
//    }

    public static void setup(){
        Load(filename);
        if (network == null){
            int inputSize = maxBalls * featuresPerBall; // 40

            int hiddenLayers = 2;
            int hiddenLayersSize = 520;
            int actionLayerSize = maxBalls;

            network = new Network(inputSize, hiddenLayers, hiddenLayersSize, actionLayerSize);
        }
    }

    public static void runTestNetwork(){
        train();

        int predictions = 100;
        StringBuilder predictionsSave = new StringBuilder("PREDICTIONS \n");

        double accuracy = 0;
        for (int i = 0; i < predictions; i++) {
            Ball[] simulatedInputs = generateRandomBallStates();

            predictionsSave.append("Balls: ");
            for (Ball ball : simulatedInputs){
                predictionsSave.append("(").append(ball.x).append(",").append(ball.y).append(",").append(ball.type).append(") ");
            }

            predictionsSave.append("\n");

            double[] outputs = network.predict(getInputs(simulatedInputs));

            predictionsSave.append(Arrays.toString(outputs));
            // System.out.println(predictionsSave);
            predictionsSave.append("\n");

            // Update accuracy
            int predictedBall = MiscUtils.getIndexOfMax(network.predict(getInputs(simulatedInputs)));

            int assumedBall = MiscUtils.getIndexOfMax(calculateOptimalScoresFromHeuristic(simulatedInputs));
            accuracy += outputs[assumedBall];

            predictionsSave.append("Predicted ball: ").append(predictedBall).append("\n");
            predictionsSave.append("Assumed ball: ").append(assumedBall).append("\n");
        }

        accuracy /= predictions;
        // System.out.println("Accuracy: " + accuracy);


        predictionsSave.append("\nAccuracy: ").append(accuracy * 100).append("%\n");


        MiscUtils.writeFile(NNFileReader.aldensPath + "\\output.txt", predictionsSave.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static void runGoToBallTargetingSystem(){
        int predictions = 100;
        StringBuilder predictionsSave = new StringBuilder("PREDICTIONS \n");

        double accuracy = 0;
        int totalPredictions = 0;
        for (int i = 0; i < predictions; i++) {
            Ball[] simulatedInputsArray = generateRandomBallStates();
            ArrayList<Ball> simulatedInputs = new ArrayList<>(Arrays.asList(simulatedInputsArray));

            predictionsSave.append("\n");
            predictionsSave.append("\n");

            predictionsSave.append("Balls: \n");
            for (int j =0; j< simulatedInputsArray.length; j++){
                Ball ball = simulatedInputsArray[j];
                predictionsSave.append(j).append(". (").append(ball.x).append(",").append(ball.y).append(",").append(ball.type).append(") \n");
            }

            predictionsSave.append("\n");
            predictionsSave.append("OrderOfCollection: ");

            while (!simulatedInputs.isEmpty()){
                Ball[] currentSimulatedInputs = simulatedInputs.toArray(new Ball[0]);

                double[] outputs = network.predict(getInputs(currentSimulatedInputs));

                // Update accuracy
                int predictedBall = MiscUtils.getIndexOfMax(outputs);

                int assumedBall = MiscUtils.getIndexOfMax(calculateOptimalScoresFromHeuristic(currentSimulatedInputs));


                totalPredictions++;

                if (predictedBall >= simulatedInputs.size()) {
                    predictionsSave.append("PREDICTED NON-EXISTENT BALL");
                    break;
                }else{
                    predictionsSave.append(MiscUtils.findIndexOf(simulatedInputsArray, simulatedInputs.get(predictedBall))).append(", ");
                    simulatedInputs.remove(predictedBall);
                    accuracy += outputs[assumedBall];
                }
            }
        }

        accuracy /= totalPredictions;
        System.out.println("Accuracy: " + accuracy);


        predictionsSave.append("\nAccuracy: ").append(accuracy * 100).append("%\n");


        MiscUtils.writeFile(NNFileReader.aldensPath + "\\output.txt", predictionsSave.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static ArrayList<Ball> orderBallsByPickup(Ball[] balls){
        ArrayList<Ball> simulatedInputs = new ArrayList<>(Arrays.asList(balls));
        ArrayList<Ball> orderOfCollection = new ArrayList<>();

        while (!simulatedInputs.isEmpty()){
            Ball[] currentSimulatedInputs = simulatedInputs.toArray(new Ball[0]);

            double[] outputs = network.predict(getInputs(currentSimulatedInputs));

            // Update accuracy
            int predictedBall = MiscUtils.getIndexOfMax(outputs);

            if (predictedBall >= simulatedInputs.size()) {
                break;
            }else{

                orderOfCollection.add(simulatedInputs.remove(predictedBall));
            }
        }
        return orderOfCollection;
    }

    public static String predict(double[] inputs){
        double[] outputs = network.predict(inputs);

        int predictedBall = MiscUtils.getIndexOfMax(outputs);

        return "Predicted ball: " + predictedBall + "\n Ouputs: " + Arrays.toString(outputs);
        // telemetry.addLine("PREDICTED BALL: " + predictedBall);
    }

    public static double[] getInputs(Ball[] balls){
        orderBallsByDistance(balls);
        double[] inputs = new double[maxBalls * featuresPerBall];

//        predictedBallCount = balls.length;

        for (int i = 0; i < inputs.length; i += featuresPerBall){
            int index = i / featuresPerBall;
            if (index < balls.length){
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

        int trainingIterations = 5000;
        for (int epoch = 0; epoch < trainingIterations; epoch++){
            Ball[] simulatedInputs = generateRandomBallStates();

            double[] inputs = getInputs(simulatedInputs);

            double[] y_true = calculateOptimalScoresFromHeuristic(simulatedInputs); // e.g., 10 target scores

            network.train(0.0001, 1, y_true, inputs);

            if (epoch % 1000 == 0) {
                System.out.println("Training progress: Epoch " + epoch + "/" + trainingIterations);
            }
        }

        Save(filename);
    }

    public static class Ball{
        public float x, y;
        public int type; // Pollen 25   Nector 41

        public Ball(float x, float y, boolean isPollen){
            this.x = x;
            this.y = y;
            this.type = isPollen ? 25 : 41;
        }

        /**
         * Convert position to field coordinates
         * @param rX robot X
         * @param rY robot Y
         * @param rotation rotation of the robot in degrees, North-Clockwise convention.
         * @return float[0] = field X pos, float[1] = field Y pos.
         */
        public float[] toFieldCoords(float rX, float rY, float rotation) {
            float[] out = new float[2];
            out[0] = (float) (rX+(Math.cos(rotation)*x)+(Math.sin(rotation)*y));
            out[1] = (float) (rX-(Math.sin(rotation)*x)+(Math.cos(rotation)*y));
            return out;
        }
    }

    public static void orderBallsByDistance(Ball[] balls) {
        java.util.Arrays.sort(balls, (b1, b2) -> {
            double dist1 = Math.sqrt(Math.pow(b1.x, 2) + Math.pow(b1.y, 2));
            double dist2 = Math.sqrt(Math.pow(b2.x, 2) + Math.pow(b2.y, 2));
            return Double.compare(dist1, dist2);
        });
    }

    // THIS IS AI GENERATED JUST FOR QUICKLY GETTING TRAINING
    public static double[] calculateOptimalScoresFromHeuristic(Ball[] balls) {
        double[] scores = new double[maxBalls];
        if (balls == null || balls.length == 0) {
            return scores;
        }

        // Assuming robot position is at origin (0, 0)
        float robotX = 0;
        float robotY = 0;

        double[] rawScores = new double[balls.length];
        double totalScore = 0;

        for (int i = 0; i < balls.length; i++) {
            Ball b = balls[i];

            // 1. Type Priority: Nectar (41) is prioritized over Pollen (25)
            // Giving Nectar a higher weight multiplier
            double typeWeight = (b.type == 41) ? 2.5 : 1.0;

            // 2. Distance Priority: Closer to the robot is better
            double distance = Math.sqrt(Math.pow(b.x - robotX, 2) + Math.pow(b.y - robotY, 2));
            double distanceScore = 1.0 / (1.0 + distance); // Inverse distance (closer = higher score)

            // 3. Group / Density Priority: Count nearby balls within a radius (e.g., 4.0 units)
            int neighborCount = 0;
            double groupRadius = 4.0;
            for (int j = 0; j < balls.length; j++) {
                if (i == j) continue;
                double distToOther = Math.sqrt(Math.pow(b.x - balls[j].x, 2) + Math.pow(b.y - balls[j].y, 2));
                if (distToOther <= groupRadius) {
                    neighborCount++;
                }
            }
            double groupScore = 1.0 + (neighborCount * 0.4); // Bonus multiplier for clustered balls

            // Combine factors into a raw score for this ball
            rawScores[i] = typeWeight * distanceScore * groupScore;
            totalScore += rawScores[i];
        }

        // Normalize raw scores into a probability distribution (summing to ~1.0)
        for (int i = 0; i < balls.length; i++) {
            if (totalScore > 0) {
                scores[i] = rawScores[i] / totalScore;
            } else {
                scores[i] = 1.0 / balls.length;
            }
        }

        return scores;
    }

    public static Ball[] generateRandomBallStates() {
        java.util.Random rand = new java.util.Random();

        // Randomly decide how many balls are visible (between 1 and maxBalls)
        int count = rand.nextInt(maxBalls) + 1;
        Ball[] balls = new Ball[count];

        // Frequently create a cluster center to test the "groups of balls" priority
        boolean createCluster = rand.nextBoolean();
        float clusterX = rand.nextFloat() * 10.0f;
        float clusterY = rand.nextFloat() * 10.0f;

        for (int i = 0; i < count; i++) {
            float x, y;

            // If clustering is enabled, place the first few balls tightly together
            if (createCluster && i < 3 && count >= 3) {
                x = clusterX + (rand.nextFloat() * 2.0f - 1.0f); // within 1 unit
                y = clusterY + (rand.nextFloat() * 2.0f - 1.0f);
            } else {
                // Otherwise, scatter them randomly across the field (0 to 12 units)
                x = rand.nextFloat() * 12.0f;
                y = rand.nextFloat() * 12.0f;
            }

            // Type: 50% chance of being Pollen (25) or Nectar (41)
            boolean isPollen = rand.nextBoolean();

            balls[i] = new Ball(x, y, isPollen);
        }

        return balls;
    }
}
