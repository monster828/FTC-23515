package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RoSlider extends RoGraphic {

    private Telemetry telemetry = null;
    private int min;
    private int max;
    public int Number;

    public RoSlider(int min, int max, Telemetry tem) {
        telemetry = tem;
        this.min = min;
        this.max = max;
        Number=min;
    }

    @Override
    public Object getOutput() {
        return Number;
    }

    @Override
    public void setOutput(Object o) {
        if(o instanceof Integer) {
            Number = (int)o;
        }
    }

    @Override
    public void interactRight() {
        if(Number < max) {
            Number++;
        }
    }

    @Override
    public void interactLeft() {
        if(Number > min) {
            Number--;
        }
    }

    @Override
    public void interactUp() {

    }

    @Override
    public void interactDown() {

    }

    @Override
    public void interact() {}

    @Override
    public void render(boolean selected) {
        String data;
        int length = 1;
        while(max/Math.pow(10,length) >= 1) {length++;}
        length = 25-2-(length-1);
        data = Number+" ";
        float slidersize = (float) length /(max-min);
        int sliderlocation = Math.round(Number*slidersize);
        for(int i = 1; i < sliderlocation; i++) {data += "-";}
        data += "=";
        for(int i = sliderlocation; i < length; i++) {data += "-";}
        if(selected) {
            data = "▓"+data;
            telemetry.addData("",data);
        } else {
            data = " "+data;
            telemetry.addData("",data);
        }
    }

    @Override
    public boolean takeControls() {
        return false;
    }

}
