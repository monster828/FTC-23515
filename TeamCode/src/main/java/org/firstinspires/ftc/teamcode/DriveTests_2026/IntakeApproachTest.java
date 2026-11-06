package org.firstinspires.ftc.teamcode.DriveTests_2026;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.utils.Angle;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Drives start -> field center, then times three approach strategies for closing the distance
 * to a scoring piece at 12/24/48/72in from center, 3 reps each, and reports the fastest strategy
 * per distance. This only measures drivetrain approach time (no intake hardware is referenced
 * anywhere in this project yet), so "best" here means fastest to get lined up on the piece.
 * Between maneuvers the drivetrain briefly goes idle instead of continuously driving/holding, and
 * battery voltage is sampled throughout, to help avoid/diagnose mid-run Control Hub brownouts.
 */
@Autonomous(name = "IntakeApproachTest", group = "Tests")
public class IntakeApproachTest extends LinearOpMode {

    private Follower follower;

    private static final double FIELD_SIZE = 141;
    private static final double ROBOT_SIZE = 18;
    private static final double WALL_CLEARANCE = 12;

    private static final double[] TEST_DISTANCES = {12, 24, 48, 72};
    private static final int REPS_PER_CONDITION = 3;
    private static final double HEADING_TOLERANCE_DEG = 2.0;
    private static final double POSITION_TOLERANCE = 0.5;

    // Brief rest stops (drivetrain idle, not holding) so the battery gets a chance to recover
    // between maneuvers instead of the motors drawing correction current continuously for the
    // whole test - this was added after repeated Control Hub reboots mid-run, likely brownouts.
    private static final long INTER_TRIAL_PAUSE_MS = 500;
    private static final long INTER_DISTANCE_PAUSE_MS = 1500;

    // Heading the robot holds while sitting at center between trials, and the world-frame bearing
    // toward the simulated piece. The piece is offset 90 degrees from that reference heading so
    // every trial actually needs a strafe and/or a turn (a piece straight ahead would need neither).
    // That 90 degree offset is placed along the field diagonal (same direction as the run in from
    // the corner) because the diagonal gives the most room before the perimeter: with a 141x141
    // field and an 18in robot kept ~12in off the wall, the center can safely reach about 70in along
    // a diagonal versus only ~49.5in along a cardinal direction. The 72in trial still runs about 2in
    // short of the full 12in perimeter buffer on that leg -- flagged here since it's the one distance
    // that doesn't fully fit the "~12in" guideline on this field size.
    private static final double REFERENCE_HEADING_DEG = 45;
    private static final double TARGET_BEARING_DEG = REFERENCE_HEADING_DEG + 90;

    // STRAFE_45 holds a heading offset 45 degrees from the straight-ahead bearing to the target
    // (referenceHeadingRad below, which is 45 degrees short of targetBearingRad by construction).
    // STRAFE_90 instead holds a heading offset a full 90 degrees from that same straight-ahead
    // bearing, i.e. perpendicular to the direction of travel -- a pure crab strafe rather than the
    // diagonal crab angle STRAFE_45 drives.
    private static final double STRAFE_90_HEADING_OFFSET_DEG = 90;

    private final Pose start;
    private final Pose center;
    private final double referenceHeadingRad;
    private final double targetBearingRad;
    private final double strafe90HeadingRad;

    public IntakeApproachTest() {
        PoseFactory poseFactory = PoseFactory.degrees();
        start = poseFactory.of(0, 0, 90);
        center = poseFactory.of(60, 36, 90);
        referenceHeadingRad = center.heading();
        targetBearingRad = Math.toRadians(TARGET_BEARING_DEG);
        strafe90HeadingRad = targetBearingRad - Math.toRadians(STRAFE_90_HEADING_OFFSET_DEG);
    }

    private enum Approach {
        STRAFE_45("Strafe, 45° heading offset"),
        STRAFE_90("Strafe, 90° heading offset (perpendicular)"),
        STRAFE_THEN_TURN("Strafe, turn halfway, then forward"),
        TURN_THEN_DRIVE("Turn to face it, then drive");

        final String label;

        Approach(String label) {
            this.label = label;
        }
    }

    private final double[][][] times =
            new double[TEST_DISTANCES.length][Approach.values().length][REPS_PER_CONDITION];
    private final double[][][] voltageBefore =
            new double[TEST_DISTANCES.length][Approach.values().length][REPS_PER_CONDITION];
    private final double[][][] voltageAfter =
            new double[TEST_DISTANCES.length][Approach.values().length][REPS_PER_CONDITION];
    private final int[][] completedReps = new int[TEST_DISTANCES.length][Approach.values().length];
    private double minVoltageSeen = Double.POSITIVE_INFINITY;
    private File logFile;

