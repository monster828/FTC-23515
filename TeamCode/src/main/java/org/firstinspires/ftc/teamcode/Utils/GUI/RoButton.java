package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RoButton extends RoGraphic{

    private String label;
    private Telemetry telemetry;
    private boolean out = false;
    private long delay = 0;
    private long delaytick;

    public RoButton(Telemetry tem, String label) {
        this.label = label;
        telemetry = tem;
    }
    @Override
    public Object getOutput() {
        if(out) {
            delaytick = System.currentTimeMillis();
            delay = 100;
            out = false;
            return true;
        } else {
            if(delay > 0) {delay -= (System.currentTimeMillis()-delaytick);
            delaytick = System.currentTimeMillis();}
            return false;
        }
    }

    @Override
    public void setOutput(Object o) {
        if(o instanceof Boolean) {
            out = (boolean)o;
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
        out = true;
    }

    @Override
    public void render(boolean selected) {
        String string = " [";
        for(int i = 1; i < 11-(label.length()/2);i++) {
            if(selected) {
                if(out || delay > 0) {
                    string += "░";
                } else {
                    string += "▒";
                }
            } else {
                if(out || delay > 0) {
                    string += "▒";
                } else {
                    string += "▓";
                }
            }
        }
        string+=label;
        while(string.length()<24) {
            if(selected) {
                if(out || delay > 0) {
                    string += "░";
                } else {
                    string += "▒";
                }
            } else {
                if(out || delay > 0) {
                    string += "▒";
                } else {
                    string += "▓";
                }
            }
        }
        telemetry.addData("",string+"]");
    }

    @Override
    public boolean takeControls() {
        return false;
    }
}
