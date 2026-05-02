package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RoLabel extends RoGraphic {

    private Telemetry telemetry;
    private String label;
    public RoLabel(String label, Telemetry telemetry) {
        this.label = label;
        this.telemetry = telemetry;
    }
    @Override
    public Object getOutput() {
        return label;
    }

    @Override
    public void setOutput(Object o) {
        if(o instanceof String) {
            label = (String)o;
        }
    }

    @Override
    public void interactRight() {

    }

    @Override
    public void interactLeft() {

    }

    @Override
    public void interactUp() {

    }

    @Override
    public void interactDown() {

    }

    @Override
    public void interact() {

    }

    @Override
    public void render(boolean selected) {
        if(selected) {
            telemetry.addData("", "▓"+label);
        } else {
            telemetry.addData("", " "+label);
        }
    }

    @Override
    public boolean takeControls() {
        return false;
    }
}