    @Override
    public void runOpMode() {
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        initLiveLog();

        telemetry.addLine("Intake Approach Test ready.");
        telemetry.addLine("Drives start -> center, then times 3 approach methods at 12/24/48/72in, 3 reps each.");
        telemetry.addData("Battery (V)", "%.2f", batteryVoltage());
        telemetry.addData("Logging live to", logFile != null ? logFile.getAbsolutePath() : "FAILED - see logcat");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        runPath(line(start, center).tangent());

        for (int d = 0; d < TEST_DISTANCES.length && opModeIsActive(); d++) {
            double distance = TEST_DISTANCES[d];
            Pose target = targetPose(distance);
            appendLog("=== Distance: " + distance + " in ===");

            for (Approach approach : Approach.values()) {
                if (!opModeIsActive()) break;
                appendLog("-- " + approach.label + " --");

                for (int rep = 0; rep < REPS_PER_CONDITION && opModeIsActive(); rep++) {
                    resetToCenter();
                    pause(INTER_TRIAL_PAUSE_MS);

                    double vBefore = batteryVoltage();
                    telemetry.addData("Distance (in)", distance);
                    telemetry.addData("Approach", approach.label);
                    telemetry.addData("Rep", (rep + 1) + "/" + REPS_PER_CONDITION);
                    telemetry.addData("Battery (V)", "%.2f", vBefore);
                    telemetry.update();

                    double elapsed = runTrial(approach, target);
                    double vAfter = batteryVoltage();

                    times[d][approach.ordinal()][rep] = elapsed;
                    voltageBefore[d][approach.ordinal()][rep] = vBefore;
                    voltageAfter[d][approach.ordinal()][rep] = vAfter;
                    completedReps[d][approach.ordinal()]++;

                    appendLog(String.format(Locale.US, "  Rep %d: %.3fs (battery %.2fV -> %.2fV, min so far %.2fV)",
                            rep + 1, elapsed, vBefore, vAfter, minVoltageSeen));

                    pause(INTER_TRIAL_PAUSE_MS);
                }
            }

            pause(INTER_DISTANCE_PAUSE_MS);
        }

        resetToCenter();
        reportResults();
    }

    /**
     * Opens (creating if needed) a timestamped log file and writes the header immediately, so
     * there is something on disk from the very start of the run - not just at the end. Each
     * subsequent write (appendLog) opens, writes, flushes and closes the file individually so a
     * mid-run crash/reboot loses at most the current rep instead of the whole run's data.
     */
    private void initLiveLog() {
        File dir = new File(MiscUtils.dataFolder);
        if (!dir.exists()) dir.mkdirs();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(new Date());
        logFile = new File(dir, "IntakeApproachTest_" + timestamp + ".txt");

        appendLog("Intake Approach Test - " + timestamp);
        appendLog("Field: " + FIELD_SIZE + "x" + FIELD_SIZE + "in, robot: " + ROBOT_SIZE + "x" + ROBOT_SIZE
                + "in, target wall clearance: ~" + WALL_CLEARANCE + "in");
        appendLog("Distances (in): " + java.util.Arrays.toString(TEST_DISTANCES)
                + ", reps per condition: " + REPS_PER_CONDITION);
        appendLog("Starting battery (V): " + String.format(Locale.US, "%.2f", batteryVoltage()));
        appendLog("");
    }

