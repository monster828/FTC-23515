package org.firstinspires.ftc.teamcode.Utils.GUI;


import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RoTab extends RoGraphic {

    private boolean open = false;
    private Telemetry tem;
    private String label;
    public RoGUI gui;

    public RoTab(String label, Telemetry telemetry) {
        this.label = label;
        tem = telemetry;
        gui = new RoGUI(tem);
        gui.addButton("Go Back");
    }

    @Override
    public Object getOutput() {
        return null;
    }

    public Object getOutOf(int n) {
        return gui.getOutOf(n);
    }

    @Override
    public void setOutput(Object o) {

    }

    @Override
    public void interactRight() {
        if(open) {
            gui.right();
        } else {
            open = true;
        }
    }

    @Override
    public void interactLeft() {
        if(open) {
            gui.left();
        }
    }

    @Override
    public void interactUp() {
        gui.up();
    }

    @Override
    public void interactDown() {
        gui.down();
    }

    @Override
    public void interact() {
        if(!open) {
            open = true;
        } else {
            gui.interact();
        }
    }

    @Override
    public void render(boolean selected) {
        if(gui.getOutOf(0).equals(true)) {
            open = false;
        }
        if(!open) {
            String dat = "";
            if (selected) {
                dat += "▓" + label;
            } else {
                dat += " " + label;
            }
            while (dat.length() < 25) {
                dat += "-";
            }
            dat += ">";
            tem.addData("",dat);
        } else {
            gui.render();
        }
    }

    @Override
    public boolean takeControls() {
        return open;
    }

    public void add(RoGraphic g) {
        gui.add(g);
    }
    public void addSlider(int min, int max) {
        gui.add(new RoSlider(min,max,tem));
    }
    public void addButton(String label) {
        gui.add(new RoButton(tem,label));
    }
    public void addLabel(String label) {
        gui.add(new RoLabel(label,tem));
    }
    public void addNumInput(String label,int Integers, int Decimals) {
        gui.add(new RoNumInput(label,Integers,Decimals,tem));
    }
    public void addTab(String label) {
        gui.add(new RoTab(label,tem));
    }
}
