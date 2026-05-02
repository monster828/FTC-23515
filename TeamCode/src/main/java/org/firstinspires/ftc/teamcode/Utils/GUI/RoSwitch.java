package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RoSwitch extends RoGraphic {

    private Telemetry telemetry;
    private String label;
    private boolean switchiness = false;
    public RoSwitch(String label, Telemetry telemetry) {
        this.label = label;
        this.telemetry = telemetry;
    }
    @Override
    public Object getOutput() {
        return switchiness;
    }

    @Override
    public void setOutput(Object o) {
        switchiness = (boolean)o;
    }

    @Override
    public void interactRight() {
        switchiness = true;
    }

    @Override
    public void interactLeft() {
        switchiness = false;
    }

    @Override
    public void interactUp() {

    }

    @Override
    public void interactDown() {

    }

    @Override
    public void interact() {
        switchiness = !switchiness;
    }

    //□■
    @Override
    public void render(boolean selected) {
        String s;
        if(switchiness) s = " □■  On";
        else s = " ■□  Off";
        /*if(switchiness) s = " -O";
        else s = " O-";*/
        if(selected) {
            telemetry.addData("", "▓"+label+s);
        } else {
            telemetry.addData("", " "+label+s);
        }
    }

    @Override
    public boolean takeControls() {
        return false;
    }
}
