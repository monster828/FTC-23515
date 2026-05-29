package org.firstinspires.ftc.teamcode.Utils.Movement.Testing;
import java.util.List;

public class BestToleranceFinder {
    public static float INITIAL_TOLERANCE = 5;
    private float _currentTolerance;

    private List<Float[]> _timesTolerance;

    private float _currentDirection = 0;

    public int Test = 10;

    public void CompletedTestResults(float time, float accuracy){
        _timesTolerance.add(new Float[] {GetCurrentTolerance(), time, accuracy});
        UpdateTestResults();
    }

    public void NewTest(){
        int previousTest = _timesTolerance.size();
        if (previousTest == 0){
            // No previous test, try lower than initial tolerance
            _currentTolerance = INITIAL_TOLERANCE - 1;
        }else if (previousTest == 1) {
            // There have been one previous test, create new test with default current tolerance
            _currentTolerance = INITIAL_TOLERANCE;
        }else{
            // There have been at least three previous test
            // When time is greater go in the opposite direction
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
            float scorePrevious = previousTest[2] / previousTest[1];
            float scorePreviousPrevious = previousPreviousTest[2] / previousPreviousTest[1];

            _currentDirection = (previousTest[0] - previousPreviousTest[0]) * (scorePrevious - scorePreviousPrevious);
        }else{
            Float[] previousTest = _timesTolerance.get(_timesTolerance.size() - 1);
            Float[] bestTest = _timesTolerance.get(indexOfHighestScore);

            _currentDirection = (previousTest[0] - bestTest[0]) / 2;
        }
    }
}