    /**
     * Appends one line to the live log file, opened/flushed/closed on every call (see
     * initLiveLog). If this fails it only logs to telemetry once - a file I/O problem shouldn't
     * abort the test.
     */
    private void appendLog(String line) {
        if (logFile == null) return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            telemetry.addData("Log write failed", e.getMessage());
        }
    }

    /**
     * Stops the drivetrain (idle, zero power - not holding a pose) for a short rest so the
     * battery isn't under continuous correction/drive current for the whole test. Odometry keeps
     * updating and battery voltage keeps getting sampled while paused.
     */
    private void pause(long ms) {
        follower.stop();
        long end = System.currentTimeMillis() + ms;
        while (opModeIsActive() && System.currentTimeMillis() < end) {
            follower.update();
            batteryVoltage();
        }
    }

    /**
     * Lowest reading across all voltage sensors on the hub(s), which also updates the
     * running minVoltageSeen used in the final report to help correlate with reboots.
     */
    private double batteryVoltage() {
        double min = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double v = sensor.getVoltage();
            if (v > 0 && v < min) min = v;
        }
        if (Double.isInfinite(min)) return 0;
        if (min < minVoltageSeen) minVoltageSeen = min;
        return min;
    }

    private Pose targetPose(double distance) {
        double x = center.x() + distance * Math.cos(targetBearingRad);
        double y = center.y() + distance * Math.sin(targetBearingRad);
        return new Pose(x, y, targetBearingRad);
    }

    private double runTrial(Approach approach, Pose target) {
        long startNanos = System.nanoTime();

        switch (approach) {
            case STRAFE_45:
                runPath(line(center, target).constant(referenceHeadingRad));
                break;
            case STRAFE_90:
                runPath(line(center, target).constant(strafe90HeadingRad));
                break;
            case STRAFE_THEN_TURN:
                Pose midpoint = new Pose((center.x() + target.x()) / 2.0, (center.y() + target.y()) / 2.0,
                        referenceHeadingRad);
                runPath(line(center, midpoint).constant(referenceHeadingRad));
                turnTo(midpoint.withHeading(target.heading()));
                runPath(line(midpoint, target).constant(target.heading()));
                break;
            case TURN_THEN_DRIVE:
                turnTo(center.withHeading(target.heading()));
                runPath(line(center, target).constant(target.heading()));
                break;
        }

        return (System.nanoTime() - startNanos) / 1e9;
    }

    private void resetToCenter() {
        if (follower.pose().distance(center) > POSITION_TOLERANCE) {
            runPath(line(follower.pose(), center).tangent());
        }
        turnTo(center);
    }

    private void runPath(Path path) {
        follower.follow(path);
        while (opModeIsActive() && !follower.atParametricEnd()) {
            follower.update();
            batteryVoltage();
        }
        follower.update();
    }

    private void turnTo(Pose pose) {
        double toleranceRad = Math.toRadians(HEADING_TOLERANCE_DEG);
        // Skip re-engaging hold() when already within tolerance - this doesn't loosen what
        // counts as "done" (same toleranceRad is still the exit condition below and the one
        // TURN_THEN_DRIVE is timed against), it just avoids needlessly kicking off a fresh
        // correction burst when there's nothing left to correct.
        if (Angle.smallestDifference(follower.pose().heading(), pose.heading()) <= toleranceRad) {
            return;
        }

        follower.hold(pose);
        follower.update();
        while (opModeIsActive() && Angle.smallestDifference(follower.pose().heading(), pose.heading()) > toleranceRad) {
            follower.update();
            batteryVoltage();
        }
    }

    private void reportResults() {
        appendSummaryToLog();

        telemetry.clearAll();
        telemetry.addLine("=== Intake Approach Test Results ===");

        for (int d = 0; d < TEST_DISTANCES.length; d++) {
            telemetry.addLine("--- " + TEST_DISTANCES[d] + " in ---");
            Approach best = bestApproach(d);

            for (Approach approach : Approach.values()) {
                telemetry.addData(approach.label, String.format(Locale.US, "%.2fs avg (n=%d)",
                        average(d, approach), completedReps[d][approach.ordinal()]));
            }

            telemetry.addData("Best", best != null ? String.format(Locale.US, "%s (%.2fs)", best.label, average(d, best)) : "n/a");
        }

        telemetry.addLine();
        telemetry.addData("Min battery (V) seen", "%.2f", minVoltageSeen);
        telemetry.addData("Saved to", logFile != null ? logFile.getAbsolutePath() : "FAILED TO SAVE - see logcat");
        telemetry.update();

        while (opModeIsActive()) {
            follower.update();
        }
    }

    private double average(int d, Approach approach) {
        int n = completedReps[d][approach.ordinal()];
        if (n == 0) return 0;
        double sum = 0;
        for (int rep = 0; rep < n; rep++) sum += times[d][approach.ordinal()][rep];
        return sum / n;
    }

    private Approach bestApproach(int d) {
        Approach best = null;
        double bestAvg = Double.MAX_VALUE;
        for (Approach approach : Approach.values()) {
            if (completedReps[d][approach.ordinal()] == 0) continue;
            double avg = average(d, approach);
            if (avg < bestAvg) {
                bestAvg = avg;
                best = approach;
            }
        }
        return best;
    }

    /**
     * Appends the per-condition/per-distance averages and the winning approach at each distance
     * to the same live log file (see initLiveLog/appendLog) that already has every individual
     * rep's time and voltage written out. Only reached if the run makes it to the end without
     * rebooting - the per-rep lines are what survive a mid-run crash.
     */
    private void appendSummaryToLog() {
        appendLog("");
        appendLog("=== SUMMARY ===");
        appendLog(String.format(Locale.US, "Minimum battery voltage observed during run: %.2fV", minVoltageSeen));
        appendLog("");

        for (int d = 0; d < TEST_DISTANCES.length; d++) {
            appendLog("--- Distance: " + TEST_DISTANCES[d] + " in ---");

            for (Approach approach : Approach.values()) {
                int n = completedReps[d][approach.ordinal()];
                appendLog(String.format(Locale.US, "%s: %.3fs avg (n=%d)", approach.label, average(d, approach), n));
            }

            Approach best = bestApproach(d);
            appendLog(best != null
                    ? String.format(Locale.US, "Best at %sin: %s (%.3fs avg)", TEST_DISTANCES[d], best.label, average(d, best))
                    : "Best at " + TEST_DISTANCES[d] + "in: n/a (no completed reps)");
            appendLog("");
        }

        appendLog("=== RUN COMPLETE ===");
    }
}
