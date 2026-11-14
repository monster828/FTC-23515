//package org.firstinspires.ftc.teamcode.Utils.Shooting;
//
//import java.util.Map;
//import java.util.TreeMap;
//
//public class ShooterLookup {
//    // Maps Distance (inches) -> Value
//    private final TreeMap<Double, Double> pollenHoodMap = new TreeMap<>();
//    private final TreeMap<Double, Double> pollenRpmMap = new TreeMap<>();
//
//    private final TreeMap<Double, Double> nectarHoodMap = new TreeMap<>();
//    private final TreeMap<Double, Double> nectarRpmMap = new TreeMap<>();
//
//    public ShooterLookup() {
//        // Replace dWall with the actual distance from turret center to Hive center when against the wall
//        double dWall = 36.0; // Example placeholder in inches
//        double dFar = dWall + 23.5;
//
//        // Pollen Data
//        pollenHoodMap.put(dWall, 0.20);
//        pollenRpmMap.put(dWall, 2310.0);
//
//        pollenHoodMap.put(dFar, 0.15);
//        pollenRpmMap.put(dFar, 2500.0);
//
//        // Nectar Data (Verify the 2300 value when back on the robot!)
//        nectarHoodMap.put(dWall, 0.14);
//        nectarRpmMap.put(dWall, 3180.0);
//
//        nectarHoodMap.put(dFar, 0.15);
//        nectarRpmMap.put(dFar, 2300.0);
//    }
//
//    public double getHood(double distanceInches, boolean isNectar) {
//        return interpolate(isNectar ? nectarHoodMap : pollenHoodMap, distanceInches);
//    }
//
//    public double getVelocity(double distanceInches, boolean isNectar) {
//        return interpolate(isNectar ? nectarRpmMap : pollenRpmMap, distanceInches);
//    }
//
//    private double interpolate(TreeMap<Double, Double> map, double key) {
//        if (map.containsKey(key)) return map.get(key);
//
//        Map.Entry<Double, Double> lower = map.floorEntry(key);
//        Map.Entry<Double, Double> upper = map.ceilingEntry(key);
//
//        // Clamp to min/max if outside tested range
//        if (lower == null) return upper.getValue();
//        if (upper == null) return lower.getValue();
//
//        // Linear interpolation formula
//        double ratio = (key - lower.getKey()) / (upper.getKey() - lower.getKey());
//        return lower.getValue() + ratio * (upper.getValue() - lower.getValue());
//    }
//}