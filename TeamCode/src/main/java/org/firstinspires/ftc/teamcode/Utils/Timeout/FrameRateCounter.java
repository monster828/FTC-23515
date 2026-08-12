package org.firstinspires.ftc.teamcode.Utils.Timeout;

import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.util.ArrayList;

public class FrameRateCounter {
    private long _lastTime;

    ArrayList<Integer> _lastTenFrames = new ArrayList<>();

    public void Reset(){
        _lastTenFrames.clear();
    }

    public double Frame(){

        Integer frameLength = Math.round((System.nanoTime() - _lastTime) / 1_000_000);
        _lastTenFrames.add(frameLength);

        if (_lastTenFrames.size() > 10){
            _lastTenFrames.remove(0);
        }

        _lastTime = System.nanoTime();

        return MiscUtils.getAverage(_lastTenFrames.stream().mapToDouble(Integer::doubleValue).toArray());
    }

}
