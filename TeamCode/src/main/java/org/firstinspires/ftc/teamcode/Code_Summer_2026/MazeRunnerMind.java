package org.firstinspires.ftc.teamcode.Code_Summer_2026;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class MazeRunnerMind {
    int[][] mazeDistances = new int[16][16]; // Grid size
    int[][] mazeWalls = new int[16][16];

    int _goalX, _goalY;

    int currentX, currentY;
    int currentHeading;

    DistanceSensor _distanceSensorFront;
    DistanceSensor _distanceSensorLeft;
    DistanceSensor _distanceSensorRight;

    double _distanceFront;
    double _distanceLeft;
    double _distanceRight;

    int _startX, _startY;

    public MazeRunnerMind(DistanceSensor distanceSensorFront, DistanceSensor distanceSensorLeft, DistanceSensor distanceSensorRight, int start_x, int start_y, int goalX, int goalY){
        _distanceSensorFront = distanceSensorFront;
        _distanceSensorLeft = distanceSensorLeft;
        _distanceSensorRight = distanceSensorRight;
        _startX = start_x;
        _startY = start_y;
        _goalX = goalX;
        _goalY = goalY;
    }

    public int DoMazeRunnerMind(float x, float y){
        // Set distances
        SetDistanceSensorsDistances();

        // Check each distance and determine if dead end, if there is a way to the right, if there is a way to the left
        updateWalls();

        // 2. Run the 'Flood' function to update 'mazeDistances'
        Flood();

        // 3. Compare values of adjacent cells
        // Logic to find neighbor coordinates based on current heading
        int frontX = currentX, frontY = currentY;
        int leftX = currentX, leftY = currentY;
        int rightX = currentX, rightY = currentY;

// North
        if (currentHeading == 0) {
            frontY++; leftX--; rightX++;
        }
// East
        else if (currentHeading == 1) {
            frontX++; leftY++; rightY--;
        }
// South
        else if (currentHeading == 2) {
            frontY--; leftX++; rightX--;
        }
// West
        else if (currentHeading == 3) {
            frontX--; leftY--; rightY++;
        }

        // 4. Return the direction that leads to the smallest number

// Get the current wall bitmask for this cell
        int walls = mazeWalls[currentX][currentY];

// 1. Can we go forward? (Wall check + distance check)
// Assuming 0=Forward, 1=Right, -1=Left, 2=Backwards
        int bestMove = 2; // Default to backwards if stuck
        int minDistance = 255;

// Check Forward
// Heading 0=N(bit 1), 1=E(bit 2), 2=S(bit 4), 3=W(bit 8)
        boolean wallInFront = (walls & (1 << currentHeading)) != 0;
        if (!wallInFront && isValid(frontX, frontY)) {
            minDistance = mazeDistances[frontX][frontY];
            bestMove = 0;
        }

// Check Right
        int rightHeading = (currentHeading + 1) % 4;
        boolean wallToRight = (walls & (1 << rightHeading)) != 0;
        if (!wallToRight && isValid(rightX, rightY)) {
            if (mazeDistances[rightX][rightY] < minDistance) {
                minDistance = mazeDistances[rightX][rightY];
                bestMove = 1;
            }
        }

// Check Left
        int leftHeading = (currentHeading + 3) % 4;
        boolean wallToLeft = (walls & (1 << leftHeading)) != 0;
        if (!wallToLeft && isValid(leftX, leftY)) {
            if (mazeDistances[leftX][leftY] < minDistance) {
                minDistance = mazeDistances[leftX][leftY];
                bestMove = -1;
            }
        }

        return bestMove;
    }

    boolean isValid(int x, int y) {
        return x >= 0 && x < 16 && y >= 0 && y < 16;
    }

    void Flood() {
        // 1. Reset all distances to a high value (like 255)
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) mazeDistances[i][j] = 255;
        }

        // 2. Set the goal(s) to 0. For a 16x16, usually (7,7), (7,8), (8,7), (8,8)
        mazeDistances[_goalX][_goalY] = 0;

        boolean changed = true;
        while (changed) {
            changed = false;
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 16; y++) {
                    // Skip cells we haven't reached yet in this "wave"
                    if (mazeDistances[x][y] == 255) continue;

                    int currentVal = mazeDistances[x][y];

                    // Check NORTH (y + 1)
                    // (mazeWalls[x][y] & 1) == 0 means "No wall at North bit"
                    if (y < 15 && (mazeWalls[x][y] & 1) == 0) {
                        if (mazeDistances[x][y + 1] > currentVal + 1) {
                            mazeDistances[x][y + 1] = currentVal + 1;
                            changed = true;
                        }
                    }

                    // Check EAST (x + 1)
                    if (x < 15 && (mazeWalls[x][y] & 2) == 0) {
                        if (mazeDistances[x + 1][y] > currentVal + 1) {
                            mazeDistances[x + 1][y] = currentVal + 1;
                            changed = true;
                        }
                    }

                    // Check SOUTH (y - 1)
                    if (y > 0 && (mazeWalls[x][y] & 4) == 0) {
                        if (mazeDistances[x][y - 1] > currentVal + 1) {
                            mazeDistances[x][y - 1] = currentVal + 1;
                            changed = true;
                        }
                    }

                    // Check WEST (x - 1)
                    if (x > 0 && (mazeWalls[x][y] & 8) == 0) {
                        if (mazeDistances[x - 1][y] > currentVal + 1) {
                            mazeDistances[x - 1][y] = currentVal + 1;
                            changed = true;
                        }
                    }
                }
            }
        }
    }

    void updateWalls() {
        boolean wallFront = GetIfWall(_distanceFront);
        boolean wallRight = GetIfWall(_distanceRight);
        boolean wallLeft = GetIfWall(_distanceLeft);

        // Front wall
        if (wallFront) addWall(currentHeading);

        // Right wall (Heading + 1, wrapped 0-3)
        if (wallRight) addWall((currentHeading + 1) % 4);

        // Left wall (Heading - 1, wrapped 0-3)
        if (wallLeft) addWall((currentHeading + 3) % 4);
    }

    void addWall(int direction) {
        // direction: 0=N, 1=E, 2=S, 3=W
        mazeWalls[currentX][currentY] |= (1 << direction);
    }

    Boolean GetIfWall(double distance){
        return distance < 100;
    }

    void SetDistanceSensorsDistances(){
        _distanceFront = _distanceSensorFront.getDistance(DistanceUnit.MM);
        _distanceLeft = _distanceSensorLeft.getDistance(DistanceUnit.MM);
        _distanceRight = _distanceSensorRight.getDistance(DistanceUnit.MM);
    }
}
