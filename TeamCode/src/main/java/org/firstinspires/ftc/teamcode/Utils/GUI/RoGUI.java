package org.firstinspires.ftc.teamcode.Utils.GUI;

import static java.lang.Thread.sleep;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;

public class RoGUI {

    private Telemetry telemetry;
    private ArrayList<RoGraphic> graphics = new ArrayList<RoGraphic>();
    private int selected = 0;
    private boolean captive = false;

    public RoGUI(Telemetry tem) {
        telemetry = tem;
    }
    public void up() {
        if(!captive) {
            if (selected > 0) {
                selected--;
            }
        } else {
            graphics.get(selected).interactUp();
        }
    }
    public void down() {
        if(!captive) {
            if (selected < graphics.size() - 1) {
                selected++;
            }
        } else {
            graphics.get(selected).interactDown();
        }
    }
    public void left() {
        graphics.get(selected).interactLeft();
    }
    public void right() {
        graphics.get(selected).interactRight();
    }
    public void interact() {
        graphics.get(selected).interact();
    }

    public Object getOutOf(int n) {
        if(n > -1 && n < graphics.size()) {
            return graphics.get(n).getOutput();
        } else {
            return 0;
        }
    }
    public RoGraphic get(int n) {return graphics.get(n);}
    public Object getOutSelected() {
        return graphics.get(selected).getOutput();
    }

    public void render() {
        captive = graphics.get(selected).takeControls();
        if(!captive) {
            for (int i = 0; i < graphics.size(); i++) {
                graphics.get(i).render(i == selected);
            }
        } else {
            graphics.get(selected).render(true);
        }
    }

    public void setOutput(int n, Object data) {
        graphics.get(n).setOutput(data);
    }

    public void clear() {
        graphics.clear();
        selected = 0;
    }

    public void add(RoGraphic g) {
        graphics.add(g);
    }

    public void addSlider(int min, int max) {
        graphics.add(new RoSlider(min,max,telemetry));
    }
    public void addButton(String label) {
        graphics.add(new RoButton(telemetry,label));
    }
    public void addLabel(String label) {
        graphics.add(new RoLabel(label,telemetry));
    }
    public void addNumInput(String label,int Integers, int Decimals) {
        graphics.add(new RoNumInput(label,Integers,Decimals,telemetry));
    }
    public void addSwitch(String label){graphics.add(new RoSwitch(label,telemetry));}

}
