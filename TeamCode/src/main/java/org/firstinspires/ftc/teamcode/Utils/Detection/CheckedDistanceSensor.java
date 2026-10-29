package org.firstinspires.ftc.teamcode.Utils.Detection;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CheckedDistanceSensor {

    ArrayList<Double> pings = new ArrayList<>();
    int c = 3;
    DistanceSensor d;

    public CheckedDistanceSensor(DistanceSensor d, int checks) {
        for(int i = 0; i < checks; i++) pings.add(d.getDistance(DistanceUnit.INCH));
        c = checks;
        this.d = d;
    }

    public void update() {
        if(pings.get(pings.size()-1) != d.getDistance(DistanceUnit.INCH)) {
            pings.remove(0);
            pings.add(d.getDistance(DistanceUnit.INCH));
        }
    }

    public double getDistance() {
        double[] pings0 = new double[c];
        for(int i = 0; i < c; i++) {
            pings0[i] = pings.get(i);
        }
        Arrays.sort(pings0);
        return pings0[pings0.length/2];
    }

}
