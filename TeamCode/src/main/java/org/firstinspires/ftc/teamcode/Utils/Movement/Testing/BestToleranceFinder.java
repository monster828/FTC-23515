package org.firstinspires.ftc.teamcode.Utils.Movement.Testing;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BestToleranceFinder {
    public static float INITIAL_TOLERANCE = 5.0f;
    private float _currentTolerance;
    private List<Float[]> _timesTolerance = new ArrayList<>();
    private float _currentDirection = -0.5f; // Start by nudging it downward

    public int maxTests;
    static String _path = MiscUtils.dataFolder + "/tolerance";
    private Boolean _complete = false;

    public BestToleranceFinder(int testAmount){
        this.maxTests = testAmount;
    }

    public void CompletedTestResults(float time, float accuracy, float rotationAccuracy){
        // Store: [Tolerance, Time, Accuracy, RotationAccuracy]
        _timesTolerance.add(new Float[] {GetCurrentTolerance(), time, accuracy, rotationAccuracy});

        UpdateTestResults();

        // If we have reached the requested amount of tests, save and finish
        if (_timesTolerance.size() >= maxTests){
            int i = 0;
            while (MiscUtils.checkFile(_path + i + ".txt")){
                i++;
            }
            SaveData(_path + i + ".txt");
            _complete = true;
        }
    }

    public Boolean IsTestComplete(){
        return _complete;
    }

    public int AmountOfTest(){
        return _timesTolerance.size();
    }

    public void NewTest(){
        int completedTests = _timesTolerance.size();

        if (completedTests == 0){
            _currentTolerance = INITIAL_TOLERANCE;
        } else if (completedTests == 1) {
            // Second test uses a slightly different tolerance to establish a baseline trend direction
            _currentTolerance = INITIAL_TOLERANCE - 1.0f;
        } else {
            // Apply the step direction calculated in UpdateTestResults
            _currentTolerance += _currentDirection;

            // Guardrail: Don't let tolerance drop below 0
            if (_currentTolerance < 0.1f) {
                _currentTolerance = 0.1f;
            }
        }
    }

    public float GetCurrentTolerance(){
        return _currentTolerance;
    }

    private void UpdateTestResults(){
        if (_timesTolerance.size() < 2){
            return;
        }

        // Since lower accuracy and lower time are better, we want to MINIMIZE the cost.
        int indexOfBestScore = 0;
        float lowestCost = Float.MAX_VALUE;

        for (int i = 0; i < _timesTolerance.size(); i++){
            Float[] data = _timesTolerance.get(i);

            // Combined Cost function: Time + Translation Error + (Rotation Error weighted down)
            float currentCost = data[1] + data[2] + (data[3] / 5.0f);

            if (currentCost < lowestCost){
                indexOfBestScore = i;
                lowestCost = currentCost;
            }
        }

        int lastIndex = _timesTolerance.size() - 1;
        Float[] latestTest = _timesTolerance.get(lastIndex);
        Float[] previousTest = _timesTolerance.get(lastIndex - 1);

        float latestCost = latestTest[1] + latestTest[2] + (latestTest[3] / 5.0f);
        float previousCost = previousTest[1] + previousTest[2] + (previousTest[3] / 5.0f);

        // Gradient Step: Adjust tolerance based on whether performance got better or worse
        float costDelta = latestCost - previousCost;
        float toleranceDelta = latestTest[0] - previousTest[0];

        if (costDelta < 0) {
            // The change made things BETTER (cost decreased). Keep moving in that direction!
            _currentDirection = toleranceDelta >= 0 ? 0.5f : -0.5f;
        } else {
            // The change made things WORSE (cost increased). Reverse direction!
            _currentDirection = toleranceDelta >= 0 ? -0.5f : 0.5f;
        }
    }

    public void SaveData(String path){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < _timesTolerance.size(); i++){
                writer.write("Test " + i + ":");
                writer.newLine();
                writer.write("Tolerance: " + _timesTolerance.get(i)[0]);
                writer.newLine();
                writer.write("Time: " + _timesTolerance.get(i)[1]);
                writer.newLine();
                writer.write("Accuracy: " + _timesTolerance.get(i)[2]);
                writer.newLine();
                writer.write("Rotation Accuracy: " + _timesTolerance.get(i)[3]);
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            // OpMode logging can be done here if needed
        }
    }
}
/*
package org.firstinspires.ftc.teamcode.Utils.Movement.Testing;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BestToleranceFinder {
    public static float INITIAL_TOLERANCE = 5;
    private float _currentTolerance;

    private List<Float[]> _timesTolerance = new ArrayList<>();

    private float _currentDirection = 0;

    public int Test;

    static String _path = MiscUtils.dataFolder + "/tolerance";
    private Boolean _complete = false;

    public BestToleranceFinder(int testAmount){
        Test = testAmount;
    }

    public void CompletedTestResults(float time, float accuracy, float rotationAccuracy){
        _timesTolerance.add(new Float[] {GetCurrentTolerance(), time, accuracy, rotationAccuracy});
        UpdateTestResults();

        if (_timesTolerance.size() - 2 == Test){
            int i = 0;
            while (MiscUtils.checkFile(_path + i + ".txt")){
                i++;
            }

            SaveData(_path + i + ".txt");
            _complete = true;
        }
    }

    public Boolean IsTestComplete(){
        return _complete;
    }

    public int AmountOfTest(){
        return _timesTolerance.size() - 1;
    }

    public void NewTest(){
        int previousTest = _timesTolerance.size();
        if (previousTest == 0){
            // No previous test, try lower than initial tolerance
            _currentTolerance = INITIAL_TOLERANCE;
        }else if (previousTest == 1) {
            // There have been one previous test, create new test with default current tolerance
            _currentTolerance = INITIAL_TOLERANCE - 1;
        }else{
            // There have been at least three previous test
            // When time is greater go in the opposite direction
            _currentTolerance += _currentDirection;
        }
    }

    public float GetCurrentTolerance(){
        return _currentTolerance;
    }

    private void UpdateTestResults(){
        if (_timesTolerance.size() < 2){
            return;
        }

        int indexOfHighestScore = 0;
        float highestScore = 0;
        for (int i = 0; i < _timesTolerance.size(); i++){
            Float[] floats = _timesTolerance.get(i);
            float score = floats[2] / floats[1];
            if (score > highestScore){
                indexOfHighestScore = i;
                highestScore = score;
            }
        }

        if (indexOfHighestScore == _timesTolerance.size() - 1){
            // Previous test was best test
            Float[] previousTest = _timesTolerance.get(indexOfHighestScore);
            Float[] previousPreviousTest = _timesTolerance.get(indexOfHighestScore - 1);
            // accuracy + rotation accuracy / time
            float scorePrevious = (previousTest[2] + previousTest[3] / 5) / previousTest[1];
            float scorePreviousPrevious = (previousPreviousTest[2] + previousPreviousTest[3] / 5)  / previousPreviousTest[1];

            _currentDirection = (previousTest[0] - previousPreviousTest[0]) * (scorePrevious - scorePreviousPrevious);
        }else{
            Float[] previousTest = _timesTolerance.get(_timesTolerance.size() - 1);
            Float[] bestTest = _timesTolerance.get(indexOfHighestScore);

            _currentDirection = (previousTest[0] - bestTest[0]);
        }
    }

    public void SaveData(String path){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < _timesTolerance.size(); i++){
                writer.write("Test " + i + ":");

                writer.newLine();

                float tolerance = _timesTolerance.get(i)[0];
                writer.write("Tolerance: " + tolerance);

                writer.newLine();

                float time = _timesTolerance.get(i)[1];
                writer.write("Time: " + time);

                writer.newLine();

                float accuracy = _timesTolerance.get(i)[2];
                writer.write("Accuracy: " + accuracy);

                writer.newLine();
                float rotationAccuracy = _timesTolerance.get(i)[3];
                writer.write("Rotation Accuracy: " + rotationAccuracy);

                writer.newLine();
                writer.newLine();
            }

            writer.newLine(); // Adds a system-dependent newline
        } catch (IOException e) {
            // Error
        }
    }
}
*/